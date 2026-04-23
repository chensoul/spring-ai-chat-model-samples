package io.zhijun.spring.ai.model;

import java.util.List;

public record Tweet(String content, List<String> hashtags) {
}
