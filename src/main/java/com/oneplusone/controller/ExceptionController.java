package com.oneplusone.controller;

import com.oneplusone.dtos.ErrorResponseDto;
import java.sql.SQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class ExceptionController {
  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponseDto> handleUserCreateException(DataIntegrityViolationException ex) {
    log.error("DataIntegrityViolationException");
    log.error(ex.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto("중복된 정보가 있습니다."));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseDto> handleUserCreateException(Exception ex) {
    //러프하게 프론트 측에도 정보 제공
    //프론트가 서버랑 통신하다가 에러 발생 시 어떤 에러가 발생했었는지 바로 공유가 가능하도록
    log.error(ex.getClass().getSimpleName());//디버그용 로그
    log.error(ex.getMessage());
    log.error(ex.getCause().getMessage());
    return ResponseEntity.internalServerError().body(new ErrorResponseDto(ex.getMessage()));
  }
}
