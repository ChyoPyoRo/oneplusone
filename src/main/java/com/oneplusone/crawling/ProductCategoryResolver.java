package com.oneplusone.crawling;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplusone.dtos.ProductDto;
import com.oneplusone.entity.Product;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProductCategoryResolver {
  @Value("${huggingface.url}")
  private String huggingFaceUrl;
  @Value("${huggingface.token}")
  private String huggingFaceToken;

  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;



  public ResponseEntity<String> getProductCategories(List<Product> products) {
    List<String> productNameList = products.stream()
        .map(Product::getProductName)
        .toList();

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setAccept(List.of(MediaType.APPLICATION_JSON));
      headers.setBearerAuth(huggingFaceToken);

      Map<String, Object> body = Map.of("inputs", productNameList);
      HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

      ResponseEntity<String> response = restTemplate.postForEntity(huggingFaceUrl, entity, String.class);
      log.info("허깅페이스에서 카테고리 정보 받아옴");
      return response;
    } catch (Exception e) {
      log.error("API 호출 실패: ", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("API 호출 실패: " + e.getMessage());
    }
  }

  public String getBaseUrl() {
    return huggingFaceUrl;
  }
}
