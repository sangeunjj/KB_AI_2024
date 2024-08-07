package com.kbai.corporatefinance.controller;

import com.kbai.corporatefinance.dto.ChatGPTRequest;
import com.kbai.corporatefinance.dto.ChatGPTResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bot")
public class CustomBotController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    @Autowired
    private RestTemplate template;

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "prompt") String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
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
}