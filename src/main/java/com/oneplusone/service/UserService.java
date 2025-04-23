package com.oneplusone.service;

import com.oneplusone.dtos.LoginResponseDto;
import com.oneplusone.dtos.UserPostRequestDto;
import com.oneplusone.entity.UserInfo;
import com.oneplusone.repository.UserRepository;
import com.oneplusone.utils.SimpleTokenUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final SimpleTokenUtil tokenUtil;

  public void createUser(UserPostRequestDto userPostRequestDto) {
    UserInfo user = UserInfo.builder()
        .loginId(userPostRequestDto.getLoginId())
        .password(userPostRequestDto.getPassword())
        .nickname(userPostRequestDto.getNickname())
        .build();
      userRepository.saveUser(user);
  }
  public LoginResponseDto loginUser(String loginId, String password) {
    //유저 로그인 정보 확인
    UserInfo userInfo = userRepository.checkUserInfo(loginId, password);
    if (userInfo == null) return null;
    //token정보 보내기
    return new LoginResponseDto(tokenUtil.encrypt(userInfo.getUserId().toString()));
  }
}
