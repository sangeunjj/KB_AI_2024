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

    // [기업 페이지]

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

    // [기업 상세 페이지]
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

    // [레포트 기업 선택 화면]


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

    // [기업 레포트 피처 선택하는 화면]

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
                companyData.put("ESG_23_e", company.getEsg_e());
                companyData.put("ESG_23_s", company.getEsg_s());
                companyData.put("ESG_23_g", company.getEsg_g());
            }
            // 활동성 지표
            if (features.contains("활동성 지표")) {
                Map<String, Object> activityMetrics = new HashMap<>();
                activityMetrics.put("총자산회전율", company.getTotalAssetTurnover());
                activityMetrics.put("매출채권회전율", company.getReceivablesTurnover());
                activityMetrics.put("재고자산회전율", company.getInventoryTurnover());
                activityMetrics.put("매출원가/재고자산", company.getCostOfGoodsSoldToInventory());
                activityMetrics.put("매입채무회전율", company.getPayablesTurnover());
                activityMetrics.put("비유동자산회전율", company.getNonCurrentAssetTurnover());
                activityMetrics.put("유형자산회전율", company.getTangibleAssetTurnover());
                activityMetrics.put("타인자본회전율", company.getDebtToEquityTurnover());
                activityMetrics.put("자기자본회전율", company.getEquityTurnover());
                activityMetrics.put("자본금회전율", company.getCapitalTurnover());
                activityMetrics.put("배당성향(%)", company.getDividendPayoutRatio());
                companyData.put("활동성 지표", activityMetrics);
            }

            // 성장성 지표
            if (features.contains("성장성 지표")) {
                Map<String, Object> growthMetrics = new HashMap<>();
                growthMetrics.put("매출액증가율(YoY)", company.getSalesGrowthRateYoY());
                growthMetrics.put("매출총이익증가율(YoY)", company.getGrossProfitGrowthRateYoY());
                growthMetrics.put("영업이익증가율(YoY)", company.getOperatingProfitGrowthRateYoY());
                growthMetrics.put("세전계속사업이익증가율(YoY)", company.getPreTaxProfitGrowthRateYoY());
                growthMetrics.put("순이익증가율(YoY)", company.getNetProfitGrowthRateYoY());
                growthMetrics.put("총포괄이익증가율(YoY)", company.getComprehensiveIncomeGrowthRateYoY());
                growthMetrics.put("총자산증가율", company.getTotalAssetGrowthRateYoY());
                growthMetrics.put("비유동자산증가율", company.getNonCurrentAssetGrowthRateYoY());
                growthMetrics.put("유형자산증가율", company.getTangibleAssetGrowthRateYoY());
                growthMetrics.put("부채총계증가율", company.getTotalLiabilitiesGrowthRateYoY());
                growthMetrics.put("총차입금증가율", company.getTotalBorrowingsGrowthRateYoY());
                growthMetrics.put("자기자본증가율", company.getEquityGrowthRateYoY());
                growthMetrics.put("유동자산증가율", company.getCurrentAssetGrowthRateYoY());
                growthMetrics.put("매출채권증가율", company.getReceivablesGrowthRateYoY());
                growthMetrics.put("재고자산증가율", company.getInventoryGrowthRateYoY());
                growthMetrics.put("유동부채증가율", company.getCurrentLiabilitiesGrowthRateYoY());
                growthMetrics.put("매입채무증가율", company.getPayablesGrowthRateYoY());
                growthMetrics.put("비유동부채증가율", company.getNonCurrentLiabilitiesGrowthRateYoY());
                companyData.put("성장성 지표", growthMetrics);
            }
            // 안정성 지표
            if (features.contains("안정성 지표")) {
                Map<String, Object> stabilityMetrics = new HashMap<>();
                stabilityMetrics.put("자기자본비율", company.getEquityRatio());
                stabilityMetrics.put("부채비율", company.getDebtRatio());
                stabilityMetrics.put("유동비율", company.getLiquidityRatio());
                stabilityMetrics.put("당좌비율", company.getQuickRatio());
                stabilityMetrics.put("유동부채비율", company.getCurrentLiabilitiesRatio());
                stabilityMetrics.put("비유동부채비율", company.getNonCurrentLiabilitiesRatio());
                stabilityMetrics.put("이자보상배율", company.getInterestCoverageRatio());
                stabilityMetrics.put("순이자보상배율", company.getNetInterestCoverageRatio());
                stabilityMetrics.put("비유동비율", company.getNonCurrentAssetRatio());
                stabilityMetrics.put("금융비용부담률", company.getFinancialCostBurdenRatio());
                stabilityMetrics.put("자본유보율", company.getCapitalRetentionRatio());
                stabilityMetrics.put("유보액대비율", company.getRetentionAmountToEquityRatio());
                stabilityMetrics.put("재무레버리지", company.getFinancialLeverage());
                stabilityMetrics.put("비유동적합률", company.getNonCurrentAssetSuitabilityRatio());
                stabilityMetrics.put("비유동자산구성비율", company.getNonCurrentAssetCompositionRatio());
                stabilityMetrics.put("유형자산구성비율", company.getTangibleAssetCompositionRatio());
                stabilityMetrics.put("유동자산구성비율", company.getCurrentAssetCompositionRatio());
                stabilityMetrics.put("재고자산구성비율", company.getInventoryCompositionRatio());
                stabilityMetrics.put("유동자산/비유동자산비율", company.getCurrentToNonCurrentAssetRatio());
                stabilityMetrics.put("재고자산/유동자산비율", company.getInventoryToCurrentAssetRatio());
                stabilityMetrics.put("매출채권/매입채무비율", company.getReceivablesToPayablesRatio());
                stabilityMetrics.put("매입채무/재고자산비율", company.getPayablesToInventoryRatio());
                companyData.put("안정성 지표", stabilityMetrics);
            }
            // 수익성 지표
            if (features.contains("수익성 지표")) {
                Map<String, Object> profitabilityMetrics = new HashMap<>();
                profitabilityMetrics.put("세전계속사업이익률", company.getOperatingProfitBeforeTax());
                profitabilityMetrics.put("순이익률", company.getNetProfitMargin());
                profitabilityMetrics.put("총포괄이익률", company.getComprehensiveIncomeMargin());
                profitabilityMetrics.put("매출총이익률", company.getGrossProfitMargin());
                profitabilityMetrics.put("매출원가율", company.getCostOfGoodsSold());
                profitabilityMetrics.put("ROE", company.getRoe());
                profitabilityMetrics.put("판관비율", company.getSgAndAExpenseRatio());
                profitabilityMetrics.put("총자산영업이익률", company.getOperatingIncomeOnTotalAssets());
                profitabilityMetrics.put("총자산세전계속사업이익률", company.getPreTaxIncomeOnTotalAssets());
                profitabilityMetrics.put("자기자본영업이익률", company.getOperatingIncomeOnEquity());
                profitabilityMetrics.put("자기자본세전계속사업이익률", company.getPreTaxIncomeOnEquity());
                profitabilityMetrics.put("자본금영업이익률", company.getOperatingIncomeOnPaidInCapital());
                profitabilityMetrics.put("자본금세전계속사업이익률", company.getPreTaxIncomeOnPaidInCapital());
                profitabilityMetrics.put("납입자본이익률", company.getRoi());
                profitabilityMetrics.put("영업수익경비율", company.getOperatingExpenseRatio());
                companyData.put("수익성 지표", profitabilityMetrics);
            }
            // 3개년 현금 흐름 분석 추가
            if (features.contains("3개년 현금 흐름 분석")) {
                Map<String, Object> cashFlowData = new HashMap<>();

                // 영업활동현금흐름
                Map<String, Long> operatingCashFlow = new HashMap<>();
                operatingCashFlow.put("current", company.getOperatingCashFlowCurrent());
                operatingCashFlow.put("previous", company.getOperatingCashFlowPrevious());
                operatingCashFlow.put("prePrevious", company.getOperatingCashFlowPrePrevious());
                cashFlowData.put("operatingCashFlow", operatingCashFlow);

                // 투자활동현금흐름
                Map<String, Long> investingCashFlow = new HashMap<>();
                investingCashFlow.put("current", company.getInvestingCashFlowCurrent());
                investingCashFlow.put("previous", company.getInvestingCashFlowPrevious());
                investingCashFlow.put("prePrevious", company.getInvestingCashFlowPrePrevious());
                cashFlowData.put("investingCashFlow", investingCashFlow);

                // 재무활동현금흐름
                Map<String, Long> financingCashFlow = new HashMap<>();
                financingCashFlow.put("current", company.getFinancingCashFlowCurrent());
                financingCashFlow.put("previous", company.getFinancingCashFlowPrevious());
                financingCashFlow.put("prePrevious", company.getFinancingCashFlowPrePrevious());
                cashFlowData.put("financingCashFlow", financingCashFlow);

                companyData.put("cashFlow", cashFlowData);
            }
            if (features.contains("재무 건전성 및 유동성 분석")) {
                Map<String, Object> financialCostToSalesRatio = new HashMap<>();
                financialCostToSalesRatio.put("current", company.getFinancialCostToSalesRatioCurrent());
                financialCostToSalesRatio.put("previous", company.getFinancialCostToSalesRatioPrevious());
                financialCostToSalesRatio.put("prePrevious", company.getFinancialCostToSalesRatioPrePrevious());
                companyData.put("financialCostToSalesRatio", financialCostToSalesRatio);

                Map<String, Object> receivablesToCashRatio = new HashMap<>();
                receivablesToCashRatio.put("current", company.getReceivablesToCashRatioCurrent());
                receivablesToCashRatio.put("previous", company.getReceivablesToCashRatioPrevious());
                receivablesToCashRatio.put("prePrevious", company.getReceivablesToCashRatioPrePrevious());
                companyData.put("receivablesToCashRatio", receivablesToCashRatio);
            }

            // 3개년 영업이익률 변화율
            if (features.contains("재무 건전성 및 수익성 지표 분석")) {
                Map<String, Object> operatingProfitMargin = new HashMap<>();
                operatingProfitMargin.put("current", company.getOperatingProfitMarginCurrent());
                operatingProfitMargin.put("previous", company.getOperatingProfitMarginPrevious());
                operatingProfitMargin.put("prePrevious", company.getOperatingProfitMarginPrePrevious());
                companyData.put("operatingProfitMargin", operatingProfitMargin);

                Map<String, Object> debtRatio = new HashMap<>();
                debtRatio.put("current", company.getDebtRatioCurrent());
                debtRatio.put("previous", company.getDebtRatioPrevious());
                debtRatio.put("prePrevious", company.getDebtRatioPrePrevious());
                companyData.put("debtRatio", debtRatio);

                Map<String, Object> liquidityRatio = new HashMap<>();
                liquidityRatio.put("current", company.getLiquidityRatioCurrent());
                liquidityRatio.put("previous", company.getLiquidityRatioPrevious());
                liquidityRatio.put("prePrevious", company.getLiquidityRatioPrePrevious());
                companyData.put("liquidityRatio", liquidityRatio);
            }
            return companyData;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }
}