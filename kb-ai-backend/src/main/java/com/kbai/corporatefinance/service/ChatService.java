package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.dto.ChatGPTRequest;
import com.kbai.corporatefinance.entity.Company1;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final CompanyService companyService;

    @Value("${openai.model}")
    private String model;

    public ChatGPTRequest createPrompt(String prompt) {
        // 1. DB의 값을 모두 가져와서 프롬프트에 추가
        List<Company1> companies = companyService.getAllCompanies();
        StringBuilder enrichedPrompt = new StringBuilder();

        int limit = Math.min(companies.size(), 10);
        for (Company1 company1 : companies.subList(0, limit)) {
            enrichedPrompt.append("요즘 기업 뉴스 요약: ").append(company1.getSumm()).append("\n");
        }

//            enrichedPrompt.append("요즘 기업 뉴스 요약: ").append(company1.getSumm()).append("\n");
////            enrichedPrompt.append("회사명: ").append(company1.getCompanyName()).append("\n")
////                    .append("ESG: ").append(company1.getEsg()).append("\n");
////                    .append("베타계수: ").append(company.getBetaCoefficient()).append("\n")
////                    .append("여성 임원수: ").append(company1.getFemaleExecutives()).append("\n")
////                    .append("정규직 유무: ").append(company1.getRegularEmployeeCount() == 1 ? "정규직 있음" : "정규직 없음").append("\n")
////                    .append("성별: ").append(Objects.equals(company1.getGender(), "남") ? "남자" : "여자").append("\n");
////                    .append("뉴스 요약: ").append(company.g()).append("\n")
////                    .append("긍정/부정 점수: ").append(company.getSentimentScore()).append("\n")
////                    .append("2주간 기사 개수: ").append(company.getTwoWeeksArticleCount()).append("\n\n");
//        }


        // 2. 사용자가 입력한 프롬프트 추가
        enrichedPrompt.append(prompt).append("\n\n");

        // 3. 예측 관련 요청
        if (prompt.contains("예측") || prompt.contains("전망") || prompt.contains("추정") ||
                prompt.contains("미래 분석") || prompt.contains("추세 분석") || prompt.contains("시나리오 예측")) {

            enrichedPrompt.append("## 예측\n\n")
                    .append("다음 항목에 대한 예측을 제시해 주세요:\n")
                    .append("- 기업 또는 시장의 미래 전망\n")
                    .append("- 예상되는 트렌드 변화\n")
                    .append("- 추정되는 성장 가능성\n");

        }

        // 4. 추천 관련 요청
        if (prompt.contains("추천") || prompt.contains("조언") || prompt.contains("제안") ||
                prompt.contains("권장") || prompt.contains("권유") || prompt.contains("전략 추천") ||
                prompt.contains("상품 추천") || prompt.contains("행동 강령 제안")) {

            enrichedPrompt.append("## 추천\n\n")
                    .append("해당 상황에 적합한 전략이나 조언을 제안해 주세요:\n")
                    .append("- 현재 상황에서 가장 적절한 조치\n")
                    .append("- 성공 가능성을 높이는 전략\n")
                    .append("- 최적의 제품 또는 서비스 추천\n");

        }

        // 5. 리스크 평가 관련 요청
        if (prompt.contains("리스크") || prompt.contains("위험성") || prompt.contains("취약점 분석") ||
                prompt.contains("위협 분석") || prompt.contains("리스크 관리") || prompt.contains("위기 대응 전략")) {

            enrichedPrompt.append("## 리스크 평가\n\n")
                    .append("해당 기업이나 시장에 존재하는 리스크를 평가하고, 다음 사항을 설명해 주세요:\n")
                    .append("- 주요 리스크 요소\n")
                    .append("- 리스크를 관리하거나 줄일 수 있는 방법\n")
                    .append("- 예상되는 위협과 대응 전략\n");

            // 결론 추가
            enrichedPrompt.append("## 결론\n\n")
                    .append("리스크 평가를 종합한 결론을 제시해 주세요.\n");
        }

        // 6. 성장성 평가 관련 요청
        if (prompt.contains("성장성") || prompt.contains("성장 가능성") || prompt.contains("확장성") ||
                prompt.contains("확장 가능성") || prompt.contains("성장 전략") ||
                prompt.contains("시장 확장") || prompt.contains("기업 성장성 분석") || prompt.contains("성장성 전망")) {

            enrichedPrompt.append("## 성장성 평가\n\n")
                    .append("해당 기업이나 시장의 성장 가능성을 평가해 주세요:\n")
                    .append("- 성장성을 높일 수 있는 주요 요인\n")
                    .append("- 현재와 미래의 성장 가능성\n")
                    .append("- 확장성 있는 전략 제안\n");

            // 결론 추가
            enrichedPrompt.append("## 결론\n\n")
                    .append("성장성 평가를 종합한 결론을 제시해 주세요.\n");
        }

        // 7. 트렌드 분석 관련 요청
        if (prompt.contains("트렌드") || prompt.contains("경향") || prompt.contains("추세") ||
                prompt.contains("유행") || prompt.contains("시장 동향") ||
                prompt.contains("소비자 트렌드") || prompt.contains("최신 트렌드 분석")) {

            enrichedPrompt.append("## 트렌드 분석\n\n")
                    .append("현재와 미래의 트렌드를 분석해 주세요:\n")
                    .append("- 시장에서 관찰되는 주요 경향\n")
                    .append("- 예상되는 트렌드 변화\n")
                    .append("- 소비자 행동과 관련된 트렌드\n");

        }

        // 8. SWOT 분석 관련 요청
        if (prompt.contains("SWOT") || prompt.contains("강점 분석") || prompt.contains("약점 분석") ||
                prompt.contains("기회 분석") || prompt.contains("위협 분석") ||
                prompt.contains("강점과 약점") || prompt.contains("기회와 위협") || prompt.contains("SWOT 전략")) {

            enrichedPrompt.append("## SWOT 분석\n\n")
                    .append("해당 기업의 SWOT 분석을 제시해 주세요:\n")
                    .append("- 강점 (Strengths)\n")
                    .append("- 약점 (Weaknesses)\n")
                    .append("- 기회 (Opportunities)\n")
                    .append("- 위협 (Threats)\n");

        }

        // 재무재표 관련
        if (prompt.contains("활동성 지표")) {
            enrichedPrompt.append("내가 같이 보낸 데이터 중에 ['총자산회전율', '매출채권회전율', '재고자산회전율', '매출원가/재고자산', '매입채무회전율', '비유동자산회전율',\n" +
                    "'유형자산회전율', '타인자본회전율', '자기자본회전율', '자본금회전율', '배당성향(%)' 컬럼을 참고해서 설명해줘 \n");
            enrichedPrompt.append("컬럼들(지표들)이 가지는 각각 의미(해석)을 함께 포함해줘.");
        }
        if (prompt.contains("성장성 지표")) {
            enrichedPrompt.append("내가 같이 보낸 데이터 중에 ['매출액증가율(YoY)', '매출총이익증가율(YoY)', '영업이익증가율(YoY)', '세전계속사업이익증가율(YoY)',\n" +
                    "       '순이익증가율(YoY)', '총포괄이익증가율(YoY)', '총자산증가율', '비유동자산증가율', '유형자산증가율',\n" +
                    "       '부채총계증가율', '총차입금증가율', '자기자본증가율', '유동자산증가율', '매출채권증가율', '재고자산증가율',\n" +
                    "       '유동부채증가율', '매입채무증가율', '비유동부채증가율'] 컬럼을 참고해서 설명해줘 \n");
            enrichedPrompt.append("컬럼들(지표들)이 가지는 각각 의미(해석)을 함께 포함해줘.");
        }
        if (prompt.contains("안정성 지표")) {
            enrichedPrompt.append("내가 같이 보낸 데이터 중에 ['매출액증가율(YoY)', '매출총이익증가율(YoY)', '영업이익증가율(YoY)', '세전계속사업이익증가율(YoY)',\n" +
                    "       '순이익증가율(YoY)', '총포괄이익증가율(YoY)', '총자산증가율', '비유동자산증가율', '유형자산증가율',\n" +
                    "       '부채총계증가율', '총차입금증가율', '자기자본증가율', '유동자산증가율', '매출채권증가율', '재고자산증가율',\n" +
                    "       '유동부채증가율', '매입채무증가율', '비유동부채증가율'] 컬럼을 참고해서 설명해줘 \n");
            enrichedPrompt.append("컬럼들(지표들)이 가지는 각각 의미(해석)을 함께 포함해줘.");
        }
        if (prompt.contains("수익성 지표")) {
            enrichedPrompt.append("내가 같이 보낸 데이터 중에  ['세전계속사업이익률', '순이익률', '총포괄이익률', '매출총이익률', '매출원가율', 'ROE', '판관비율',\n" +
                    "       '총자산영업이익률', '총자산세전계속사업이익률', '자기자본영업이익률', '자기자본세전계속사업이익률', '자본금영업이익률',\n" +
                    "       '자본금세전계속사업이익률', '납입자본이익률', '영업수익경비율'] 컬럼을 참고해서 설명해줘 \n");
            enrichedPrompt.append("컬럼들(지표들)이 가지는 각각 의미(해석)을 함께 포함해줘.");
        }

        // 모든 케이스에서 내용이 많은 경우, 목차를 추가하도록 유도
        enrichedPrompt.append("\n\n답변 내용이 다양하고, 5줄 이상인 경우에만, 번호와 특수문자를 사용하여 목차를 만들어 설명해 주세요.\n");
        enrichedPrompt.append("\n\n짧은 답변은 번호와 특수문자를 사용한 목차를 붙일 필요가 없어.\n");


        // 최종 프롬프트 생성
        return new ChatGPTRequest(model, enrichedPrompt.toString());
    }

    public ChatGPTRequest createAlarmPrompt() {
        // 1. DB의 값을 모두 가져와서 프롬프트에 추가
        List<Company1> companies = companyService.getAllCompanies();
        StringBuilder enrichedPrompt = new StringBuilder();

//        for (Company1 company1 : companies) {
//            enrichedPrompt.append("회사명: ").append(company1.getCompanyName()).append("\n")
//                    .append("요즘 기업 뉴스 요약: ").append(company1.getSumm()).append("\n");
//                    .append("ESG: ").append(company1.getEsg()).append("\n")
//                    .append("company_code: ").append(company1.getCompanyCode()).append("\n")
//                    .append("male_contract_employee_count: ").append(company1.getMaleContractEmployeeCount()).append("\n")
//                    .append("female_contract_employee_count: ").append(company1.getFemaleContractEmployeeCount()).append("\n")
//                    .append("total_salary_male: ").append(company1.getTotalSalaryMale()).append("\n")
//                    .append("total_salary_female: ").append(company1.getTotalSalaryFemale()).append("\n")
//                    .append("male_regular_employee_count: ").append(company1.getMaleRegularEmployeeCount()).append("\n")
//                    .append("female_regular_employee_count: ").append(company1.getFemaleRegularEmployeeCount()).append("\n")
//                    .append("total_male_employees: ").append(company1.getTotalMaleEmployees()).append("\n")
//                    .append("total_female_employees: ").append(company1.getTotalFemaleEmployees()).append("\n")
//                    .append("average_male_salary: ").append(company1.getAverageMaleSalary()).append("\n")
//                    .append("average_female_salary: ").append(company1.getAverageFemaleSalary()).append("\n");
//        }

        enrichedPrompt.append("이 데이터를 바탕으로, 두 가지 질문을 작성해 주세요. 각 질문은 번호(1., 2.)를 붙여서 작성해 주세요.\n\n");

        // 1번 질문: 재무적 요소를 다루는 질문
        enrichedPrompt.append("1. 00 기업의 재무제표를 분석하여 재무 건전성을 평가하는 질문을 작성해주세요. \n");
        enrichedPrompt.append("첫째, 현금흐름이 어떻게 발생하고 있나요?\n");
        enrichedPrompt.append("둘째, 우발채무나 미수금이 발생할 가능성에 비해 현금성 자산이 충분한가요?\n");
        enrichedPrompt.append("셋째, 영업이익률이나 부채비율 같은 주요 비율 지표가 최근 몇 년 동안 어떻게 변동해 왔나요?\n");

        // 2번 질문: 비재무적 요소를 다루는 질문
        enrichedPrompt.append("2. 00 기업의 비재무적 요소를 평가하여 기업의 비재무적 건전성을 평가할 수 있는 질문을 작성해주세요.\n");
        enrichedPrompt.append("첫째, 이 기업의 ESG 등급이 업계 평균과 비교하여 어떤가요?\n");
        enrichedPrompt.append("둘째, 이 기업의 여성 임원 수가 업계 평균과 비교하여 어느 수준인가요?\n");
        enrichedPrompt.append("셋째, 최근 뉴스에서 다루어진 이슈들이 이 기업의 대외 이미지에 어떤 영향을 미칠 수 있나요?\n");

        // 최종 프롬프트 생성
        return new ChatGPTRequest(model, enrichedPrompt.toString());
    }

    public ChatGPTRequest createReportPrompt(List<String> companyNames, List<String> selectedFeatures, List<Map<String, Object>> companyDataList) {
        StringBuilder reportPrompt = new StringBuilder();

        // 보고서 서문
        reportPrompt.append("### 기업 비교 분석 보고서\n\n");

        // 피처에 따른 보고서 섹션 생성
        for (String feature : selectedFeatures) {
            switch (feature) {
                case "3개년 현금 흐름 분석":
                    reportPrompt.append("### 1. **현금흐름 분석**\n\n")
                            .append("**목표:** 여러 기업의 현금흐름 상황을 비교하여 현금 생성 능력과 운영 효율성을 평가합니다.\n\n")
                            .append("### **주요 지표 및 데이터**\n\n");

                    for (int i = 0; i < companyNames.size(); i++) {
                        reportPrompt.append("**").append(companyNames.get(i)).append("**:\n")
                                .append("    - **영업활동으로 인한 현금흐름 (Operating Cash Flow):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("operatingCashFlow.prePrevious")).append(" 원\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("operatingCashFlow.previous")).append(" 원\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("operatingCashFlow.current")).append(" 원\n\n");
                    }

                    reportPrompt.append("    - **해석:** 영업활동으로 인한 현금흐름의 변화는 각 기업의 운영 효율성 변동을 나타내며, 기업별 비교 분석을 통해 주요 원인과 개선 필요성을 도출할 수 있습니다.\n\n");
                    break;

                case "재무 건전성 및 유동성 분석":
                    reportPrompt.append("### 2. **재무 건전성 및 유동성 분석**\n\n")
                            .append("**목표:** 여러 기업의 재무 건전성과 유동성을 비교하여 기업의 단기 및 장기 생존 가능성을 평가합니다.\n\n")
                            .append("### **주요 지표 및 데이터**\n\n");

                    for (int i = 0; i < companyNames.size(); i++) {
                        reportPrompt.append("**").append(companyNames.get(i)).append("**:\n")
                                .append("    - **현금 및 현금성 자산:**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("cashAndCashEquivalents.prePrevious")).append(" 원\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("cashAndCashEquivalents.previous")).append(" 원\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("cashAndCashEquivalents.current")).append(" 원\n\n");
                    }

                    reportPrompt.append("    - **해석:** 기업별 현금성 자산의 비교를 통해 각 기업의 유동성 확보 상황을 평가할 수 있으며, 특정 기업의 유동성 위험 요소를 식별할 수 있습니다.\n\n");
                    break;

                case "재무 건전성 및 수익성 지표 분석":
                    reportPrompt.append("### 3. **재무 건전성 및 수익성 지표 분석**\n\n")
                            .append("**목표:** 여러 기업의 주요 비율 지표를 비교하여 재무 건전성과 수익성을 평가합니다.\n\n")
                            .append("### **주요 지표 및 데이터**\n\n");

                    for (int i = 0; i < companyNames.size(); i++) {
                        reportPrompt.append("**").append(companyNames.get(i)).append("**:\n")
                                .append("    - **영업이익률 (Operating Profit Margin):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("operatingProfitMargin.prePrevious")).append("%\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("operatingProfitMargin.previous")).append("%\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("operatingProfitMargin.current")).append("%\n\n")
                                .append("    - **부채비율 (Debt Ratio):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("debtRatio.prePrevious")).append("%\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("debtRatio.previous")).append("%\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("debtRatio.current")).append("%\n\n");
                    }

                    reportPrompt.append("    - **해석:** 영업이익률과 부채비율의 비교를 통해 각 기업의 수익성과 재무 건전성 간의 상관관계를 분석할 수 있습니다.\n\n");
                    break;

                case "활동성 지표":
                    reportPrompt.append("### 4. **활동성 지표 분석**\n\n")
                            .append("**목표:** 여러 기업의 자산 활용 효율성을 비교하여 경영 효율성을 평가합니다.\n\n")
                            .append("### **주요 지표 및 데이터**\n\n");

                    for (int i = 0; i < companyNames.size(); i++) {
                        reportPrompt.append("**").append(companyNames.get(i)).append("**:\n")
                                .append("    - **총자산회전율 (Total Asset Turnover):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("totalAssetTurnover.prePrevious")).append("\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("totalAssetTurnover.previous")).append("\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("totalAssetTurnover.current")).append("\n\n");
                    }

                    reportPrompt.append("    - **해석:** 총자산회전율의 비교를 통해 각 기업의 자산 운영 효율성을 분석할 수 있으며, 경영 효율성 개선이 필요한 기업을 식별할 수 있습니다.\n\n");
                    break;

                case "성장성 지표":
                    reportPrompt.append("### 5. **성장성 지표 분석**\n\n")
                            .append("**목표:** 여러 기업의 매출 및 이익 성장성을 비교하여 장기적인 성장 가능성을 평가합니다.\n\n")
                            .append("### **주요 지표 및 데이터**\n\n");

                    for (int i = 0; i < companyNames.size(); i++) {
                        reportPrompt.append("**").append(companyNames.get(i)).append("**:\n")
                                .append("    - **매출액증가율 (Sales Growth Rate):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("salesGrowthRate.prePrevious")).append("%\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("salesGrowthRate.previous")).append("%\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("salesGrowthRate.current")).append("%\n\n");
                    }

                    reportPrompt.append("    - **해석:** 매출액증가율을 비교하여 각 기업의 성장성을 평가하고, 시장에서의 경쟁 우위를 가진 기업을 도출할 수 있습니다.\n\n");
                    break;

                case "안정성 지표":
                    reportPrompt.append("### 6. **안정성 지표 분석**\n\n")
                            .append("**목표:** 여러 기업의 재무 구조의 안정성을 비교하여 장기적인 생존 가능성을 평가합니다.\n\n")
                            .append("### **주요 지표 및 데이터**\n\n");

                    for (int i = 0; i < companyNames.size(); i++) {
                        reportPrompt.append("**").append(companyNames.get(i)).append("**:\n")
                                .append("    - **자기자본비율 (Equity Ratio):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("equityRatio.prePrevious")).append("%\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("equityRatio.previous")).append("%\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("equityRatio.current")).append("%\n\n")
                                .append("    - **부채비율 (Debt Ratio):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("debtRatio.prePrevious")).append("%\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("debtRatio.previous")).append("%\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("debtRatio.current")).append("%\n\n");
                    }

                    reportPrompt.append("    - **해석:** 자기자본비율과 부채비율의 비교를 통해 각 기업의 재무 구조 안정성을 분석하고, 재무 위험이 높은 기업을 식별할 수 있습니다.\n\n");
                    break;

                case "수익성 지표":
                    reportPrompt.append("### 7. **수익성 지표 분석**\n\n")
                            .append("**목표:** 여러 기업의 수익 창출 능력을 비교하여 경영 효율성을 평가합니다.\n\n")
                            .append("### **주요 지표 및 데이터**\n\n");

                    for (int i = 0; i < companyNames.size(); i++) {
                        reportPrompt.append("**").append(companyNames.get(i)).append("**:\n")
                                .append("    - **순이익률 (Net Profit Margin):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("netProfitMargin.prePrevious")).append("%\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("netProfitMargin.previous")).append("%\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("netProfitMargin.current")).append("%\n\n")
                                .append("    - **매출총이익률 (Gross Profit Margin):**\n")
                                .append("        - **2021년:** ").append(companyDataList.get(i).get("grossProfitMargin.prePrevious")).append("%\n")
                                .append("        - **2022년:** ").append(companyDataList.get(i).get("grossProfitMargin.previous")).append("%\n")
                                .append("        - **2023년:** ").append(companyDataList.get(i).get("grossProfitMargin.current")).append("%\n\n");
                    }

                    reportPrompt.append("    - **해석:** 수익성 지표를 비교하여 각 기업의 경영 효율성을 평가하고, 수익성 개선이 필요한 기업을 식별할 수 있습니다.\n\n");
                    break;

                case "ESG":
                    reportPrompt.append("### 8. **환경, 사회, 지배구조 (ESG) 분석**\n\n")
                            .append("**목표:** 여러 기업의 ESG(환경, 사회, 지배구조) 성과를 비교하여 지속 가능성을 평가합니다.\n\n")
                            .append("### **주요 지표 및 데이터**\n\n");

                    for (int i = 0; i < companyNames.size(); i++) {
                        reportPrompt.append("**").append(companyNames.get(i)).append("**:\n")
                                .append("    - **환경 (Environmental):**\n")
                                .append("        - **점수:** ").append(companyDataList.get(i).get("ESG_23_e")).append("\n")
                                .append("    - **사회 (Social):**\n")
                                .append("        - **점수:** ").append(companyDataList.get(i).get("ESG_23_s")).append("\n")
                                .append("    - **지배구조 (Governance):**\n")
                                .append("        - **점수:** ").append(companyDataList.get(i).get("ESG_23_g")).append("\n\n");
                    }

                    reportPrompt.append("    - **해석:** ESG 성과를 비교하여 각 기업의 지속 가능성을 평가하고, 개선이 필요한 영역을 식별할 수 있습니다.\n\n");
                    break;

                default:
                    reportPrompt.append("### ").append(feature).append(" 분석 섹션이 아직 구현되지 않았습니다.\n\n");
                    break;
            }
        }

        // 보고서 종합 해석 추가
        reportPrompt.append("### **종합 해석:**\n\n")
                .append("선택한 기업들의 데이터를 비교 분석한 결과, 각 피처에서 나타난 주요 결과를 종합하여 기업의 현재 상태를 평가할 수 있습니다. 추가적인 데이터 분석이나 시뮬레이션이 필요하다면 말씀해 주세요.\n");

        // 최종 프롬프트 생성
        return new ChatGPTRequest(model, reportPrompt.toString());
    }
}
