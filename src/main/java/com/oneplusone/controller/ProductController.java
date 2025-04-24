package com.oneplusone.controller;

import com.oneplusone.dtos.ProductItemResponseDto;
import com.oneplusone.dtos.ResponseDto;
import com.oneplusone.entity.Product;
import com.oneplusone.service.ProductService;
import com.oneplusone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProductController {
  private final ProductService productService;
  private final UserService userService;

  //검색
  //로그인 유저만 가능
  @GetMapping("/me/search")
  public ResponseEntity<ResponseDto> searchProductWithName(@RequestParam String keyword, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
    log.info("[START][GET] Method name : searchProductWithName");
    String userId = userService.getCurrentUserId();
    Page<ProductItemResponseDto> result = productService.searchProductUsingName(keyword, userId, page, size);
    log.info("[FINISH][GET] Method name : getFavoriteList");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }

  @GetMapping("/me/product")
  public ResponseEntity<ResponseDto> getFavoriteList(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
    log.info("[START][GET] Method name : getFavoriteList");
    //로그인 한 유저의 경우 해당 api로 요청
    String userId = userService.getCurrentUserId();
    Page<ProductItemResponseDto> result =  productService.getLoginProductLIst(userId, page, size);
    log.info("[FINISH][GET] Method name : getFavoriteList");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }

  @GetMapping("/me/product/{category}")
  public ResponseEntity<ResponseDto> getFavoriteListAboutCategory(@PathVariable String category,@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
    log.info("[START][GET] Method name : getFavoriteListAboutCategory");
    log.info("category : {}", category);
    //로그인 한 유저의 경우 해당 api로 요청
    String userId = userService.getCurrentUserId();
    Page<ProductItemResponseDto> result =  productService.getLoginUserProductListCategory(userId, page, size, category);
    log.info("[FINISH][GET] Method name : getFavoriteListAboutCategory");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }

  @GetMapping("/me/product/convenience")
  public ResponseEntity<ResponseDto> getProductConvenienceAboutLoginUser(@RequestParam String convenience,@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size) {
    log.info("[START][GET] Method name : getProductConvenienceAboutLoginUser");
    //로그인 한 유저의 경우 해당 api로 요청
    String userId = userService.getCurrentUserId();
    Page<ProductItemResponseDto> result =  productService.getLoginUserProductListConvenience(userId, page, size, convenience);
    log.info("[FINISH][GET] Method name : getProductConvenienceAboutLoginUser");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }

  @GetMapping("/me/product/convenience/{category}")
  public ResponseEntity<ResponseDto> getProductConvenienceAboutLoginUserByCategory(@PathVariable String category,@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size,@RequestParam String convenience) {
    log.info("[START][GET] Method name : getProductConvenienceAboutLoginUserByCategory");
    //로그인 한 유저의 경우 해당 api로 요청
    String userId = userService.getCurrentUserId();
    Page<ProductItemResponseDto> result =  productService.loginUserProductListConvenienceCategory(userId, page, size, category, convenience);
    log.info("[FINISH][GET] Method name : getProductConvenienceAboutLoginUserByCategory");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }

  @DeleteMapping("/favorite/{productId}")
  public ResponseEntity<ResponseDto> removeFavoriteProduct(@PathVariable String productId) {
    log.info("[START][DELETE] Method name : removeFavoriteProduct");
    //로그인 한 유저의 id 와 product id 의 값을 가지는 좋아요를 취소 가능함
    String userInfo = userService.getCurrentUserId();
    productService.deleteFavoriteProduct(productId, userInfo);
    log.info("[FINISH][DELETE] Method name : removeFavoriteProduct");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), "즐겨찾기 삭제"));
  }

  @PostMapping("/favorite/{productId}")
  public ResponseEntity<ResponseDto> addFavoriteProduct(@PathVariable String productId) {
    log.info("[START][POST] Method name : addFavoriteProduct");
    String userInfo = userService.getCurrentUserId();
    productService.addFavoriteProduct(productId, userInfo);
    log.info("[FINISH][POST] Method name : addFavoriteProduct");
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
