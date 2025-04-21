package com.oneplusone;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oneplusone.crawling.Gs25Crawling;
import com.oneplusone.crawling.ProductCategoryResolver;
import com.oneplusone.dtos.ProductDto;
import com.oneplusone.enums.Category;
import com.oneplusone.enums.EventType;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;


class OneplusoneApplicationTests {
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

	void openWebsite() throws InterruptedException {
	}

	void enumCheck(){
//		ProductCategoryResolver.getProductCategories(new ArrayList());
	}


}
