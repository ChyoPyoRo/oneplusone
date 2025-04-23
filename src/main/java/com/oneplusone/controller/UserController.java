package com.oneplusone.controller;

import com.oneplusone.dtos.LoginResponseDto;
import com.oneplusone.dtos.ResponseDto;
import com.oneplusone.dtos.UserPostRequestDto;
import com.oneplusone.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PostMapping("/user")
  public ResponseEntity<?> createUser(@RequestBody UserPostRequestDto userPostRequestDto) {
    log.info("[START][POST] Method name : createUser");
    //null 값 검증
    userService.createUser(userPostRequestDto);
    log.info("[FINISH][POST] Method name : createUser");
    return ResponseEntity.ok(new ResponseDto(HttpStatus.CREATED.value(), "회원 가입"));
  }

  @GetMapping("/user")
  public ResponseEntity<?> loginUser(@RequestParam String loginId, @RequestParam String password) {
    log.info("[START][GET] Method name : loginUser");
    LoginResponseDto result = userService.loginUser(loginId, password);
    log.info("[FINISH][GET] Method name : loginUser");
    //유저가 없을 시 no content의 state를 보내고 싶지만 그러면 body가 비기 때문에 ResponseEntity 객체에는 ok로 하고 body 안에 response status 값을 전달
    if(result == null) return  ResponseEntity.ok(new ResponseDto(HttpStatus.NO_CONTENT.value(), "ID나 비밀번호가 잘못되었거나 존재하지 않습니다."));
    return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.value(), result));
  }

}
