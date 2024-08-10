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

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

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

    @GetMapping(value = "/ABC", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getCompanyNamesSortedByABC() {
        List<Company> companies = companyService.getAllCompanies();

        // Collator를 사용하여 한글과 영문을 함께 정렬, 한글을 우선 정렬
        Collator collator = Collator.getInstance(new Locale("ko", "KR"));
        List<String> sortedCompanyNames = companies.stream()
                .map(Company::getCompanyName)
                .sorted((name1, name2) -> {
                    boolean name1IsKorean = isKorean(name1);
                    boolean name2IsKorean = isKorean(name2);
                    if (name1IsKorean && !name2IsKorean) {
                        return -1; // name1이 한글이고 name2가 영어면 name1이 먼저 오도록 함
                    } else if (!name1IsKorean && name2IsKorean) {
                        return 1; // name1이 영어이고 name2가 한글이면 name2가 먼저 오도록 함
                    } else {
                        return collator.compare(name1, name2); // 동일 언어끼리는 기본 정렬
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(sortedCompanyNames);
    }

    // 한글 여부를 확인하는 함수
    private boolean isKorean(String name) {
        return name.codePoints().anyMatch(codepoint -> Character.UnicodeBlock.of(codepoint) == Character.UnicodeBlock.HANGUL_SYLLABLES);
    }
}