package cc.chensoul.ai;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentReader;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class AiConfig {
    private static final Logger log = LoggerFactory.getLogger(AiConfig.class);

    @Value("classpath:/data/about.md")
    private Resource aboutFile;

    @Value("classpath:/data/career.md")
    private Resource careerLessonsFile;

    /*@Bean
    VectorStore vectorStore(EmbeddingModel embeddingModel) {
       return SimpleVectorStore.builder(embeddingModel).build();
    }*/

    @Bean
    ApplicationRunner applicationRunner(VectorStore vectorStore) {
        return args -> {
            loadDocument(vectorStore, aboutFile);
            loadDocument(vectorStore, careerLessonsFile);
        };
    }

    private void loadDocument(VectorStore vectorStore, Resource resource) {
        log.info("Loading document {} into vector store", resource.getFilename());
         //For loading text docs
        //DocumentReader documentReader = new TextReader(resource);

        //For loading PDF docs
        //DocumentReader documentReader = new PagePdfDocumentReader(resource);

        //For loading PDF, DOC/DOCX, PPT/PPTX, and HTML docs
        //DocumentReader documentReader = new TikaDocumentReader(resource);

        DocumentReader documentReader = new MarkdownDocumentReader(resource, MarkdownDocumentReaderConfig.defaultConfig());

        List<Document> documents = documentReader.get();
        TextSplitter textSplitter = new TokenTextSplitter();
        List<Document> splitDocuments = textSplitter.apply(documents);
        String src = resource.getFilename() != null && resource.getFilename().startsWith("about") ? "about" : "career";
        List<Document> enriched = java.util.stream.IntStream.range(0, splitDocuments.size())
                .mapToObj(i -> {
                    Document d = splitDocuments.get(i);
                    Map<String, Object> meta = new HashMap<>();
                    if (d.getMetadata() != null) {
                        meta.putAll(d.getMetadata());
                    }
                    meta.put("source", src);
                    meta.put("id", src + "-" + i);
                    return new Document(d.getText(), meta);
                })
                .collect(Collectors.toList());
        vectorStore.accept(enriched);
        log.info("Document {} loaded into vector store", resource.getFilename());
    }

}
