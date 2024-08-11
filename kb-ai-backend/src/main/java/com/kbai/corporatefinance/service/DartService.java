package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.dto.DartResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DartService {

    public DartResponse getCompanyInfo(String corpCode, String dartApiKey) {
        String url = UriComponentsBuilder.fromHttpUrl("https://opendart.fss.or.kr/api/company.json")
                .queryParam("crtfc_key", dartApiKey)
                .queryParam("corp_code", corpCode) // TODO 회사코드 앞에 00을 붙여서 호출해야 함
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        DartResponse response = restTemplate.getForObject(url, DartResponse.class);
        return response;
    }
}
