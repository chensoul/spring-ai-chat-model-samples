package cc.chensoul.ai.evaluation;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EvaluationConfig {

    @Bean
    RelevancyEvaluator relevancyEvaluator(ChatClient.Builder chatClientBuilder) {
        return new RelevancyEvaluator(chatClientBuilder);
    }

    @Bean
    FactCheckingEvaluator factCheckingEvaluator(ChatClient.Builder chatClientBuilder) {
        return FactCheckingEvaluator.builder(chatClientBuilder).build();
    }
}
