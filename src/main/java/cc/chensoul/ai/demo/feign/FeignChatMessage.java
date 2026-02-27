package cc.chensoul.ai.demo.feign;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 单条消息，兼容 OpenAI / 千问 chat completions 格式。
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record FeignChatMessage(String role, String content) {

    public static FeignChatMessage user(String content) {
        return new FeignChatMessage("user", content);
    }

    public static FeignChatMessage system(String content) {
        return new FeignChatMessage("system", content);
    }

    public static FeignChatMessage assistant(String content) {
        return new FeignChatMessage("assistant", content);
    }
}
