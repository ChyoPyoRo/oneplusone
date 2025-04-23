package com.oneplusone.repository;

import com.oneplusone.entity.Product;
import com.oneplusone.enums.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.oneplusone.entity.QProduct.product;

@Repository
@RequiredArgsConstructor
public class CrawlingRepository {
  private final ProductRepository productRepository;//DI
  private final JPAQueryFactory queryFactory;
  //crawlingData 저장

  public List<Product> getAllProduct(UUID lastIndex) {
    return queryFactory.selectFrom(product).where(product.category.eq(Category.UNCLASSIFIED)).fetch();
  }

  public void saveProducts(List<Product> products) {
    productRepository.saveAll(products);//save는 queryDSL을 사용하는 것 보다 기존의 jpa를 사용하는게 더 코드가 짧음
  }

  public List<Product> getUnclassifiedProductList() {
    return queryFactory.selectFrom(product).where(product.category.eq(Category.UNCLASSIFIED)).fetch();
  }
}
