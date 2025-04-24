package com.oneplusone.utils;

import com.oneplusone.enums.errors.CustomException;
import com.oneplusone.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenAuthenticationFilter  extends OncePerRequestFilter {

  private final TokenUtil tokenUtil;
  private final UserService userService;

  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader("Authorization");
    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7); // "Bearer " 제거
      try {
        String userId = tokenUtil.decrypt(token);
        UserDetails userDetails = userService.loadUserByUserId(userId);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      } catch (Exception e) {
        // 토큰이 유효하지 않거나 복호화 실패한 경우 무시 (인증되지 않음)
        log.warn("Token authentication failed", e);
        throw new CustomException("Unauthorized");
      }
    }
    chain.doFilter(request, response);
  }
}