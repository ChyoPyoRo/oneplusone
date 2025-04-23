package com.oneplusone.repository;

import com.oneplusone.entity.UserInfo;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, UUID> {

}
