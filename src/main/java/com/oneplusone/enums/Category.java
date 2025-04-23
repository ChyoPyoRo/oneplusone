package com.oneplusone.enums;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

public enum Category {
  SNACK, DRINK, FOOD, ICECREAM, NOODLE, UNCLASSIFIED, ETC;

  private static final Map<String, Category> LABEL_MAP = Map.of(
      "LABEL_0", DRINK,
      "LABEL_1", SNACK,
      "LABEL_2", FOOD,
      "LABEL_3", ICECREAM,
      "LABEL_4", NOODLE,
      "LABEL_5", ETC
  );

  public static Category fromLabel(String label) {
    return LABEL_MAP.getOrDefault(label.toUpperCase(), UNCLASSIFIED);
  }
  public static Category getSameCategory(String inputName) {
    try {
      return Category.valueOf(inputName.toUpperCase());
    } catch (IllegalArgumentException e) {
      return UNCLASSIFIED;
    }
  }
}

