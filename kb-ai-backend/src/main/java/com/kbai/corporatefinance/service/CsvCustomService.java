package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.entity.Company1;
import com.kbai.corporatefinance.repository.CompanyRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvCustomService {

    @Autowired
    private CompanyRepository companyRepository; // JPA Repository

    @Transactional
    public void importCsvData(String filePath) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();

            // 첫 번째 행 건너뛰기
            rows.remove(0);

            for (String[] row : rows) {
                Company1 company = Company1.builder()
                        .companyName(row[0]) // 기업 이름
                        .esg(row[1]) // ESG 등급
                        .esg_e(row[2]) // 환경
                        .esg_s(row[3]) // 사회
                        .esg_g(row[4]) // 지배구조
                        .per(parseFloatOrDefault(row[5], 0.0f))
                        .pbr(parseFloatOrDefault(row[6], 0.0f))
                        .dividendYield(parseFloatOrDefault(row[7], 0.0f))
                        .companyCode((row[8])) // 기업 코드
                        .maleContractEmployeeCount(parseIntOrDefault(row[9], 0)) // 계약직수남
                        .femaleContractEmployeeCount(parseIntOrDefault(row[10], 0)) // 계약직수여
                        .totalSalaryMale(parseLongOrDefault(row[11], 0L)) // 연봉합계남
                        .totalSalaryFemale(parseLongOrDefault(row[12], 0L)) // 연봉합계여
                        .maleRegularEmployeeCount(parseLongOrDefault(row[13], 0L)) // 정규직수남
                        .femaleRegularEmployeeCount(parseLongOrDefault(row[14], 0L)) // 정규직수여
                        .totalMaleEmployees(parseLongOrDefault(row[15], 0L)) // 남자합계
                        .totalFemaleEmployees(parseLongOrDefault(row[16], 0L)) // 여자합계
                        .averageMaleSalary(parseFloatOrDefault(row[17], 0.0f)) // 평균연봉남
                        .averageFemaleSalary(parseFloatOrDefault(row[18], 0.0f)) // 평균연봉여
                        .operatingProfitBeforeTax(parseFloatOrDefault(row[19], 0.0f)) // 세전계속사업이익률
                        .netProfitMargin(parseFloatOrDefault(row[20], 0.0f)) // 순이익률
                        .comprehensiveIncomeMargin(parseFloatOrDefault(row[21], 0.0f)) // 총포괄이익률
                        .grossProfitMargin(parseFloatOrDefault(row[22], 0.0f)) // 매출총이익률
                        .costOfGoodsSold(parseFloatOrDefault(row[23], 0.0f)) // 매출원가율
                        .roe(parseFloatOrDefault(row[24], 0.0f)) // ROE
                        .sgAndAExpenseRatio(parseFloatOrDefault(row[25], 0.0f)) // 판관비율
                        .operatingIncomeOnTotalAssets(parseFloatOrDefault(row[26], 0.0f)) // 총자산영업이익률
                        .preTaxIncomeOnTotalAssets(parseFloatOrDefault(row[27], 0.0f)) // 총자산세전계속사업이익률
                        .operatingIncomeOnEquity(parseFloatOrDefault(row[28], 0.0f)) // 자기자본영업이익률
                        .preTaxIncomeOnEquity(parseFloatOrDefault(row[29], 0.0f)) // 자기자본세전계속사업이익률
                        .operatingIncomeOnPaidInCapital(parseFloatOrDefault(row[30], 0.0f)) // 자본금영업이익률
                        .preTaxIncomeOnPaidInCapital(parseFloatOrDefault(row[31], 0.0f)) // 자본금세전계속사업이익률
                        .roi(parseFloatOrDefault(row[32], 0.0f)) // 납입자본이익률
                        .operatingExpenseRatio(parseFloatOrDefault(row[33], 0.0f)) // 영업수익경비율
                        .equityRatio(parseFloatOrDefault(row[34], 0.0f)) // 자기자본비율
                        .debtRatio(parseFloatOrDefault(row[35], 0.0f)) // 부채비율
                        .liquidityRatio(parseFloatOrDefault(row[36], 0.0f)) // 유동비율
                        .quickRatio(parseFloatOrDefault(row[37], 0.0f)) // 당좌비율
                        .currentLiabilitiesRatio(parseFloatOrDefault(row[38], 0.0f)) // 유동부채비율
                        .nonCurrentLiabilitiesRatio(parseFloatOrDefault(row[39], 0.0f)) // 비유동부채비율
                        .interestCoverageRatio(parseFloatOrDefault(row[40], 0.0f)) // 이자보상배율
                        .netInterestCoverageRatio(parseFloatOrDefault(row[41], 0.0f)) // 순이자보상배율
                        .nonCurrentAssetRatio(parseFloatOrDefault(row[42], 0.0f)) // 비유동비율
                        .financialCostBurdenRatio(parseFloatOrDefault(row[43], 0.0f)) // 금융비용부담률
                        .capitalRetentionRatio(parseFloatOrDefault(row[44], 0.0f)) // 자본유보율
                        .retentionAmountToEquityRatio(parseFloatOrDefault(row[45], 0.0f)) // 유보액대비율
                        .financialLeverage(parseFloatOrDefault(row[46], 0.0f)) // 재무레버리지
                        .nonCurrentAssetSuitabilityRatio(parseFloatOrDefault(row[47], 0.0f)) // 비유동적합률
                        .nonCurrentAssetCompositionRatio(parseFloatOrDefault(row[48], 0.0f)) // 비유동자산구성비율
                        .tangibleAssetCompositionRatio(parseFloatOrDefault(row[49], 0.0f)) // 유형자산구성비율
                        .currentAssetCompositionRatio(parseFloatOrDefault(row[50], 0.0f)) // 유동자산구성비율
                        .inventoryCompositionRatio(parseFloatOrDefault(row[51], 0.0f)) // 재고자산구성비율
                        .currentToNonCurrentAssetRatio(parseFloatOrDefault(row[52], 0.0f)) // 유동자산/비유동자산비율
                        .inventoryToCurrentAssetRatio(parseFloatOrDefault(row[53], 0.0f)) // 재고자산/유동자산비율
                        .receivablesToPayablesRatio(parseFloatOrDefault(row[54], 0.0f)) // 매출채권/매입채무비율
                        .payablesToInventoryRatio(parseFloatOrDefault(row[55], 0.0f)) // 매입채무/재고자산비율
                        .salesGrowthRateYoY(parseFloatOrDefault(row[56], 0.0f)) // 매출액증가율(YoY)
                        .grossProfitGrowthRateYoY(parseFloatOrDefault(row[57], 0.0f)) // 매출총이익증가율(YoY)
                        .operatingProfitGrowthRateYoY(parseFloatOrDefault(row[58], 0.0f)) // 영업이익증가율(YoY)
                        .preTaxProfitGrowthRateYoY(parseFloatOrDefault(row[59], 0.0f)) // 세전계속사업이익증가율(YoY)
                        .netProfitGrowthRateYoY(parseFloatOrDefault(row[60], 0.0f)) // 순이익증가율(YoY)
                        .comprehensiveIncomeGrowthRateYoY(parseFloatOrDefault(row[61], 0.0f)) // 총포괄이익증가율(YoY)
                        .totalAssetGrowthRateYoY(parseFloatOrDefault(row[62], 0.0f)) // 총자산증가율
                        .nonCurrentAssetGrowthRateYoY(parseFloatOrDefault(row[63], 0.0f)) // 비유동자산증가율
                        .tangibleAssetGrowthRateYoY(parseFloatOrDefault(row[64], 0.0f)) // 유형자산증가율
                        .totalLiabilitiesGrowthRateYoY(parseFloatOrDefault(row[65], 0.0f)) // 부채총계증가율
                        .totalBorrowingsGrowthRateYoY(parseFloatOrDefault(row[66], 0.0f)) // 총차입금증가율
                        .equityGrowthRateYoY(parseFloatOrDefault(row[67], 0.0f)) // 자기자본증가율
                        .currentAssetGrowthRateYoY(parseFloatOrDefault(row[68], 0.0f)) // 유동자산증가율
                        .receivablesGrowthRateYoY(parseFloatOrDefault(row[69], 0.0f)) // 매출채권증가율
                        .inventoryGrowthRateYoY(parseFloatOrDefault(row[70], 0.0f)) // 재고자산증가율
                        .currentLiabilitiesGrowthRateYoY(parseFloatOrDefault(row[71], 0.0f)) // 유동부채증가율
                        .payablesGrowthRateYoY(parseFloatOrDefault(row[72], 0.0f)) // 매입채무증가율
                        .nonCurrentLiabilitiesGrowthRateYoY(parseFloatOrDefault(row[73], 0.0f)) // 비유동부채증가율
                        .totalAssetTurnover(parseFloatOrDefault(row[74], 0.0f)) // 총자산회전율
                        .receivablesTurnover(parseFloatOrDefault(row[75], 0.0f)) // 매출채권회전율
                        .inventoryTurnover(parseFloatOrDefault(row[76], 0.0f)) // 재고자산회전율
                        .costOfGoodsSoldToInventory(parseFloatOrDefault(row[77], 0.0f)) // 매출원가/재고자산
                        .payablesTurnover(parseFloatOrDefault(row[78], 0.0f)) // 매입채무회전율
                        .nonCurrentAssetTurnover(parseFloatOrDefault(row[79], 0.0f)) // 비유동자산회전율
                        .tangibleAssetTurnover(parseFloatOrDefault(row[80], 0.0f)) // 유형자산회전율
                        .debtToEquityTurnover(parseFloatOrDefault(row[81], 0.0f)) // 타인자본회전율
                        .equityTurnover(parseFloatOrDefault(row[82], 0.0f)) // 자기자본회전율
                        .capitalTurnover(parseFloatOrDefault(row[83], 0.0f)) // 자본금회전율
                        .dividendPayoutRatio(parseFloatOrDefault(row[84], 0.0f)) // 배당성향(%)
                        .femaleExecutives(parseIntOrDefault(row[85], 0)) // 여성임원수
                        .maleExecutives(parseIntOrDefault(row[86], 0)) // 남성임원수
                        .status(row[87]) // 상태
                        .message(row[88]) // 메시지
                        .corpNameEng(row[89]) // 회사 이름 (영어)
                        .stockName(row[90]) // 주식명
                        .stockCode(parseLongOrDefault(row[91], 0L)) // 주식코드
                        .ceoName(row[92]) // CEO 이름
                        .corpCls(row[93]) // 회사 분류
                        .jurirNo(row[94]) // 법인등록번호
                        .bizrNo(row[95]) // 사업자등록번호
                        .address(row[96]) // 주소
                        .homeUrl(row[97]) // 홈페이지 URL
                        .irUrl(row[98]) // IR URL
                        .phoneNumber(row[99]) // 전화번호
                        .faxNumber(row[100]) // 팩스번호
                        .industryCode(row[101]) // 산업코드
                        .establishmentDate(row[102]) // 설립일자
                        .accountingMonth(row[103]) // 회계월
                        .financialCostCurrent(parseLongOrDefault(row[104], 0L)) // 금융비용_당액
                        .financialCostPrevious(parseLongOrDefault(row[105], 0L)) // 금융비용_전기
                        .financialCostPrePrevious(parseLongOrDefault(row[106], 0L)) // 금융비용_전전기
                        .cashAndCashEquivalentsCurrent(parseLongOrDefault(row[107], 0L)) // 현금및현금성자산_당액
                        .cashAndCashEquivalentsPrevious(parseLongOrDefault(row[108], 0L)) // 현금및현금성자산_전기
                        .cashAndCashEquivalentsPrePrevious(parseLongOrDefault(row[109], 0L)) // 현금및현금성자산_전전기
                        .operatingCashFlowCurrent(parseLongOrDefault(row[110], 0L)) // 영업활동현금흐름_당액
                        .operatingCashFlowPrevious(parseLongOrDefault(row[111], 0L)) // 영업활동현금흐름_전기
                        .operatingCashFlowPrePrevious(parseLongOrDefault(row[112], 0L)) // 영업활동현금흐름_전전기
                        .provisionsCurrent(parseLongOrDefault(row[113], 0L)) // 충당부채_당액
                        .provisionsPrevious(parseLongOrDefault(row[114], 0L)) // 충당부채_전기
                        .provisionsPrePrevious(parseLongOrDefault(row[115], 0L)) // 충당부채_전전기
                        .financingCashFlowCurrent(parseLongOrDefault(row[116], 0L)) // 재무활동현금흐름_당액
                        .financingCashFlowPrevious(parseLongOrDefault(row[117], 0L)) // 재무활동현금흐름_전기
                        .financingCashFlowPrePrevious(parseLongOrDefault(row[118], 0L)) // 재무활동현금흐름_전전기
                        .investingCashFlowCurrent(parseLongOrDefault(row[119], 0L)) // 투자활동현금흐름_당액
                        .investingCashFlowPrevious(parseLongOrDefault(row[120], 0L)) // 투자활동현금흐름_전기
                        .investingCashFlowPrePrevious(parseLongOrDefault(row[121], 0L)) // 투자활동현금흐름_전전기
                        .accountsReceivableCurrent(parseLongOrDefault(row[122], 0L)) // 미수금_당액
                        .accountsReceivablePrevious(parseLongOrDefault(row[123], 0L)) // 미수금_전기
                        .accountsReceivablePrePrevious(parseLongOrDefault(row[124], 0L)) // 미수금_전전기
                        .totalLiabilitiesCurrent(parseLongOrDefault(row[125], 0L)) // 부채총계_당액
                        .totalLiabilitiesPrevious(parseLongOrDefault(row[126], 0L)) // 부채총계_전기
                        .totalLiabilitiesPrePrevious(parseLongOrDefault(row[127], 0L)) // 부채총계_전전기
                        .nonCurrentLiabilitiesCurrent(parseLongOrDefault(row[128], 0L)) // 비유동부채_당액
                        .nonCurrentLiabilitiesPrevious(parseLongOrDefault(row[129], 0L)) // 비유동부채_전기
                        .nonCurrentLiabilitiesPrePrevious(parseLongOrDefault(row[130], 0L)) // 비유동부채_전전기
                        .nonCurrentAssetsCurrent(parseLongOrDefault(row[131], 0L)) // 비유동자산_당액
                        .nonCurrentAssetsPrevious(parseLongOrDefault(row[132], 0L)) // 비유동자산_전기
                        .nonCurrentAssetsPrePrevious(parseLongOrDefault(row[133], 0L)) // 비유동자산_전전기
                        .currentLiabilitiesCurrent(parseLongOrDefault(row[134], 0L)) // 유동부채_당액
                        .currentLiabilitiesPrevious(parseLongOrDefault(row[135], 0L)) // 유동부채_전기
                        .currentLiabilitiesPrePrevious(parseLongOrDefault(row[136], 0L)) // 유동부채_전전기
                        .currentAssetsCurrent(parseLongOrDefault(row[137], 0L)) // 유동자산_당액
                        .currentAssetsPrevious(parseLongOrDefault(row[138], 0L)) // 유동자산_전기
                        .currentAssetsPrePrevious(parseLongOrDefault(row[139], 0L)) // 유동자산_전전기
                        .shareCapitalCurrent(parseLongOrDefault(row[140], 0L)) // 자본금_당액
                        .shareCapitalPrevious(parseLongOrDefault(row[141], 0L)) // 자본금_전기
                        .shareCapitalPrePrevious(parseLongOrDefault(row[142], 0L)) // 자본금_전전기
                        .totalAssetsCurrent(parseLongOrDefault(row[143], 0L)) // 자산총계_당액
                        .totalAssetsPrevious(parseLongOrDefault(row[144], 0L)) // 자산총계_전기
                        .totalAssetsPrePrevious(parseLongOrDefault(row[145], 0L)) // 자산총계_전전기
                        .salesCurrent(parseLongOrDefault(row[146], 0L)) // 매출액_당액
                        .salesPrevious(parseLongOrDefault(row[147], 0L)) // 매출액_전기
                        .salesPrePrevious(parseLongOrDefault(row[148], 0L)) // 매출액_전전기
                        .operatingProfitCurrent(parseLongOrDefault(row[149], 0L)) // 영업이익_당액
                        .operatingProfitPrevious(parseLongOrDefault(row[150], 0L)) // 영업이익_전기
                        .operatingProfitPrePrevious(parseLongOrDefault(row[151], 0L)) // 영업이익_전전기
                        .accountsReceivableCurrent2(parseLongOrDefault(row[152], 0L)) // 매출채권_당액
                        .accountsReceivablePrevious2(parseLongOrDefault(row[153], 0L)) // 매출채권_전기
                        .accountsReceivablePrePrevious2(parseLongOrDefault(row[154], 0L)) // 매출채권_전전기
                        .costOfSalesCurrent(parseLongOrDefault(row[155], 0L)) // 매출원가_당액
                        .costOfSalesPrevious(parseLongOrDefault(row[156], 0L)) // 매출원가_전기
                        .costOfSalesPrePrevious(parseLongOrDefault(row[157], 0L)) // 매출원가_전전기
                        .operatingProfitPrevious(parseLongOrDefault(row[150], 0L)) // 영업이익_전기
                        .operatingProfitPrePrevious(parseLongOrDefault(row[151], 0L)) // 영업이익_전전기
                        .accountsReceivableCurrent2(parseLongOrDefault(row[152], 0L)) // 매출채권_당액
                        .accountsReceivablePrevious2(parseLongOrDefault(row[153], 0L)) // 매출채권_전기
                        .accountsReceivablePrePrevious2(parseLongOrDefault(row[154], 0L)) // 매출채권_전전기
                        .costOfSalesCurrent(parseLongOrDefault(row[155], 0L)) // 매출원가_당액
                        .costOfSalesPrevious(parseLongOrDefault(row[156], 0L)) // 매출원가_전기
                        .costOfSalesPrePrevious(parseLongOrDefault(row[157], 0L)) // 매출원가_전전기
                        .receivablesToCashRatioCurrent(parseFloatOrDefault(row[158], 0.0f)) // 미수금대현금자산비율_당액
                        .receivablesToCashRatioPrevious(parseFloatOrDefault(row[159], 0.0f)) // 미수금대현금자산비율_전기
                        .receivablesToCashRatioPrePrevious(parseFloatOrDefault(row[160], 0.0f)) // 미수금대현금자산비율_전전기
                        .financialCostToSalesRatioCurrent(parseFloatOrDefault(row[161], 0.0f)) // 금융비용대매출액비율_당액
                        .financialCostToSalesRatioPrevious(parseFloatOrDefault(row[162], 0.0f)) // 금융비용대매출액비율_전기
                        .financialCostToSalesRatioPrePrevious(parseFloatOrDefault(row[163], 0.0f)) // 금융비용대매출액비율_전전기
                        .operatingCashFlowChangeRatePrevious(parseFloatOrDefault(row[164], 0.0f)) // 영업활동현금흐름_전기_변화율
                        .operatingCashFlowChangeRateCurrent(parseFloatOrDefault(row[165], 0.0f)) // 영업활동현금흐름_당액_변화율
                        .investingCashFlowChangeRatePrevious(parseFloatOrDefault(row[166], 0.0f)) // 투자활동현금흐름_전기_변화율
                        .investingCashFlowChangeRateCurrent(parseFloatOrDefault(row[167], 0.0f)) // 투자활동현금흐름_당액_변화율
                        .financingCashFlowChangeRatePrevious(parseFloatOrDefault(row[168], 0.0f)) // 재무활동현금흐름_전기_변화율
                        .financingCashFlowChangeRateCurrent(parseFloatOrDefault(row[169], 0.0f)) // 재무활동현금흐름_당액_변화율
                        .debtRatioCurrent(parseFloatOrDefault(row[170], 0.0f)) // 부채비율_당액
                        .liquidityRatioCurrent(parseFloatOrDefault(row[171], 0.0f)) // 유동비율_당액
                        .operatingProfitMarginCurrent(parseFloatOrDefault(row[172], 0.0f)) // 영업이익률_당액
                        .financialCostToOperatingIncomeRatioCurrent(parseFloatOrDefault(row[173], 0.0f)) // 금융비용대영업이익비율_당액
                        .provisionsToEquityRatioCurrent(parseFloatOrDefault(row[174], 0.0f)) // 자본대비충당부채비율_당액
                        .debtRatioPrevious(parseFloatOrDefault(row[175], 0.0f)) // 부채비율_전기
                        .liquidityRatioPrevious(parseFloatOrDefault(row[176], 0.0f)) // 유동비율_전기
                        .operatingProfitMarginPrevious(parseFloatOrDefault(row[177], 0.0f)) // 영업이익률_전기
                        .financialCostToOperatingIncomeRatioPrevious(parseFloatOrDefault(row[178], 0.0f)) // 금융비용대영업이익비율_전기
                        .provisionsToEquityRatioPrevious(parseFloatOrDefault(row[179], 0.0f)) // 자본대비충당부채비율_전기
                        .debtRatioPrePrevious(parseFloatOrDefault(row[180], 0.0f)) // 부채비율_전전기
                        .liquidityRatioPrePrevious(parseFloatOrDefault(row[181], 0.0f)) // 유동비율_전전기
                        .operatingProfitMarginPrePrevious(parseFloatOrDefault(row[182], 0.0f)) // 영업이익률_전전기
                        .financialCostToOperatingIncomeRatioPrePrevious(parseFloatOrDefault(row[183], 0.0f)) // 금융비용대영업이익비율_전전기
                        .provisionsToEquityRatioPrePrevious(parseFloatOrDefault(row[184], 0.0f)) // 자본대비충당부채비율_전전기
                        .operatingProfitMarginChangeRatePrevious(parseFloatOrDefault(row[185], 0.0f)) // 영업이익률_전기_변화율
                        .operatingProfitMarginChangeRateCurrent(parseFloatOrDefault(row[186], 0.0f)) // 영업이익률_당액_변화율
                        .debtRatioChangeRatePrevious(parseFloatOrDefault(row[187], 0.0f)) // 부채비율_전기_변화율
                        .debtRatioChangeRateCurrent(parseFloatOrDefault(row[188], 0.0f)) // 부채비율_당액_변화율
                        .liquidityRatioChangeRatePrevious(parseFloatOrDefault(row[189], 0.0f)) // 유동비율_전기_변화율
                        .liquidityRatioChangeRateCurrent(parseFloatOrDefault(row[190], 0.0f)) // 유동비율_당액_변화율
                        .daysUntilToday(parseIntOrDefault(row[191], 0)) // 오늘까지_일수
                        .build();
                companyRepository.save(company); // 데이터베이스에 저장
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    // 기본값 처리를 위한 헬퍼 메서드들
    private float parseFloatOrDefault(String value, float defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Float.parseFloat(value);
    }

    private long parseLongOrDefault(String value, long defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Long.parseLong(value);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(value);
    }
}