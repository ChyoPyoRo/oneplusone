package com.oneplusone.crawling;

import com.fasterxml.jackson.core.JsonProcessingException;
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

  public List<ProductDto> resolveCategory(List<ProductDto> products, ResponseEntity<String> response) throws JsonProcessingException {
    List<Map<String, Object>> responseList = objectMapper.readValue(response.getBody(), List.class);
    for(int i = 0 ; i< products.size(); i++){
      Map<String, Object> prediction = responseList.get(i);
      String label = (String) prediction.get("label");
      products.get(i).setProductCategory(label);
    }
    return products;
  }

  public List<ProductDto> getProductCategories(List<ProductDto> products) {
    List<String> productNameList = products.stream().map(ProductDto::getProductName).toList();
    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setAccept(List.of(MediaType.APPLICATION_JSON));
      headers.setBearerAuth(huggingFaceToken); // Authorization: Bearer {token}
      Map<String, Object> body = Map.of("inputs", productNameList);
      HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);
      ResponseEntity<String> response = restTemplate.postForEntity(huggingFaceUrl, entity, String.class);
      System.out.println("응답: " + response.getBody());
    } catch (Exception e) {
      System.out.println("API 호출 실패: " + e.getMessage());
    }
    return products;
  }

  public String getBaseUrl() {
    return huggingFaceUrl;
  }
}
