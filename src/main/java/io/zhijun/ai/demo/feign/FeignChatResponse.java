package cc.chensoul.ai.demo.feign;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record FeignChatResponse(
        String id,
        List<FeignChoice> choices,
        FeignUsage usage
) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FeignChoice(
            int index,
            FeignChatMessage message,
            @JsonProperty("finish_reason") String finishReason
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record FeignUsage(
            @JsonProperty("prompt_tokens") Integer promptTokens,
            @JsonProperty("completion_tokens") Integer completionTokens,
            @JsonProperty("total_tokens") Integer totalTokens
    ) {}

    public String getFirstContent() {
        if (choices == null || choices.isEmpty()) return null;
        FeignChatMessage msg = choices.get(0).message();
        return msg != null ? msg.content() : null;
    }
}
