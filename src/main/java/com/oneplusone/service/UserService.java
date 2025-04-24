package com.oneplusone.service;

import com.oneplusone.dtos.LoginResponseDto;
import com.oneplusone.dtos.UserPostRequestDto;
import com.oneplusone.entity.UserInfo;
import com.oneplusone.enums.RoleType;
import com.oneplusone.enums.errors.CustomException;
import com.oneplusone.repository.UserRepository;
import com.oneplusone.utils.TokenUtil;
import jakarta.transaction.Transactional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final TokenUtil tokenUtil;

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

  public UserInfo getUserInfo(String token)  {
    String userId = tokenUtil.decrypt(token);//시간 초과시 자동으로 에러
    UserInfo userinfo = userRepository.getUserByUserId(UUID.fromString(userId));
    if(userinfo == null) throw new CustomException("해당 유저는 존재하지 않습니다");
    return userinfo;
  }

  //spring security를 활용한 인증을 위한 메서드
  public UserDetails loadUserByUserId(String userId)throws UsernameNotFoundException {
    UserInfo userInfo = userRepository.getUserByUserId(UUID.fromString(userId));
    if(userInfo == null) throw new CustomException("해당 유저는 존재하지 않습니다");
    return org.springframework.security.core.userdetails.User.builder()
        .username(userInfo.getUserId().toString())//로그인 유저 id
        .password(userInfo.getPassword())
        .roles(RoleType.USERS.name())
        .build();
  }
  public String getCurrentUserId(){
    //test에서 확인한 현재 로그인 유저 확인
    Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
    UserDetails authenticatedUser = (UserDetails) contextAuth.getPrincipal();
    return authenticatedUser.getUsername();
  }
}
