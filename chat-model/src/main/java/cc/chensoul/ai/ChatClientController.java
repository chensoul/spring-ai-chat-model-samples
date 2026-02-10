package cc.chensoul.ai;

import cc.chensoul.ai.model.Input;
import cc.chensoul.ai.model.Output;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
class ChatClientController {
    private final ChatClient chatClient;

    ChatClientController(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @PostMapping
    Output chat(@RequestBody @Valid Input input) {
        String response = chatClient.prompt(input.prompt()).call().content();
        return new Output(response);
    }

}