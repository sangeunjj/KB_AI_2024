package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.entity.Company1;
import com.kbai.corporatefinance.repository.CompanyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Transactional
    public Company1 getCompanyByCode(Long companyCode) {
        return companyRepository.findById(companyCode)
                .orElseThrow(() -> new RuntimeException("기업코드에 맞는 회사를 찾을 수 없습니다."));
    }

    // 모든 회사를 가져오는 메서드 추가
    public List<Company1> getAllCompanies() {
        return companyRepository.findAll();
    }

    // 회사 이름 리스트에 따라 회사 엔티티를 가져오는 메서드

    // 회사 코드 리스트에 따라 회사 엔티티를 가져오는 메서드
    public List<Company1> getCompaniesByCodes(List<String> companyCodes) {
        return companyRepository.findByCompanyCodeIn(companyCodes);
    }

    public Map<String, Object> getCompanyFeatures(Company1 company, List<String> features) {
        Map<String, Object> companyData = new HashMap<>();

        // 3개년 현금 흐름 분석
        if (features.contains("3개년 현금 흐름 분석")) {
            Map<String, Long> operatingCashFlow = new HashMap<>();
            operatingCashFlow.put("current", company.getOperatingCashFlowCurrent());
            operatingCashFlow.put("previous", company.getOperatingCashFlowPrevious());
            operatingCashFlow.put("prePrevious", company.getOperatingCashFlowPrePrevious());
            companyData.put("operatingCashFlow", operatingCashFlow);

            Map<String, Long> investingCashFlow = new HashMap<>();
            investingCashFlow.put("current", company.getInvestingCashFlowCurrent());
            investingCashFlow.put("previous", company.getInvestingCashFlowPrevious());
            investingCashFlow.put("prePrevious", company.getInvestingCashFlowPrePrevious());
            companyData.put("investingCashFlow", investingCashFlow);

            Map<String, Long> financingCashFlow = new HashMap<>();
            financingCashFlow.put("current", company.getFinancingCashFlowCurrent());
            financingCashFlow.put("previous", company.getFinancingCashFlowPrevious());
            financingCashFlow.put("prePrevious", company.getFinancingCashFlowPrePrevious());
            companyData.put("financingCashFlow", financingCashFlow);
        }

        // 재무 건전성 및 유동성 분석
        if (features.contains("재무 건전성 및 유동성 분석")) {
            companyData.put("cashAndCashEquivalents.prePrevious", company.getCashAndCashEquivalentsPrePrevious());
            companyData.put("cashAndCashEquivalents.previous", company.getCashAndCashEquivalentsPrevious());
            companyData.put("cashAndCashEquivalents.current", company.getCashAndCashEquivalentsCurrent());

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

        // 재무 건전성 및 수익성 지표 분석
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

        return companyData;
    }
}