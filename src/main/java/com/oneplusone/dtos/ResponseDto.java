package com.oneplusone.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {//일정한 Repsonse 형식
  private int HttpStatus;
  private T message; //여러 데이터 타입으로 가능하게

}
