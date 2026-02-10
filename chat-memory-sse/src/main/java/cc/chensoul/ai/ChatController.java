package cc.chensoul.ai;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/")
class ChatController {

    private final ChatClient chatClient;

    ChatController(ChatClient.Builder builder, ChatMemory chatMemory) {
        this.chatClient = builder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new SimpleLoggerAdvisor()
                )
                .build();
    }

    @PostMapping(value = "/api/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    ResponseEntity<SseEmitter> chatStream(@RequestBody @Valid Input input,
                                          @CookieValue(name = "X-CONV-ID", required = false) String convId) {
        String conversationId = convId == null ? UUID.randomUUID().toString() : convId;
        SseEmitter emitter = new SseEmitter(0L);

        Flux<String> contentFlux = this.chatClient.prompt()
                .user(input.prompt())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .stream()
                .content();

        contentFlux.subscribe(
                chunk -> {
                    try {
                        emitter.send(SseEmitter.event().data(chunk));
                    } catch (IOException e) {
                        emitter.completeWithError(e);
                    }
                },
                emitter::completeWithError,
                () -> {
                    try {
                        emitter.complete();
                    } catch (Exception ignored) {
                    }
                }
        );

        ResponseCookie cookie = ResponseCookie.from("X-CONV-ID", conversationId)
                .path("/")
                .maxAge(3600)
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(emitter);
    }

    @PostMapping("/api/chat")
    ResponseEntity<Output> chat(@RequestBody @Valid Input input,
                                @CookieValue(name = "X-CONV-ID", required = false) String convId) {
        String conversationId = convId == null ? UUID.randomUUID().toString() : convId;
        var response = this.chatClient.prompt()
                .user(input.prompt())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call().content();
        ResponseCookie cookie = ResponseCookie.from("X-CONV-ID", conversationId)
                .path("/")
                .maxAge(3600)
                .build();
        Output output = new Output(response);
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(output);
    }

    record Input(@NotBlank String prompt) {}
    record Output(String content) {}
}
