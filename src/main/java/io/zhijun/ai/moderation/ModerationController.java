//package cc.chensoul.ai.moderation;
//
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotBlank;
//import org.springframework.ai.moderation.Categories;
//import org.springframework.ai.moderation.CategoryScores;
//import org.springframework.ai.moderation.Moderation;
//import org.springframework.ai.moderation.ModerationModel;
//import org.springframework.ai.moderation.ModerationPrompt;
//import org.springframework.ai.moderation.ModerationResponse;
//import org.springframework.ai.moderation.ModerationResult;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.ArrayList;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/")
//class ModerationController {
//
//    private final ModerationModel moderationModel;
//
//    ModerationController(ModerationModel moderationModel) {
//        this.moderationModel = moderationModel;
//    }
//
//    @PostMapping("/api/moderate")
//    ModerateOutput moderate(@RequestBody @Valid ModerateInput input) {
//        ModerationPrompt prompt = new ModerationPrompt(input.text());
//        ModerationResponse response = moderationModel.call(prompt);
//        Moderation moderation = response.getResult().getOutput();
//
//        boolean flagged = false;
//        List<Map<String, Object>> results = new ArrayList<>();
//        for (ModerationResult result : moderation.getResults()) {
//            flagged = flagged || result.isFlagged();
//            Map<String, Object> item = new LinkedHashMap<>();
//            item.put("flagged", result.isFlagged());
//            item.put("categories", categoriesMap(result.getCategories()));
//            item.put("categoryScores", categoryScoresMap(result.getCategoryScores()));
//            results.add(item);
//        }
//
//        return new ModerateOutput(
//                moderation.getId(),
//                moderation.getModel(),
//                flagged,
//                results
//        );
//    }
//
//    private static Map<String, Boolean> categoriesMap(Categories c) {
//        Map<String, Boolean> m = new LinkedHashMap<>();
//        m.put("sexual", c.isSexual());
//        m.put("hate", c.isHate());
//        m.put("harassment", c.isHarassment());
//        m.put("selfHarm", c.isSelfHarm());
//        m.put("sexualMinors", c.isSexualMinors());
//        m.put("hateThreatening", c.isHateThreatening());
//        m.put("violenceGraphic", c.isViolenceGraphic());
//        m.put("selfHarmIntent", c.isSelfHarmIntent());
//        m.put("selfHarmInstructions", c.isSelfHarmInstructions());
//        m.put("harassmentThreatening", c.isHarassmentThreatening());
//        m.put("violence", c.isViolence());
//        return m;
//    }
//
//    private static Map<String, Double> categoryScoresMap(CategoryScores s) {
//        Map<String, Double> m = new LinkedHashMap<>();
//        m.put("sexual", s.getSexual());
//        m.put("hate", s.getHate());
//        m.put("harassment", s.getHarassment());
//        m.put("selfHarm", s.getSelfHarm());
//        m.put("sexualMinors", s.getSexualMinors());
//        m.put("hateThreatening", s.getHateThreatening());
//        m.put("violenceGraphic", s.getViolenceGraphic());
//        m.put("selfHarmIntent", s.getSelfHarmIntent());
//        m.put("selfHarmInstructions", s.getSelfHarmInstructions());
//        m.put("harassmentThreatening", s.getHarassmentThreatening());
//        m.put("violence", s.getViolence());
//        return m;
//    }
//
//    record ModerateInput(@NotBlank String text) {}
//    record ModerateOutput(String id, String model, boolean flagged, List<Map<String, Object>> results) {}
//}
