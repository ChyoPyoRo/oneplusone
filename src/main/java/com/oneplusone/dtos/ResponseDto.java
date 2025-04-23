package com.oneplusone.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {//일정한 Repsonse 형식
  private int HttpStatus;
  //여러 데이터 타입으로 가능하게
  //API 문서 수정 문제로 변수명을 data로 수정하지 않고 message로 유지
  private T message;

}
