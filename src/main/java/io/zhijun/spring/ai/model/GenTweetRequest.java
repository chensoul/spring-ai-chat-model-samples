package io.zhijun.spring.ai.model;

import jakarta.validation.constraints.NotBlank;

public record GenTweetRequest(@NotBlank String prompt, String tone) {
}