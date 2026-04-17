package cc.chensoul.ai.demo.feign;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FeignChatRequest(
        String model,
        List<FeignChatMessage> messages,
        Double temperature,
        Integer max_tokens
) {}
