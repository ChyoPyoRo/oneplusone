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
      }
      catch (Exception e) {
        log.warn("Token validation failed: {}", e.getMessage());

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        String errorMessage = "인증에 실패했습니다";
        if ("Token expired".equals(e.getMessage())) {
          errorMessage = "토큰이 만료되었습니다. 재로그인 해주세요";
        }

        response.getWriter().write("{\"errorMessage\":\"" + errorMessage + "\"}");
        return; // 필터 체인 종료!
      }

    }
    chain.doFilter(request, response);
  }
}