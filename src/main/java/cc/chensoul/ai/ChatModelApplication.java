package cc.chensoul.ai;

import cc.chensoul.ai.demo.feign.OpenAiChatApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackageClasses = OpenAiChatApi.class)
public class ChatModelApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatModelApplication.class, args);
	}

}
