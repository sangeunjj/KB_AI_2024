package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.dto.ChatGPTRequest;
import com.kbai.corporatefinance.dto.ChatGPTResponse;
import com.kbai.corporatefinance.entity.Question;
import com.kbai.corporatefinance.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;
    private final QuestionRepository questionRepository;
    private ChatService chatService; // 생성자 주입 대신 필드 주입으로 변경

    // 새로운 질문 생성 및 저장
    public void generateQuestions() {
        ChatGPTRequest alarmPrompt = chatService.createAlarmPrompt();
        String response = getChatgptResponse(alarmPrompt);

        // 받은 응답을 바탕으로 질문을 생성하여 DB에 저장
        saveQuestions(response);
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

    // DB에서 최신 질문 가져오기
    public List<Question> getLatestQuestions() {
        return questionRepository.findAll();
    }

    // 매일 새벽 6시 30분에 질문 생성
    @Scheduled(cron = "0 30 6 * * *")
    public void scheduledQuestionGeneration() {
        generateQuestions();
    }

    public void saveQuestions(String response) {
        // 숫자와 마침표, 공백으로 구분하여 질문을 분리하는 정규 표현식 사용
        String[] questions = response.split("\\d+\\.\\s");

        for (String text : questions) {
            text = text.trim(); // 앞뒤 공백 제거
            if (!text.isEmpty()) { // 비어 있지 않은 경우에만 저장
                Question question = Question.builder()
                        .text(text)
                        .createdAt(LocalDateTime.now())
                        .build();
                questionRepository.save(question);
            }
        }
    }

    // 가장 최신의 두 개의 질문을 가져오는 메서드
    public List<Question> getLatestTwoQuestions() {
        return questionRepository.findTop2ByOrderByCreatedAtDesc();
    }
}