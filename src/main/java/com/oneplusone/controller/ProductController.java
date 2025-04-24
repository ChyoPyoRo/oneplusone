package com.oneplusone.controller;

import com.oneplusone.dtos.ResponseDto;
import com.oneplusone.entity.Product;
import com.oneplusone.entity.UserInfo;
import com.oneplusone.service.ProductService;
import com.oneplusone.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {
  private final ProductService productService;
  private final UserService userService;

  @DeleteMapping("/favorite/{productId}")
  public ResponseEntity<ResponseDto> removeFavoriteProduct(@PathVariable String productId) {
    log.info("[START][GET] Method name : removeFavoriteProduct");
    //로그인 한 유저의 id 와 product id 의 값을 가지는 좋아요를 취소 가능함
    String userInfo = userService.getCurrentUserId();
    productService.deleteFavoriteProduct(productId, userInfo);
    log.info("[FINISH][GET] Method name : removeFavoriteProduct");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), "즐겨찾기 삭제"));
  }

  @PostMapping("/favorite/{productId}")
  public ResponseEntity<ResponseDto> addFavoriteProduct(@PathVariable String productId) {
    log.info("[START][GET] Method name : addFavoriteProduct");
    String userInfo = userService.getCurrentUserId();
    productService.addFavoriteProduct(productId, userInfo);
    log.info("[FINISH][GET] Method name : addFavoriteProduct");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), "즐겨찾기 등록"));
  }

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

  @GetMapping("/product/convenience")
  public ResponseEntity<ResponseDto> getProductConvenience(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam String convenience) {
    log.info("[START][GET] Method name : getProductConvenience");
    log.info("conv : {}, page : {}, size : {}",convenience, page, size);
    Page<Product> result = productService.getProductList(page, size, convenience);
    log.info("[FINISH][GET] Method : getProductConvenience");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }

  @GetMapping("/product/convenience/{category}")
  public ResponseEntity<ResponseDto> getProductConvenienceByCategory(@PathVariable String category, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size, @RequestParam String convenience) {
    log.info("[START][GET] Method name : getProductConvenienceByCategory");
    log.info("conv : {}, category : {}, page : {}, size : {}",convenience, category, page, size);
    Page<Product> result = productService.getProductListByCategory(category, page, size, convenience);
    log.info("[FINISH][GET] Method : getProductConvenienceByCategory");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));

  }
}
