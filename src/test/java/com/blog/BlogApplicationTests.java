package com.blog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@EnabledIfEnvironmentVariable(
		named = "SPRING_PROFILES_ACTIVE",
		matches = "local"
)
class BlogApplicationTests {

	@Test
	void contextLoads() {
		// redis 과연 될까
	}

}
