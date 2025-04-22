package com.oneplusone.service;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplusone.crawling.Gs25Crawling;
import com.oneplusone.crawling.ProductCategoryResolver;
import com.oneplusone.dtos.ProductDto;
import com.oneplusone.entity.Product;
import com.oneplusone.enums.Category;
import com.oneplusone.enums.ConvenienceName;
import com.oneplusone.enums.EventType;
import com.oneplusone.repository.CrawlingRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CrawlingService {
  private final CrawlingRepository crawlingRepository;
  private final Gs25Crawling gs25Crawling;
  private final ProductCategoryResolver productCategoryResolver;
  private final ObjectMapper objectMapper;

  //크롤링 메서드
  public void crawlingProductsAndUpdate() throws Exception {
    CompletableFuture<Void> oneToOneFuture = asyncCrawlAndSave(EventType.ONE_TO_ONE, ConvenienceName.GS25);
    CompletableFuture<Void> twoToOneFuture = asyncCrawlAndSave(EventType.TWO_TO_ONE, ConvenienceName.GS25);

    // 모든 비동기 작업이 완료될 때까지 대기
    CompletableFuture.allOf(oneToOneFuture, twoToOneFuture).join();

    // 크롤링 완료 후 카테고리 업데이트 실행
    updateCategory();
  }
  @Async
  protected CompletableFuture<Void> asyncCrawlAndSave(EventType eventType, ConvenienceName convName)
      throws InterruptedException {
    List<ProductDto> productDtoList = gs25Crawling.crawlProduct(eventType);
    List<Product> productList = new ArrayList<>();
    for (ProductDto productDto : productDtoList) {
      productList.add(Product.builder()
          .category(Category.UNCLASSIFIED)
          .convenienceBrand(convName)
          .createdAt(LocalDateTime.now())
          .productCost(productDto.getProductPrice())
          .productName(productDto.getProductName())
          .build());
    }
    crawlingRepository.saveProducts(productList);
    return CompletableFuture.completedFuture(null);
  }
  public void updateCategory() {
    List<Product> unclassifiedProductList = crawlingRepository.getUnclassifiedProductList();
    final int BATCH_SIZE = 400;

    for (int i = 0; i < unclassifiedProductList.size(); i += BATCH_SIZE) {
      int end = Math.min(i + BATCH_SIZE, unclassifiedProductList.size());
      List<Product> batch = unclassifiedProductList.subList(i, end);

      try {
        ResponseEntity<String> response = productCategoryResolver.getProductCategories(batch);

        if (response.getStatusCode() == HttpStatus.OK) {
          List<Map<String, Object>> responseList = objectMapper.readValue(response.getBody(), List.class);

          //QueryDsl을 사용하면 DB에 너무 많이 접근하므로, DataJPA를 생성해서 DB에 한번만 접근한다.
          List<Product> updatedProducts = new ArrayList<>();
          for (int j = 0; j < batch.size(); j++) {
            Product p = batch.get(j);
            String label = (String) responseList.get(j).get("label");
            Category category = Category.fromLabel(label);
            Product updated = Product.builder()
                .productId(p.getProductId())
                .productName(p.getProductName())
                .productCost(p.getProductCost())
                .convenienceBrand(p.getConvenienceBrand())
                .createdAt(p.getCreatedAt())
                .category(category)
                .build();

            updatedProducts.add(updated);
          }

          crawlingRepository.saveProducts(updatedProducts);
        } else {
          log.error("카테고리 API 응답 오류: {}", response.getBody());
        }

      } catch (Exception e) {
        log.error("배치 처리 중 오류 발생: {}", e.getMessage(), e);
      }
    }
  }



}
