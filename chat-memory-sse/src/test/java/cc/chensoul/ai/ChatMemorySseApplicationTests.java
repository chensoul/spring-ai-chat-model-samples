package cc.chensoul.ai;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {
        "spring.ai.openai.api-key=test",
        "spring.ai.chat.memory.repository.jdbc.initialize-schema=never"
})
@SpringBootTest
class ChatMemorySseApplicationTests {

    @Test
    void contextLoads() {
    }
}
