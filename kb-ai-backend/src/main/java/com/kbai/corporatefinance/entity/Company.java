package com.kbai.corporatefinance.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "company")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자를 통한 무분별한 객체 생성을 방지
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyCode; // 기업코드

    private String companyName; // 기업명
    private String esg; // ESG
    private float betaCoefficient; // 베타계수
    private int femaleExecutives; // 여성임원수
    private int hasRegularEmployees; // 정규직 유무, 정규직은 1번, 정규직x는 0번
    private int gender; // 성별, 남자는 1번, 여자는 2번

//    private float cashFlowReport;
    // TODO 사업보고서(손익계산서), 사업보고서(재무상태표), 사업보고서(현금흐름표) 열 쭉 붙여서 보여주기

    private String newsSummary; // 뉴스요약
    private float sentimentScore; // 긍정/부정 점수 (산업현황)
    private int twoWeeksArticleCount; // 2주정도 기사 개수
}

