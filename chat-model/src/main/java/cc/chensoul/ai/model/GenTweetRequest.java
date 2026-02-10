package cc.chensoul.ai.model;

import jakarta.validation.constraints.NotBlank;

public record GenTweetRequest(@NotBlank String prompt, String tone) {
}