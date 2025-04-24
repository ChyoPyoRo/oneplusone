package com.oneplusone.service;

import com.oneplusone.dtos.ResponseDto;
import com.oneplusone.entity.Favorite;
import com.oneplusone.entity.Product;
import com.oneplusone.entity.UserInfo;
import com.oneplusone.enums.errors.CustomException;
import com.oneplusone.repository.ProductDetailRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ProductService {
  private final ProductDetailRepository productDetailRepository;

  public Page<Product> getProductList(int page, int size) {
    Pageable pageable = PageRequest.of(page-1, size);
    return productDetailRepository.findProductUsingPage(pageable);
  }
  public Page<Product> getProductList(int page, int size, String convenience) {
    Pageable pageable = PageRequest.of(page-1, size);
    return productDetailRepository.findProductUsingPage(pageable, convenience);
  }

  public Page<Product> getProductListByCategory(String category, int page, int size) {
    Pageable pageable = PageRequest.of(page-1, size);
    return productDetailRepository.findProductByCategoryUsingPage(pageable, category);
  }

  public Page<Product> getProductListByCategory(String category, int page, int size, String convenience) {
    Pageable pageable = PageRequest.of(page-1, size);
    return productDetailRepository.findProductByCategoryUsingPage(pageable, category, convenience);
  }

  public void addFavoriteProduct(String productId, String currentUserId) {
    //존재하는 그거인지 확인
    if(productDetailRepository.findFavoriteProduct(UUID.fromString(productId),UUID.fromString(currentUserId))) throw new CustomException("이미 추가된 즐겨찾기 입니다.");
    //Favorite 타입 생성
    Favorite favorite = Favorite.builder().productId(UUID.fromString(productId)).userId(UUID.fromString(currentUserId)).build();
    productDetailRepository.addFavoriteProduct(favorite);
  }

  public void deleteFavoriteProduct(String productId, String currentUserId) {
    Long deleteResult = productDetailRepository.deleteFavoriteProduct(UUID.fromString(productId), UUID.fromString(currentUserId));
    if(deleteResult==0) throw new CustomException("이미 삭제되었거나 존재하지 않는 즐겨찾기입니다.");
  }
}
