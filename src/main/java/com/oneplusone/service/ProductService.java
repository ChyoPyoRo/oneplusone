package com.oneplusone.service;

import com.oneplusone.entity.Product;
import com.oneplusone.repository.ProductDetailRepository;
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

  public Page<Product> getProductListByCategory(String category, int page, int size) {
    Pageable pageable = PageRequest.of(page-1, size);
    return productDetailRepository.findProductByCategoryUsingPage(pageable, category);
  }
}
