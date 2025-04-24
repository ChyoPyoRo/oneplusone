package com.oneplusone.dtos;

import com.oneplusone.enums.Category;
import com.oneplusone.enums.ConvenienceName;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductItemResponseDto {
  //Product 정보를 동일하게 유지하는 dto
  private UUID productId;
  private String productName;
  private Long productCost;
  private ConvenienceName convenienceBrand;
  private Category category;
  private Boolean isfavorite;

}
