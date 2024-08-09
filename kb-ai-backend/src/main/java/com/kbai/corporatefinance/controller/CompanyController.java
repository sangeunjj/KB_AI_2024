package com.kbai.corporatefinance.controller;

import com.kbai.corporatefinance.dto.CompanyDetailResponse;
import com.kbai.corporatefinance.dto.CompanyResponse;
import com.kbai.corporatefinance.dto.DartResponse;
import com.kbai.corporatefinance.entity.Company;
import com.kbai.corporatefinance.service.CompanyService;
import com.kbai.corporatefinance.service.DartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;
    private final DartService dartService;

    @Value("${dart.api.key}")
    private String dartApiKey;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CompanyResponse>> getCompanyList() { // dart api와 db에서 모두 가져오기
        // DB에서 모든 Company 엔티티 가져오기
        List<Company> companies = companyService.getAllCompanies();

        List<CompanyResponse> responses = new ArrayList<>();

        // 각 회사에 대해 Dart API 호출하여 추가 데이터 가져오기
        for (Company company : companies) {
            DartResponse dartData = dartService.getCompanyInfo(company.getCompanyCode().toString(), dartApiKey);

            // 각 회사의 데이터를 합쳐서 CompanyResponse로 변환
            CompanyResponse response = new CompanyResponse(
                    company.getCompanyName(),
                    company.getFemaleExecutives(),
                    company.getEsg(),
                    company.getSentimentScore(),
                    company.getCompanyCode(),
                    dartData.getAdres(),
                    dartData.getEst_dt(),
                    dartData.getCeo_nm()
            );

            // 리스트에 추가
            responses.add(response);
        }
        // 리스트를 응답으로 반환
        return ResponseEntity.ok(responses);
    }

    @GetMapping(value = "/{corpCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyDetailResponse> getCompanyDetails(@PathVariable Long corpCode) { // dart api와 db에서 모두 가져오기
        // DB에서 Company 엔티티 가져오기
        Company company = companyService.getCompanyByCode(corpCode);

        // Dart API 호출하여 추가 데이터 가져오기
        DartResponse dartData = dartService.getCompanyInfo(corpCode.toString(), dartApiKey);

        // 모든 데이터를 합쳐서 반환 (db 데이터 + Dart 데이터)
        CompanyDetailResponse response = new CompanyDetailResponse(
                company,
                dartData
        );
        return ResponseEntity.ok(response);
    }
}