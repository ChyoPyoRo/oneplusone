package com.oneplusone;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplusone.crawling.Gs25Crawling;
import com.oneplusone.crawling.ProductCategoryResolver;
import com.oneplusone.dtos.LoginResponseDto;
import com.oneplusone.dtos.ProductDto;
import com.oneplusone.dtos.UserPostRequestDto;
import com.oneplusone.enums.Category;
import com.oneplusone.enums.EventType;
import com.oneplusone.service.UserService;
import com.oneplusone.utils.TokenUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class OneplusoneApplicationTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper objectMapper;
  @Autowired
  private UserService userService;
	@Autowired
	private TokenUtil tokenUtil;

	@Test//user 생성
	void createUser() throws Exception {
		// 1. 사용자 등록
		UserPostRequestDto registerDto = new UserPostRequestDto("testuser", "testpass", "nickname");
		userService.createUser(registerDto);
	}
	@Test//user 로그인 확인
	void checkToken() throws Exception {
		LoginResponseDto result = userService.loginUser("testuser", "testpass");
		assertThat(result).isNotNull();
		// 토큰에서 userId 추출
		String decryptedUserId = tokenUtil.decrypt(result.getToken());

		// userId로 UserDetails 로드
		UserDetails userDetails = userService.loadUserByUserId(decryptedUserId);

		// SecurityContext에 수동으로 인증 객체 설정
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Authentication contextAuth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails authenticatedUser = (UserDetails) contextAuth.getPrincipal();
		String userId = authenticatedUser.getUsername();

		assertThat(userId).isEqualTo(decryptedUserId);
		System.out.println("SecurityContext 인증된 userId: " + userId);
	}
	@Test
	void updateCategory() throws JsonProcessingException {
		List<ProductDto> products = List.of(
				new ProductDto("신라면", 1000L, null, "2+1", "GS25"),
				new ProductDto("초코파이", 1200L, null, "2+1", "GS25"),
				new ProductDto("도시락", 4500L, null, "2+1", "GS25")
		);
		String mockJson = """
        [
          {"label":"LABEL_4","score":0.9987415671348572},
          {"label":"LABEL_1","score":0.9997038245201111},
          {"label":"LABEL_2","score":0.9998621940612793}
        ]
        """;
		ResponseEntity<String> mockResponse = new ResponseEntity<>(mockJson, HttpStatus.OK);
		ProductCategoryResolver resolver = new ProductCategoryResolver(new RestTemplate(), new ObjectMapper());
	}

}
