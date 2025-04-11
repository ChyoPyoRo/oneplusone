package com.oneplusone.dtos;

import com.oneplusone.enums.Category;
import com.oneplusone.enums.ConvenienceName;
import com.oneplusone.enums.EventType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductDto {
  private String productName;
  private Long productPrice;
  private Category productCategory;
  //enum type으로 저장하는 것보다 String으로 저장하는 것이 크기가 더 작으니까 효율적일 것이라고 생각
  private String eventType;
  private String convenienceName;

  public ProductDto(String productName, Long productPrice, EventType eventType, ConvenienceName convenienceName) {
    this.productName = productName;
    this.productPrice = productPrice;
    this.eventType = eventType.getDataBaseName();
    this.convenienceName = convenienceName.toString();
  }

  public void setProductCategory(String categoryLabel) {
    this.productCategory = Category.getCategoryByLabel(categoryLabel);
  }
}
