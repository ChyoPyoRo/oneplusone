package com.oneplusone.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
  ONE_TO_ONE("oneToOne", "ONE_TO_ONE"), TWO_TO_ONE("twoToOne", "TWO_TO_ONE");
  private final String DataBaseName;//DB에 저장될 이름
  private final String HTMLName;//HTML상에서 요소에 적혀있는 이름
}
