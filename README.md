# Spring AI Chat Model Samples

This project demonstrates the integration of AI capabilities (chat、prompt template、tool calling) within a Spring Boot application, utilizing the Spring AI framework.


## Articles

- 开始使用 Spring AI聊天模型，并轻松切换不同的 AI 提供商，包括 OpenAI、Anthropic 和 Ollama。详细指南请参阅以下文章：

## Architecture

Currently, there are four @RestControllers that show Spring AI features:

- ChatClientController
- ChatPromptController
- ChatConverterController
- ChatToolController

## Running the Application

Follow these steps to run the application locally.

```
git clone https://github.com/chensoul/spring-ai-chat-model-samples.git
cd spring-ai-chat-model-samples
```

By default, this sample Spring AI app connects to OpenAI. So, before running the app you must set a token:

```
export OPENAI_API_KEY=<YOUR_API_TOKEN>
mvn spring-boot:run
```

To enable integration with Anthropic, we should activate the `anthropici` profile:

```
export ANTHROPIC_API_KEY=<YOUR_API_TOKEN>
mvn spring-boot:run -Panthropici
```

To enable integration with Ollama, we should activate the `ollama` profile:

```
mvn spring-boot:run -Pollama
```

Before that, we must run the model on Ollama, e.g.:

```
ollama run qwen3:8b
```