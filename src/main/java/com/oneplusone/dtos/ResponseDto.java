package com.oneplusone.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto {//일정한 Repsonse 형식
  private int HttpStatus;
  private String message;

}
