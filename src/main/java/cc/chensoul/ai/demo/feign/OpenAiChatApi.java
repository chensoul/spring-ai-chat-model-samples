package cc.chensoul.ai.demo.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 使用 Feign 声明式调用 OpenAI 兼容的 chat completions 接口（支持千问等）。
 */
@FeignClient(
        name = "openai-chat",
        url = "${app.feign-openai.base-url:https://api.openai.com/v1}",
        configuration = FeignOpenAiConfig.class
)
public interface OpenAiChatApi {

    @PostMapping("/chat/completions")
    FeignChatResponse chatCompletions(@RequestBody FeignChatRequest request);
}
