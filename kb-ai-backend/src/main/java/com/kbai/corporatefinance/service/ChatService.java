package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.dto.ChatGPTRequest;
import com.kbai.corporatefinance.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final CompanyService companyService;

    @Value("${openai.model}")
    private String model;

    public ChatGPTRequest createPrompt(String prompt) {
        // 1. DB의 값을 모두 가져와서 프롬프트에 추가
        List<Company> companies = companyService.getAllCompanies();
        StringBuilder enrichedPrompt = new StringBuilder();

        for (Company company : companies) {
            enrichedPrompt.append("회사명: ").append(company.getCompanyName()).append("\n")
                    .append("ESG: ").append(company.getEsg()).append("\n")
                    .append("베타계수: ").append(company.getBetaCoefficient()).append("\n")
                    .append("여성 임원수: ").append(company.getFemaleExecutives()).append("\n")
                    .append("정규직 유무: ").append(company.getHasRegularEmployees() == 1 ? "정규직 있음" : "정규직 없음").append("\n")
                    .append("성별: ").append(company.getGender() == 1 ? "남자" : "여자").append("\n")
                    .append("뉴스 요약: ").append(company.getNewsSummary()).append("\n")
                    .append("긍정/부정 점수: ").append(company.getSentimentScore()).append("\n")
                    .append("2주간 기사 개수: ").append(company.getTwoWeeksArticleCount()).append("\n\n");
        }

        enrichedPrompt.append("이러한 데이터들을 참고해서 답변해줘. 없으면 너가 추가해서 답변해줘.\n\n");

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

        // 모든 케이스에서 내용이 많은 경우, 목차를 추가하도록 유도
        enrichedPrompt.append("\n\n내용이 많을 경우 번호와 특수문자를 사용하여 목차를 만들어 설명해 주세요.\n");

        // 최종 프롬프트 생성
        return new ChatGPTRequest(model, enrichedPrompt.toString());
    }


    public ChatGPTRequest createAlarmPrompt() {
        // 1. DB의 값을 모두 가져와서 프롬프트에 추가
        List<Company> companies = companyService.getAllCompanies();
        StringBuilder enrichedPrompt = new StringBuilder();

        for (Company company : companies) {
            enrichedPrompt.append("회사명: ").append(company.getCompanyName()).append("\n")
                    .append("ESG: ").append(company.getEsg()).append("\n")
                    .append("베타계수: ").append(company.getBetaCoefficient()).append("\n")
                    .append("여성 임원수: ").append(company.getFemaleExecutives()).append("\n")
                    .append("정규직 유무: ").append(company.getHasRegularEmployees() == 1 ? "정규직 있음" : "정규직 없음").append("\n")
                    .append("성별: ").append(company.getGender() == 1 ? "남자" : "여자").append("\n")
                    .append("뉴스 요약: ").append(company.getNewsSummary()).append("\n")
                    .append("긍정/부정 점수: ").append(company.getSentimentScore()).append("\n")
                    .append("2주간 기사 개수: ").append(company.getTwoWeeksArticleCount()).append("\n\n");
        }

        enrichedPrompt.append("이 데이터를 바탕으로, 두 가지 질문을 작성해 주세요. 각 질문은 번호(1., 2.)를 붙여서 작성해 주세요.\n\n");

        // 1번 질문: 재무적 요소를 다루는 질문
        enrichedPrompt.append("1. 00 기업의 재무제표를 분석하여 다음 사항을 물어보는 통합 질문을 작성해주세요.\n");
        enrichedPrompt.append("첫째, 현금흐름이 어떻게 발생하고 있나요?\n");
        enrichedPrompt.append("둘째, 우발채무나 미수금이 발생할 가능성에 비해 현금성 자산이 충분한가요?\n");
        enrichedPrompt.append("셋째, 영업이익률이나 부채비율 같은 주요 비율 지표가 최근 몇 년 동안 어떻게 변동해 왔나요?\n");
        enrichedPrompt.append("위 질문들이 모두 포함된, 기업의 재무 건전성을 평가할 수 있는 질문을 작성해주세요\n\n");

        // 2번 질문: 비재무적 요소를 다루는 질문
        enrichedPrompt.append("2. 00 기업의 비재무적 요소를 평가하여 다음 사항을 물어보는 통합 질문을 작성해주세요.\n");
        enrichedPrompt.append("첫째, 이 기업의 ESG 등급이 업계 평균과 비교하여 어떤가요?\n");
        enrichedPrompt.append("둘째, 이 기업의 여성 임원 수가 업계 평균과 비교하여 어느 수준인가요?\n");
        enrichedPrompt.append("셋째, 최근 뉴스에서 다루어진 이슈들이 이 기업의 대외 이미지에 어떤 영향을 미칠 수 있나요?\n");
        enrichedPrompt.append("위 사항들을 종합적으로 고려된, 기업의 비재무적 건전성을 평가할 수 있는 질문을 작성해주세요\n\n");

        // 최종 프롬프트 생성
        return new ChatGPTRequest(model, enrichedPrompt.toString());
    }
}
