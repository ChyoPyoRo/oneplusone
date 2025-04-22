package com.oneplusone.enums;

import lombok.Getter;

@Getter
public enum ConvenienceName {
  GS25, CU;
  public static ConvenienceName getConvenienceName(String name) {
    try{
      return ConvenienceName.valueOf(name.toUpperCase());
    }catch(IllegalArgumentException e){
      return ConvenienceName.GS25;
    }
  }
}
