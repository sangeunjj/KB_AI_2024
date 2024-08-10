package com.kbai.corporatefinance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cid; // 고유id
    private String companyCode; // 기업코드
    private String companyName; // 기업이름
    private String esg; // ESG등급
    private String esg_e; // 환경
    private String esg_s; // 사회
    private String esg_g; // 지배구조

    private float per; // PER
    private float pbr; // PBR
    private float dividendYield; // 배당수익률
    private String sector; // 부문

    private String gender; // 성별, 남, 여
    private int regularEmployeeCount; // 정규직수
    private int contractEmployeeCount; // 계약직수
    private int totalEmployeeCount; // 합계(정규직+계약직)

    private Long averageSalaryPerPerson; // 1인평균연봉

    private float operatingProfitBeforeTax; // 세전계속사업이익률
    private float netProfitMargin; // 순이익률
    private float comprehensiveIncomeMargin; // 총포괄이익률
    private float grossProfitMargin; // 매출총이익률
    private float costOfGoodsSold; // 매출원가율
    private float roe; // ROE
    private float sgAndAExpenseRatio; // 판관비율
    private float operatingIncomeOnTotalAssets; // 총자산영업이익률
    private float preTaxIncomeOnTotalAssets; // 총자산세전계속사업이익률
    private float operatingIncomeOnEquity; // 자기자본영업이익률
    private float preTaxIncomeOnEquity; // 자기자본세전계속사업이익률
    private float operatingIncomeOnPaidInCapital; // 자본금영업이익률
    private float preTaxIncomeOnPaidInCapital; // 자본금세전계속사업이익률
    private float roi; // 납입자본이익률
    private float operatingExpenseRatio; // 영업수익경비율
    private float equityRatio; // 자기자본비율
    private float debtRatio; // 부채비율
    private float liquidityRatio; // 유동비율
    private float quickRatio; // 당좌비율
    private float currentLiabilitiesRatio; // 유동부채비율
    private float nonCurrentLiabilitiesRatio; // 비유동부채비율
    private float interestCoverageRatio; // 이자보상배율
    private float netInterestCoverageRatio; // 순이자보상배율
    private float nonCurrentAssetRatio; // 비유동비율
    private float financialCostBurdenRatio; // 금융비용부담률
    private float capitalRetentionRatio; // 자본유보율
    private float retentionAmountToEquityRatio; // 유보액대비율
    private float financialLeverage; // 재무레버리지
    private float nonCurrentAssetSuitabilityRatio; // 비유동적합률
    private float nonCurrentAssetCompositionRatio; // 비유동자산구성비율
    private float tangibleAssetCompositionRatio; // 유형자산구성비율
    private float currentAssetCompositionRatio; // 유동자산구성비율
    private float inventoryCompositionRatio; // 재고자산구성비율
    private float currentToNonCurrentAssetRatio; // 유동자산/비유동자산비율
    private float inventoryToCurrentAssetRatio; // 재고자산/유동자산비율
    private float receivablesToPayablesRatio; // 매출채권/매입채무비율
    private float payablesToInventoryRatio; // 매입채무/재고자산비율
    private float salesGrowthRateYoY; // 매출액증가율(YoY)
    private float grossProfitGrowthRateYoY; // 매출총이익증가율(YoY)
    private float operatingProfitGrowthRateYoY; // 영업이익증가율(YoY)
    private float preTaxProfitGrowthRateYoY; // 세전계속사업이익증가율(YoY)
    private float netProfitGrowthRateYoY; // 순이익증가율(YoY)
    private float comprehensiveIncomeGrowthRateYoY; // 총포괄이익증가율(YoY)
    private float totalAssetGrowthRateYoY; // 총자산증가율
    private float nonCurrentAssetGrowthRateYoY; // 비유동자산증가율
    private float tangibleAssetGrowthRateYoY; // 유형자산증가율
    private float totalLiabilitiesGrowthRateYoY; // 부채총계증가율
    private float totalBorrowingsGrowthRateYoY; // 총차입금증가율
    private float equityGrowthRateYoY; // 자기자본증가율
    private float currentAssetGrowthRateYoY; // 유동자산증가율
    private float receivablesGrowthRateYoY; // 매출채권증가율
    private float inventoryGrowthRateYoY; // 재고자산증가율
    private float currentLiabilitiesGrowthRateYoY; // 유동부채증가율
    private float payablesGrowthRateYoY; // 매입채무증가율
    private float nonCurrentLiabilitiesGrowthRateYoY; // 비유동부채증가율
    private float totalAssetTurnover; // 총자산회전율
    private float receivablesTurnover; // 매출채권회전율
    private float inventoryTurnover; // 재고자산회전율
    private float costOfGoodsSoldToInventory; // 매출원가/재고자산
    private float payablesTurnover; // 매입채무회전율
    private float nonCurrentAssetTurnover; // 비유동자산회전율
    private float tangibleAssetTurnover; // 유형자산회전율
    private float debtToEquityTurnover; // 타인자본회전율
    private float equityTurnover; // 자기자본회전율
    private float capitalTurnover; // 자본금회전율
    private float dividendPayoutRatio; // 배당성향(%)
    private int femaleExecutives; // 여성임원수
    private int maleExecutives; // 남성임원수
}