package com.oneplusone.entity;

import com.oneplusone.enums.Category;
import com.oneplusone.enums.ConvenienceName;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)//proxy 객체 생성을 위해서
@AllArgsConstructor(access = AccessLevel.PRIVATE)//실제로 사용 X, Builder 패턴으로 사용
public class Product {

  @Id
  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID productId;

  @Column(name="product_name", nullable=false)
  private String productName;

  @Column(name="product_cost", nullable = false)
  private Long productCost;

  @Enumerated(EnumType.STRING)//Enum타입을 DB에선 String으로 저장
  @Column(name = "convenience_brand", nullable = false)
  private ConvenienceName convenienceBrand;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Category category;

  @Column(nullable = false, name="created_at")
  private LocalDateTime createdAt;


}
