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