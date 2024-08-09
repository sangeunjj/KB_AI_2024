package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.dto.CompanyResponse;
import lombok.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DartService {

    public CompanyResponse getCompanyInfo(String corpCode, String dartApiKey) {
        String url = UriComponentsBuilder.fromHttpUrl("https://opendart.fss.or.kr/api/company.json")
                .queryParam("crtfc_key", dartApiKey)
                .queryParam("corp_code", corpCode)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        CompanyResponse response = restTemplate.getForObject(url, CompanyResponse.class);
        return response;
    }
}
