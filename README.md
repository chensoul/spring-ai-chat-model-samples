# Spring AI Samples

基于 [Spring AI](https://spring.io/projects/spring-ai) 的示例项目，涵盖对话、提示词模板、结构化输出、对话记忆、RAG、工具调用与 MCP 等能力。各模块可独立运行。

## 环境要求

- **Java 21**
- **Maven 3.9+**（或使用仓库根目录的 `./mvnw`）
- 部分模块需配置 **API Key** 或本地服务：
  - **DeepSeek**：多数模块默认使用，需设置 `DEEPSEEK_API_KEY`
- **Redis**：06-chat-memory、08-rag-vector-store
  - **PostgreSQL**：06-chat-memory

## 许可证

见 [LICENSE](LICENSE)。
