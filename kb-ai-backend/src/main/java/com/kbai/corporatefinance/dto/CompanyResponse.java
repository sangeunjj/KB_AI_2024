package com.kbai.corporatefinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyResponse {
    private String corp_name; // 기업명
//    private int femaleExecutives; // 여성임원수
    private String esg; // ESG
//    private float sentimentScore; // 긍정/부정 점수 (산업현황)
////    private Long companyCode;
////    private String adres; // 주소
////    private String est_dt; // 설립일자
////    private String ceo_nm; // 대표자명
}

//        1. 기업명 -> db에서
//        5. 여성임원 -> db에서
//        6. ESG 지표 -> db에서
//        7. 산업현황 -> db에서 sentimentScore의 값
//        2. 지역 -> dart api 호출해서
//        3. 설립일자 -> dart api 호출해서
//        4. 대표자 -> dart api 호출해서
//        8. 대표자 신용점수 연계보기↗️ 라는 버튼 (데이터에서 가져오는게 아니라, 그냥 버튼이 있었으면 좋겠어)