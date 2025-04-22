package com.oneplusone.controller;

import com.oneplusone.dtos.ResponseDto;
import com.oneplusone.entity.Product;
import com.oneplusone.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {
  private final ProductService productService;

  @GetMapping("/product")
  public ResponseEntity<ResponseDto> getProduct(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
    log.info("[START][GET] Method name : getProduct ");
    log.info("page : {}, size : {}", page, size);
    Page<Product> result = productService.getProductList(page, size);
    log.info("[FINISH][GET] Method : getProduct");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }

  @GetMapping("/product/{category}")
  public ResponseEntity<ResponseDto> getProductByCategory(@PathVariable String category, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
    log.info("[START][GET] Method name : getProductByCategory");
    log.info("category : {}", category);
    Page<Product> result = productService.getProductListByCategory(category, page, size);
    log.info("[FINISH][GET] Method name : getProductByCategory");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }
}
