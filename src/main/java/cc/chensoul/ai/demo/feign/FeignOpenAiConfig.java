package cc.chensoul.ai.demo.feign;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignOpenAiConfig {

    @Bean
    public RequestInterceptor openAiAuthInterceptor(
            @Value("${app.feign-openai.api-key:}") String apiKey) {
        return template -> template.header("Authorization", "Bearer " + apiKey);
    }
}
