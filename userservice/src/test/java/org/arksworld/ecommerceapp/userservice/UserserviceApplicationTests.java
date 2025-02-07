package org.arksworld.ecommerceapp.userservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"eureka.client.enabled=false",
		"eureka.instance.enabled=false"
})
class UserserviceApplicationTests {

	@Test
	void contextLoads() {
	}

}
