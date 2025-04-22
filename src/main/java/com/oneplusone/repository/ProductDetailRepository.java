package com.oneplusone.repository;

import static com.oneplusone.entity.QProduct.product;

import com.oneplusone.entity.Product;
import com.oneplusone.enums.Category;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductDetailRepository {
  private final ProductRepository productRepository;//DI
  private final JPAQueryFactory queryFactory;


  public Page<Product> findProductUsingPage(Pageable pageable) {
    //Pageable 클래스의 정보를 기준으로 데이터를 가져옴
    List<Product> content = queryFactory
        .selectFrom(product)
        .orderBy(product.productName.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    //총 갯수를 가져오는 쿼리 문
    long total = queryFactory
        .select(product.count())
        .from(product)
        .fetchOne();

    return new PageImpl<>(content, pageable, total);
  }

  public Page<Product> findProductByCategoryUsingPage(Pageable pageable, String category) {
    List<Product> content = queryFactory
        .selectFrom(product)
        .where(product.category.eq(Category.getSameCategory(category)))
        .orderBy(product.productName.asc())
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
    long total = queryFactory
        .select(product.count())
        .from(product)
        .fetchOne();
    return new PageImpl<>(content, pageable, total);
  }
}
