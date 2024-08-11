package com.kbai.corporatefinance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company2")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Company1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = true)
    private Long cid; // 고유id

    @Column(nullable = true)
    private Long stockCode; // 주식코드

    @Column(nullable = true)
    private String companyCode; // 기업코드

    @Column(nullable = true)
    private String companyName; // 기업이름

    @Column(nullable = true)
    private String esg; // ESG등급

    @Column(nullable = true)
    private String esg_e; // 환경

    @Column(nullable = true)
    private String esg_s; // 사회

    @Column(nullable = true)
    private String esg_g; // 지배구조

    @Column(nullable = true)
    private float per; // PER

    @Column(nullable = true)
    private float pbr; // PBR

    @Column(nullable = true)
    private float dividendYield; // 배당수익률

    @Column(nullable = true)
    private int maleContractEmployeeCount; // 계약직수남

    @Column(nullable = true)
    private int femaleContractEmployeeCount; // 계약직수여

    @Column(nullable = true)
    private Long totalSalaryMale; // 연봉합계남

    @Column(nullable = true)
    private Long totalSalaryFemale; // 연봉합계여

    @Column(nullable = true)
    private Long maleRegularEmployeeCount; // 정규직수남

    @Column(nullable = true)
    private Long femaleRegularEmployeeCount; // 정규직수여

    @Column(nullable = true)
    private Long totalMaleEmployees; // 남자합계

    @Column(nullable = true)
    private Long totalFemaleEmployees; // 여자합계

    @Column(nullable = true)
    private float averageMaleSalary; // 평균연봉남

    @Column(nullable = true)
    private float averageFemaleSalary; // 평균연봉여

    @Column(nullable = true)
    private float operatingProfitBeforeTax; // 세전계속사업이익률

    @Column(nullable = true)
    private float netProfitMargin; // 순이익률

    @Column(nullable = true)
    private float comprehensiveIncomeMargin; // 총포괄이익률

    @Column(nullable = true)
    private float grossProfitMargin; // 매출총이익률

    @Column(nullable = true)
    private float costOfGoodsSold; // 매출원가율

    @Column(nullable = true)
    private float roe; // ROE

    @Column(nullable = true)
    private float sgAndAExpenseRatio; // 판관비율

    @Column(nullable = true)
    private float operatingIncomeOnTotalAssets; // 총자산영업이익률

    @Column(nullable = true)
    private float preTaxIncomeOnTotalAssets; // 총자산세전계속사업이익률

    @Column(nullable = true)
    private float operatingIncomeOnEquity; // 자기자본영업이익률

    @Column(nullable = true)
    private float preTaxIncomeOnEquity; // 자기자본세전계속사업이익률

    @Column(nullable = true)
    private float operatingIncomeOnPaidInCapital; // 자본금영업이익률

    @Column(nullable = true)
    private float preTaxIncomeOnPaidInCapital; // 자본금세전계속사업이익률

    @Column(nullable = true)
    private float roi; // 납입자본이익률

    @Column(nullable = true)
    private float operatingExpenseRatio; // 영업수익경비율

    @Column(nullable = true)
    private float equityRatio; // 자기자본비율

    @Column(nullable = true)
    private float debtRatio; // 부채비율

    @Column(nullable = true)
    private float liquidityRatio; // 유동비율

    @Column(nullable = true)
    private float quickRatio; // 당좌비율

    @Column(nullable = true)
    private float currentLiabilitiesRatio; // 유동부채비율

    @Column(nullable = true)
    private float nonCurrentLiabilitiesRatio; // 비유동부채비율

    @Column(nullable = true)
    private float interestCoverageRatio; // 이자보상배율

    @Column(nullable = true)
    private float netInterestCoverageRatio; // 순이자보상배율

    @Column(nullable = true)
    private float nonCurrentAssetRatio; // 비유동비율

    @Column(nullable = true)
    private float financialCostBurdenRatio; // 금융비용부담률

    @Column(nullable = true)
    private float capitalRetentionRatio; // 자본유보율

    @Column(nullable = true)
    private float retentionAmountToEquityRatio; // 유보액대비율

    @Column(nullable = true)
    private float financialLeverage; // 재무레버리지

    @Column(nullable = true)
    private float nonCurrentAssetSuitabilityRatio; // 비유동적합률

    @Column(nullable = true)
    private float nonCurrentAssetCompositionRatio; // 비유동자산구성비율

    @Column(nullable = true)
    private float tangibleAssetCompositionRatio; // 유형자산구성비율

    @Column(nullable = true)
    private float currentAssetCompositionRatio; // 유동자산구성비율

    @Column(nullable = true)
    private float inventoryCompositionRatio; // 재고자산구성비율

    @Column(nullable = true)
    private float currentToNonCurrentAssetRatio; // 유동자산/비유동자산비율

    @Column(nullable = true)
    private float inventoryToCurrentAssetRatio; // 재고자산/유동자산비율

    @Column(nullable = true)
    private float receivablesToPayablesRatio; // 매출채권/매입채무비율

    @Column(nullable = true)
    private float payablesToInventoryRatio; // 매입채무/재고자산비율

    @Column(nullable = true)
    private float salesGrowthRateYoY; // 매출액증가율(YoY)

    @Column(nullable = true)
    private float grossProfitGrowthRateYoY; // 매출총이익증가율(YoY)

    @Column(nullable = true)
    private float operatingProfitGrowthRateYoY; // 영업이익증가율(YoY)

    @Column(nullable = true)
    private float preTaxProfitGrowthRateYoY; // 세전계속사업이익증가율(YoY)

    @Column(nullable = true)
    private float netProfitGrowthRateYoY; // 순이익증가율(YoY)

    @Column(nullable = true)
    private float comprehensiveIncomeGrowthRateYoY; // 총포괄이익증가율(YoY)

    @Column(nullable = true)
    private float totalAssetGrowthRateYoY; // 총자산증가율

    @Column(nullable = true)
    private float nonCurrentAssetGrowthRateYoY; // 비유동자산증가율

    @Column(nullable = true)
    private float tangibleAssetGrowthRateYoY; // 유형자산증가율

    @Column(nullable = true)
    private float totalLiabilitiesGrowthRateYoY; // 부채총계증가율

    @Column(nullable = true)
    private float totalBorrowingsGrowthRateYoY; // 총차입금증가율

    @Column(nullable = true)
    private float equityGrowthRateYoY; // 자기자본증가율

    @Column(nullable = true)
    private float currentAssetGrowthRateYoY; // 유동자산증가율

    @Column(nullable = true)
    private float receivablesGrowthRateYoY; // 매출채권증가율

    @Column(nullable = true)
    private float inventoryGrowthRateYoY; // 재고자산증가율

    @Column(nullable = true)
    private float currentLiabilitiesGrowthRateYoY; // 유동부채증가율

    @Column(nullable = true)
    private float payablesGrowthRateYoY; // 매입채무증가율

    @Column(nullable = true)
    private float nonCurrentLiabilitiesGrowthRateYoY; // 비유동부채증가율

    @Column(nullable = true)
    private float totalAssetTurnover; // 총자산회전율

    @Column(nullable = true)
    private float receivablesTurnover; // 매출채권회전율

    @Column(nullable = true)
    private float inventoryTurnover; // 재고자산회전율

    @Column(nullable = true)
    private float costOfGoodsSoldToInventory; // 매출원가/재고자산

    @Column(nullable = true)
    private float payablesTurnover; // 매입채무회전율

    @Column(nullable = true)
    private float nonCurrentAssetTurnover; // 비유동자산회전율

    @Column(nullable = true)
    private float tangibleAssetTurnover; // 유형자산회전율

    @Column(nullable = true)
    private float debtToEquityTurnover; // 타인자본회전율

    @Column(nullable = true)
    private float equityTurnover; // 자기자본회전율

    @Column(nullable = true)
    private float capitalTurnover; // 자본금회전율

    @Column(nullable = true)
    private float dividendPayoutRatio; // 배당성향(%)

    @Column(nullable = true)
    private int femaleExecutives; // 여성임원수

    @Column(nullable = true)
    private int maleExecutives; // 남성임원수

    @Column(nullable = true)
    private String status; // 상태

    @Column(nullable = true)
    private String message; // 메시지

    @Column(nullable = true)
    private String corpNameEng; // 회사 이름 (영어)

    @Column(nullable = true)
    private String stockName; // 주식명

    @Column(nullable = true)
    private String ceoName; // CEO 이름

    @Column(nullable = true)
    private String corpCls; // 회사 분류

    @Column(nullable = true)
    private String jurirNo; // 법인등록번호

    @Column(nullable = true)
    private String bizrNo; // 사업자등록번호

    @Column(nullable = true)
    private String address; // 주소

    @Column(nullable = true)
    private String homeUrl; // 홈페이지 URL

    @Column(nullable = true)
    private String irUrl; // IR URL

    @Column(nullable = true)
    private String phoneNumber; // 전화번호

    @Column(nullable = true)
    private String faxNumber; // 팩스번호

    @Column(nullable = true)
    private String industryCode; // 산업코드

    @Column(nullable = true)
    private String establishmentDate; // 설립일자

    @Column(nullable = true)
    private String accountingMonth; // 회계월

    @Column(nullable = true)
    private Long financialCostCurrent; // 금융비용_당액

    @Column(nullable = true)
    private Long financialCostPrevious; // 금융비용_전기

    @Column(nullable = true)
    private Long financialCostPrePrevious; // 금융비용_전전기

    @Column(nullable = true)
    private Long cashAndCashEquivalentsCurrent; // 현금및현금성자산_당액

    @Column(nullable = true)
    private Long cashAndCashEquivalentsPrevious; // 현금및현금성자산_전기

    @Column(nullable = true)
    private Long cashAndCashEquivalentsPrePrevious; // 현금및현금성자산_전전기

    @Column(nullable = true)
    private Long operatingCashFlowCurrent; // 영업활동현금흐름_당액

    @Column(nullable = true)
    private Long operatingCashFlowPrevious; // 영업활동현금흐름_전기

    @Column(nullable = true)
    private Long operatingCashFlowPrePrevious; // 영업활동현금흐름_전전기

    @Column(nullable = true)
    private Long provisionsCurrent; // 충당부채_당액

    @Column(nullable = true)
    private Long provisionsPrevious; // 충당부채_전기

    @Column(nullable = true)
    private Long provisionsPrePrevious; // 충당부채_전전기

    @Column(nullable = true)
    private Long financingCashFlowCurrent; // 재무활동현금흐름_당액

    @Column(nullable = true)
    private Long financingCashFlowPrevious; // 재무활동현금흐름_전기

    @Column(nullable = true)
    private Long financingCashFlowPrePrevious; // 재무활동현금흐름_전전기

    @Column(nullable = true)
    private Long investingCashFlowCurrent; // 투자활동현금흐름_당액

    @Column(nullable = true)
    private Long investingCashFlowPrevious; // 투자활동현금흐름_전기

    @Column(nullable = true)
    private Long investingCashFlowPrePrevious; // 투자활동현금흐름_전전기

    @Column(nullable = true)
    private Long accountsReceivableCurrent; // 미수금_당액

    @Column(nullable = true)
    private Long accountsReceivablePrevious; // 미수금_전기

    @Column(nullable = true)
    private Long accountsReceivablePrePrevious; // 미수금_전전기

    @Column(nullable = true)
    private Long totalLiabilitiesCurrent; // 부채총계_당액

    @Column(nullable = true)
    private Long totalLiabilitiesPrevious; // 부채총계_전기

    @Column(nullable = true)
    private Long totalLiabilitiesPrePrevious; // 부채총계_전전기

    @Column(nullable = true)
    private Long nonCurrentLiabilitiesCurrent; // 비유동부채_당액

    @Column(nullable = true)
    private Long nonCurrentLiabilitiesPrevious; // 비유동부채_전기

    @Column(nullable = true)
    private Long nonCurrentLiabilitiesPrePrevious; // 비유동부채_전전기

    @Column(nullable = true)
    private Long nonCurrentAssetsCurrent; // 비유동자산_당액

    @Column(nullable = true)
    private Long nonCurrentAssetsPrevious; // 비유동자산_전기

    @Column(nullable = true)
    private Long nonCurrentAssetsPrePrevious; // 비유동자산_전전기

    @Column(nullable = true)
    private Long currentLiabilitiesCurrent; // 유동부채_당액

    @Column(nullable = true)
    private Long currentLiabilitiesPrevious; // 유동부채_전기

    @Column(nullable = true)
    private Long currentLiabilitiesPrePrevious; // 유동부채_전전기

    @Column(nullable = true)
    private Long currentAssetsCurrent; // 유동자산_당액

    @Column(nullable = true)
    private Long currentAssetsPrevious; // 유동자산_전기

    @Column(nullable = true)
    private Long currentAssetsPrePrevious; // 유동자산_전전기

    @Column(nullable = true)
    private Long shareCapitalCurrent; // 자본금_당액

    @Column(nullable = true)
    private Long shareCapitalPrevious; // 자본금_전기

    @Column(nullable = true)
    private Long shareCapitalPrePrevious; // 자본금_전전기

    @Column(nullable = true)
    private Long totalAssetsCurrent; // 자산총계_당액

    @Column(nullable = true)
    private Long totalAssetsPrevious; // 자산총계_전기

    @Column(nullable = true)
    private Long totalAssetsPrePrevious; // 자산총계_전전기

    @Column(nullable = true)
    private Long salesCurrent; // 매출액_당액

    @Column(nullable = true)
    private Long salesPrevious; // 매출액_전기

    @Column(nullable = true)
    private Long salesPrePrevious; // 매출액_전전기

    @Column(nullable = true)
    private Long operatingProfitCurrent; // 영업이익_당액

    @Column(nullable = true)
    private Long operatingProfitPrevious; // 영업이익_전기

    @Column(nullable = true)
    private Long operatingProfitPrePrevious; // 영업이익_전전기

    @Column(nullable = true)
    private Long accountsReceivableCurrent2; // 매출채권_당액

    @Column(nullable = true)
    private Long accountsReceivablePrevious2; // 매출채권_전기

    @Column(nullable = true)
    private Long accountsReceivablePrePrevious2; // 매출채권_전전기

    @Column(nullable = true)
    private Long costOfSalesCurrent; // 매출원가_당액

    @Column(nullable = true)
    private Long costOfSalesPrevious; // 매출원가_전기

    @Column(nullable = true)
    private Long costOfSalesPrePrevious; // 매출원가_전전기
}