package cc.chensoul.ai.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TitleSuggestionsRequest(@NotBlank String topic, @NotNull Integer count) {
}
