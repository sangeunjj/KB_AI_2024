package com.kbai.corporatefinance.controller;

import com.kbai.corporatefinance.dto.CompanyDetailResponse;
import com.kbai.corporatefinance.dto.CompanyResponse;
import com.kbai.corporatefinance.dto.DartResponse;
import com.kbai.corporatefinance.entity.Company1;
import com.kbai.corporatefinance.service.CompanyService;
import com.kbai.corporatefinance.service.DartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

import com.kbai.corporatefinance.dto.CompanyDTO;


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
        List<Company1> companies = companyService.getAllCompanies();

        List<CompanyResponse> responses = new ArrayList<>();

        // 각 회사에 대해 Dart API 호출하여 추가 데이터 가져오기
        for (Company1 company : companies) {
//            DartResponse dartData = dartService.getCompanyInfo(company.getCompanyCode(), dartApiKey);

            // 각 회사의 데이터를 합쳐서 CompanyResponse로 변환
            CompanyResponse response = new CompanyResponse(
                    company.getCompanyName(),
//                    company.getFemaleExecutives(),
                    company.getEsg()
//                    company.getSentimentScore(),
//                    company.getCompanyCode(),
//                    dartData.getAdres(),
//                    dartData.getEst_dt(),
//                    dartData.getCeo_nm()
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
        Company1 company = companyService.getCompanyByCode(corpCode);

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
    public ResponseEntity<List<CompanyDTO>> getCompanyNamesSortedByABC() {
        List<Company1> companies = companyService.getAllCompanies();

        // Collator를 사용하여 한글과 영문을 함께 정렬, 한글을 우선 정렬
        Collator collator = Collator.getInstance(new Locale("ko", "KR"));
        List<CompanyDTO> sortedCompanies = companies.stream()
                .map(company -> new CompanyDTO(company.getCompanyName(), company.getCompanyCode()))
                .sorted((company1, company2) -> {
                    boolean name1IsKorean = isKorean(company1.getCompanyName());
                    boolean name2IsKorean = isKorean(company2.getCompanyName());
                    if (name1IsKorean && !name2IsKorean) {
                        return -1; // name1이 한글이고 name2가 영어면 name1이 먼저 오도록 함
                    } else if (!name1IsKorean && name2IsKorean) {
                        return 1; // name1이 영어이고 name2가 한글이면 name2가 먼저 오도록 함
                    } else {
                        return collator.compare(company1.getCompanyName(), company2.getCompanyName()); // 동일 언어끼리는 기본 정렬
                    }
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(sortedCompanies);
    }


    // 한글 여부를 확인하는 함수
    private boolean isKorean(String name) {
        return name.codePoints().anyMatch(codepoint -> Character.UnicodeBlock.of(codepoint) == Character.UnicodeBlock.HANGUL_SYLLABLES);
    }

    @GetMapping("/features")
    public ResponseEntity<List<Map<String, Object>>> getCompanyFeatures(
            @RequestParam List<String> companyCodes, // 회사 코드를 받아옴
            @RequestParam List<String> features) {

        // 회사 코드 리스트에 따라 회사 엔티티를 가져옴
        List<Company1> companies = companyService.getCompaniesByCodes(companyCodes);

        // 선택된 피처에 따라 데이터를 매핑
        List<Map<String, Object>> result = companies.stream().map(company -> {
            Map<String, Object> companyData = new HashMap<>();
            companyData.put("companyName", company.getCompanyName());

            // 선택된 피처에 따라 데이터 매핑
            if (features.contains("ESG")) {
                companyData.put("ESG_23", company.getEsg());
//                companyData.put("ESG_22", company.getEsg_22());
                companyData.put("ESG_23_e", company.getEsg_e());
                companyData.put("ESG_23_s", company.getEsg_s());
                companyData.put("ESG_23_g", company.getEsg_g());
//                companyData.put("ESG_22_e", company.getEsg_22_e());
//                companyData.put("ESG_22_s", company.getEsg_22_s());
//                companyData.put("ESG_22_g", company.getEsg_22_g());
            }
//            if (features.contains("베타계수")) {
//                companyData.put("베타계수", company.getBetaCoefficient());
//            }
//            if (features.contains("여성임원수")) {
//                companyData.put("여성임원수", company.getFemaleExecutives());
//            }
//            if (features.contains("정규직 유무")) {
//                companyData.put("정규직 유무", company.getRegularEmployeeCount());
//            }
//            if (features.contains("성별")) {
//                companyData.put("성별", company.getGender());
//            }
            // TODO 데이터 받으면 시행
            if (features.contains("사업보고서(현금흐름표)")) {
                // 이 부분은 추후 현금흐름표 데이터가 추가되면 구현
                companyData.put("현금흐름표", "현금흐름표 데이터 없음"); // 예시 데이터
            }
            if (features.contains("사업보고서(손익계산서)")) {
                // 이 부분은 추후 손익계산서 데이터가 추가되면 구현
                companyData.put("손익계산서", "손익계산서 데이터 없음"); // 예시 데이터
            }
            if (features.contains("사업보고서(재무상태표)")) {
                // 이 부분은 추후 재무상태표 데이터가 추가되면 구현
                companyData.put("재무상태표", "재무상태표 데이터 없음"); // 예시 데이터
            }
//            if (features.contains("뉴스 내용 및 개수")) {
//                companyData.put("뉴스 내용 및 개수", company.getNewsSummary());
//                companyData.put("기사 개수", company.getTwoWeeksArticleCount());
//            }
//            if (features.contains("산업현황(긍정/부정 점수)")) {
//                companyData.put("긍정/부정 점수", company.getSentimentScore());
//            }

            return companyData;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}