package com.oneplusone.controller;

import com.oneplusone.dtos.ResponseDto;
import com.oneplusone.service.CrawlingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CrawlingController {
  private final CrawlingService crawlingService;

  //실제 크롤링은 달마다 진행되지만 서버 최초 실행시에 데이터를 업데이트 하기 위한 api
  @PostMapping("/crawling")
  public ResponseEntity<?> crawling() throws Exception {
    log.info("[POST] Method : crawling");
    crawlingService.crawlingProductsAndUpdate();
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), "크롤링 완료"));
  }

  //category update가 에러 발생시에 update만 추가적으로 요청하는 api
  @PostMapping("/category")
  public ResponseEntity<?> updateCategory(){
    log.info("[POST] Method : category");
    crawlingService.updateCategory();
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), "카테고리 업데이트 완료"));
  }

}
