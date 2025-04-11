package com.oneplusone.crawling;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplusone.dtos.ProductDto;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ProductCategoryResolver {
  @Value("${huggingface.url}")
  private static String huggingFaceUrl;
  @Value("${huggingface.token}")
  private static String huggingFaceToken;
  private final RestTemplate restTemplate;
  private final ObjectMapper objectMapper;


  public List<ProductDto> getProductCategories(List<ProductDto> products) {
    //상품 이름 list 생성
    List<String> productNameList = products.stream().map(ProductDto::getProductName).toList();
    try {
      // 1. Header 설정
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(huggingFaceToken); // Authorization: Bearer {token}

      // 2. Body 생성
      Map<String, Object> body = Map.of("inputs", productNameList);
      HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

      // 3. POST 요청 실행
      ResponseEntity<String> response = restTemplate.postForEntity(huggingFaceUrl, entity, String.class);

      // 4. 디버깅용 출력
      System.out.println("응답: " + response.getBody());

      // TODO: response.getBody() → JSON 파싱 후 products에 category 붙이기
    } catch (Exception e) {
      System.out.println("API 호출 실패: " + e.getMessage());
    }

    return products;
  }

  public String getBaseUrl() {
    return huggingFaceUrl;
  }
}
