package cc.chensoul.ai.evaluation;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.ai.chat.evaluation.FactCheckingEvaluator;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
class EvaluationController {

    private final RelevancyEvaluator relevancyEvaluator;
    private final FactCheckingEvaluator factCheckingEvaluator;

    EvaluationController(RelevancyEvaluator relevancyEvaluator,
                         FactCheckingEvaluator factCheckingEvaluator) {
        this.relevancyEvaluator = relevancyEvaluator;
        this.factCheckingEvaluator = factCheckingEvaluator;
    }

    /**
     * 相关性评估：判断模型回复是否与用户问题和给定上下文一致。
     */
    @PostMapping("/api/evaluate/relevancy")
    EvaluateOutput evaluateRelevancy(@RequestBody @Valid EvaluateInput input) {
        List<Document> contextDocs = input.context() == null || input.context().isBlank()
                ? List.of()
                : List.of(new Document(input.context()));
        EvaluationRequest request = new EvaluationRequest(
                input.userText(),
                contextDocs,
                input.responseContent()
        );
        EvaluationResponse response = relevancyEvaluator.evaluate(request);
        return new EvaluateOutput(response.isPass(), response.getFeedback(), response.getMetadata());
    }

    /**
     * 事实核查：判断模型回复中的陈述是否被给定上下文支持（减少幻觉）。
     */
    @PostMapping("/api/evaluate/fact-check")
    EvaluateOutput evaluateFactCheck(@RequestBody @Valid EvaluateInput input) {
        List<Document> contextDocs = input.context() == null || input.context().isBlank()
                ? List.of()
                : List.of(new Document(input.context()));
        // FactCheckingEvaluator: document=context, claim=responseContent
        EvaluationRequest request = new EvaluationRequest(
                input.userText(),
                contextDocs,
                input.responseContent()
        );
        EvaluationResponse response = factCheckingEvaluator.evaluate(request);
        return new EvaluateOutput(response.isPass(), response.getFeedback(), response.getMetadata());
    }

    record EvaluateInput(
            @NotBlank String userText,
            String context,
            @NotBlank String responseContent
    ) {}

    record EvaluateOutput(boolean pass, String feedback, java.util.Map<String, Object> metadata) {}
}
