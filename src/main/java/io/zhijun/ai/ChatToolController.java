package io.zhijun.ai;

import cc.chensoul.ai.model.Input;
import cc.chensoul.ai.model.Output;
import io.zhijun.ai.tool.DateTimeTools;
import io.zhijun.ai.tool.EmployeeTools;
import jakarta.validation.Valid;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat/tool")
class ChatToolController {
    private final ChatClient chatClient;

    ChatToolController(ChatClient.Builder builder, EmployeeTools employeeTools) {
        this.chatClient = builder
                .defaultSystem("""
                        You are a helpful assistant for our company.
                        You always respond based on the data you have from tools available to you.
                        If you don't know the answer, you will respond with "I don't know".
                        """)
                .defaultTools(employeeTools, new DateTimeTools())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    @PostMapping
    Output chat(@RequestBody @Valid Input input) {
        String response = chatClient
                .prompt(input.prompt()).call().content();
        return new Output(response);
    }

}