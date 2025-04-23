package com.oneplusone.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPostRequestDto {
  private String loginId;
  private String password;
  private String nickname;
}
