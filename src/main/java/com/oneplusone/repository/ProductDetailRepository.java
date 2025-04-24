package com.oneplusone.repository;

import static com.oneplusone.entity.QProduct.product;
import static com.oneplusone.entity.QFavorite.favorite;

import com.oneplusone.entity.Favorite;
import com.oneplusone.entity.Product;
import com.oneplusone.entity.UserInfo;
import com.oneplusone.enums.Category;
import com.oneplusone.enums.ConvenienceName;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductDetailRepository {
  private final ProductRepository productRepository;//DI
  private final FavoriteRepository favoriteRepository;
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

  public Page<Product> findProductUsingPage(Pageable pageable, String convenience) {
    List<Product> content = queryFactory
        .selectFrom(product)
        .where(product.convenienceBrand.eq(ConvenienceName.getConvenienceName(convenience)))
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

  public Page<Product> findProductByCategoryUsingPage(Pageable pageable, String category, String convenience) {
    List<Product> content = queryFactory
        .selectFrom(product)
        .where(
            product.category.eq(Category.getSameCategory(category)),
            product.convenienceBrand.eq(ConvenienceName.getConvenienceName(convenience)))
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

  public void addFavoriteProduct(Favorite favorite) {
    favoriteRepository.save(favorite);
  }

  public Long deleteFavoriteProduct(UUID inputProductId, UUID inputUserId) {
    return queryFactory.delete(favorite).where(favorite.productId.eq(inputProductId), favorite.userId.eq(inputUserId)).execute();
  }

  public boolean findFavoriteProduct(UUID inputProductId, UUID inputUserId) {
    //존재하면 true 없으면 false
    return queryFactory.selectOne()
        .from(favorite)
        .where(favorite.productId.eq(inputProductId), favorite.userId.eq(inputUserId))
        .fetchFirst() != null;
  }
}
