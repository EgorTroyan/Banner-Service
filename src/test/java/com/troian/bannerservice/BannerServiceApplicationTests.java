package com.troian.bannerservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "/application.yml")

class BannerServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
