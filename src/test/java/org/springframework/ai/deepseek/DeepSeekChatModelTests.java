package org.springframework.ai.deepseek;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.deepseek.api.DeepSeekApi;
import org.springframework.ai.deepseek.api.DeepSeekApi.ChatCompletion;
import org.springframework.ai.deepseek.api.DeepSeekApi.ChatCompletionMessage;
import org.springframework.ai.deepseek.api.DeepSeekApi.ChatCompletionRequest;
import org.springframework.ai.retry.RetryUtils;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * Tests for {@link DeepSeekChatModel}.
 */
@ExtendWith(MockitoExtension.class)
public class DeepSeekChatModelTests {

	@Mock
	private DeepSeekApi deepSeekApi;

	private DeepSeekChatModel chatModel;

	@BeforeEach
	void setUp() {
		this.chatModel = new DeepSeekChatModel(this.deepSeekApi,
				DeepSeekChatOptions.builder().model("deepseek-chat").build(),
                org.springframework.ai.model.tool.ToolCallingManager.builder().build(),
				RetryTemplate.builder().build(),
				io.micrometer.observation.ObservationRegistry.create());
	}

	@Test
	void deepSeekAssistantMessageReasoningContentTest() {
		// Prepare a DeepSeekAssistantMessage with reasoning content
		String reasoningContent = "I am thinking...";
		String content = "Hello!";
		DeepSeekAssistantMessage assistantMessage = new DeepSeekAssistantMessage.Builder()
				.content(content)
				.reasoningContent(reasoningContent)
				.build();

		// Create a prompt with this message
        // We simulate a conversation history where the assistant previously replied with reasoning
		Prompt prompt = new Prompt(List.of(new UserMessage("Hi"), assistantMessage, new UserMessage("Follow up")));

		// Mock the API response
		ChatCompletionMessage responseMessage = new ChatCompletionMessage("Response", ChatCompletionMessage.Role.ASSISTANT);
		ChatCompletion.Choice choice = new ChatCompletion.Choice(null, 0, responseMessage, null);
		ChatCompletion completion = new ChatCompletion("id", List.of(choice), 123L, "model", null, null, new DeepSeekApi.Usage(10, 10, 20));
		given(this.deepSeekApi.chatCompletionEntity(any(ChatCompletionRequest.class)))
				.willReturn(ResponseEntity.ok(completion));

		// Execute call
		this.chatModel.call(prompt);

		// Verify the request sent to the API contains the reasoning content
		ArgumentCaptor<ChatCompletionRequest> requestCaptor = ArgumentCaptor.forClass(ChatCompletionRequest.class);
		verify(this.deepSeekApi).chatCompletionEntity(requestCaptor.capture());

		ChatCompletionRequest request = requestCaptor.getValue();
		List<ChatCompletionMessage> messages = request.messages();

		// The second message should be the assistant message
		assertThat(messages).hasSize(3);
		ChatCompletionMessage sentAssistantMessage = messages.get(1);
		
		assertThat(sentAssistantMessage.role()).isEqualTo(ChatCompletionMessage.Role.ASSISTANT);
		assertThat(sentAssistantMessage.content()).isEqualTo(content);
        // Verify that reasoning_content was passed
		assertThat(sentAssistantMessage.reasoningContent()).isEqualTo(reasoningContent);
	}
}
