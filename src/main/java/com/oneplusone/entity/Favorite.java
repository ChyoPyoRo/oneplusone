package com.oneplusone.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Favorite {
  @Id
  @Column(name = "favorite_id")
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID favoriteId;

  @Column(nullable = false, name="user_id")
  private UUID userId;

  @Column(nullable = false, name="product_id")
  private UUID productId;


}
