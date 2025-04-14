package com.oneplusone.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
  DRINK( "LABEL_0"),
  SNACK( "LABEL_1"),
  FOOD( "LABEL_2"),
  ICECREAM("LABEL_3"),
  NOODLE( "LABEL_4"),
  ETC( "LABEL_5"),
  UNCLASSIFIED( "NONE")
  ;
  private final String label;
  public static Category getCategoryByLabel(String label) {
    for (Category category : Category.values()) {
      if (category.getLabel().equals(label)) {
        return category;
      }
    }
    return null;//잘못된 label 값 전달
  }
}
