package com.yaksh.mailms;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.mail.username=test@gmail.com",
		"spring.mail.password=test1234",
//		"rabbitmq.queue.email=email-queue"
})
class MailmsApplicationTests {

	@Test
	void contextLoads() {
	}

}
