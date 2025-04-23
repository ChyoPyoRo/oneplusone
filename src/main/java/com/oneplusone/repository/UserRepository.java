package com.oneplusone.repository;

import com.oneplusone.entity.UserInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import static com.oneplusone.entity.QUserInfo.userInfo;

@Repository
@RequiredArgsConstructor
public class UserRepository {
  private final JPAQueryFactory queryFactory;
  private final UserInfoRepository userRepository;

  public void saveUser(UserInfo userData) {
    //QueryDSL 사용하면 너무 복잡하니까 JPA로 간단하게 저장
    userRepository.save(userData);
  }

  public UserInfo checkUserInfo(String loginId, String password) {
    return queryFactory.selectFrom(userInfo)
        .where(userInfo.loginId.eq(loginId),userInfo.password.eq(password))
        .fetchOne();
  }
}
