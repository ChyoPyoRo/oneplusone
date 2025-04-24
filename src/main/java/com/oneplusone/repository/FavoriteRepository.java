package com.oneplusone.repository;

import com.oneplusone.entity.Favorite;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, UUID> {

}
