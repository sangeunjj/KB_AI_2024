package com.kbai.corporatefinance.controller;

import com.kbai.corporatefinance.dto.ChatGPTRequest;
import com.kbai.corporatefinance.dto.ChatGPTResponse;
import com.kbai.corporatefinance.entity.Company1;
import com.kbai.corporatefinance.entity.Question;
import com.kbai.corporatefinance.service.ChatService;
import com.kbai.corporatefinance.service.CompanyService;
import com.kbai.corporatefinance.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bot")
@RequiredArgsConstructor
public class CustomBotController {

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;
    private final ChatService chatService;
    private final QuestionService questionService;
    private final CompanyService companyService;

    @GetMapping("/chat")
    public String chat(String prompt) {
        // ChatService를 이용해 프롬프트 엔지니어링 수행
        ChatGPTRequest request = chatService.createPrompt(prompt);
        return getChatgptResponse(request);
    }

    // 프론트엔드에서 호출하여 생성된 프롬프트를 가져가는 API
    @GetMapping("/alarm-prompt")
    public String getAlarmPrompt() {
        ChatGPTRequest alarmPrompt = chatService.createAlarmPrompt();
        String response = getChatgptResponse(alarmPrompt);

        // 받은 응답을 바탕으로 질문을 생성하여 DB에 저장
        questionService.saveQuestions(response);

        return response;
    }

    // 오늘 생성된 질문 제공
    @GetMapping("/questions/today")
    public List<Question> getTodayQuestions() {
        return questionService.getLatestTwoQuestions();
    }

    private String getChatgptResponse(ChatGPTRequest request) {
        try {
            ChatGPTResponse chatGPTResponse = template.postForObject(apiURL, request, ChatGPTResponse.class); // apiURL 주소로 request 객체를 JSON 형태로 전송
            assert chatGPTResponse != null;
            return chatGPTResponse.getChoices().get(0).getMessage().getContent();
        } catch (HttpClientErrorException.TooManyRequests e) {
            return "현재 요청이 너무 많습니다. 잠시 후 다시 시도해 주세요.";
        } catch (HttpClientErrorException e) {
            return "API 요청 중 오류가 발생했습니다: " + e.getMessage();
        }
    }

    @GetMapping("/generate-report")
    public ResponseEntity<String> generateReport(@RequestParam List<String> companyCodes, @RequestParam List<String> features) {
        // 선택한 기업들의 데이터를 가져옴
        List<Company1> companies = companyService.getCompaniesByCodes(companyCodes);
        // 각 기업에 대해 데이터를 매핑
        List<Map<String, Object>> companyDataList = companies.stream()
                .map(company -> companyService.getCompanyFeatures(company, features))
                .toList();

        // 기업 이름 리스트 생성
        List<String> companyNames = companies.stream()
                .map(Company1::getCompanyName)
                .collect(Collectors.toList());

        // 기업 이름과 데이터로 프롬프트 엔지니어링 수행
        ChatGPTRequest chatGPTRequest = chatService.createReportPrompt(companyNames, features, companyDataList);
        // OpenAI API 호출
        String response = getChatgptResponse(chatGPTRequest);

        return ResponseEntity.ok(response);
    }
}