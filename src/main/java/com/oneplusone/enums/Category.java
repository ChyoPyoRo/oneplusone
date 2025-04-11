package com.oneplusone.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Category {
  DRINK("drink", "LABEL_0"),
  SNACK("snack", "LABEL_1"),
  FOOD("food", "LABEL_2"),
  ICECREAM("icecream", "LABEL_3"),
  NOODLE("noodle", "LABEL_4"),
  ETC("etc", "LABEL_5"),
  ;
  private final String name;
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
