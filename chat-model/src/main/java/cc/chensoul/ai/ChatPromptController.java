package cc.chensoul.ai;

import cc.chensoul.ai.model.GenTweetRequest;
import cc.chensoul.ai.model.Input;
import cc.chensoul.ai.model.Output;
import cc.chensoul.ai.model.TitleSuggestionsRequest;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("/api/chat/prompt")
class ChatPromptController {
    private final ChatClient chatClient;
    private final Resource tweetSystemMsgResource;

    ChatPromptController(ChatClient.Builder builder,
                         @Value("classpath:/prompts/tweet-system-message.st")
                         Resource tweetSystemMsgResource) {
        this.chatClient = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
        this.tweetSystemMsgResource = tweetSystemMsgResource;
    }

    @PostMapping
    Output chat(@RequestBody @Valid Input input) {
        // 简单聊天：仅用户消息
        // String response = chatClient.prompt(input.prompt()).call().content();

        // 带系统消息的聊天：指定助手角色与风格
        String systemPrompt = "You are a friendly, helpful assistant. You always respond professionally.";
        SystemMessage systemMessage = new SystemMessage(systemPrompt);
        UserMessage userMessage = new UserMessage(input.prompt());
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        String response = chatClient.prompt(prompt).call().content();

        return new Output(response);
    }

    @PostMapping("/suggest-titles")
    Output suggestTitles(@RequestBody @Valid TitleSuggestionsRequest req) {
        String response;

        PromptTemplate pt = new PromptTemplate("""
                I would like to give a presentation about the following:
                
                {topic}
                
                Give me {count} title suggestions for this topic.
                
                Make sure the title is relevant to the topic and it should be a single short sentence.
                """);

        Map<String, Object> vars = Map.of("topic", req.topic(), "count", req.count());
        Message message = pt.createMessage(vars);
        response = chatClient.prompt().messages(message).call().content();

        return new Output(response);
    }

    @PostMapping("/gen-tweet")
    Output generateTweet(@RequestBody @Valid GenTweetRequest request) throws IOException {
        String templateContent = tweetSystemMsgResource.getContentAsString(UTF_8);
        String tone = request.tone() != null ? request.tone() : "professional";
        String systemPrompt = new PromptTemplate(templateContent)
                .createMessage(Map.of("tone", tone))
                .getText();
        SystemMessage systemMessage = new SystemMessage(systemPrompt);
        UserMessage userMessage = new UserMessage(request.prompt());

        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        String response = chatClient.prompt(prompt).call().content();

        return new Output(response);
    }
}