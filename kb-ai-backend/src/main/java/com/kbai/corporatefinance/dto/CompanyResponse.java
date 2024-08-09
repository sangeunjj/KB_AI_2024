package com.kbai.corporatefinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyResponse {
    private String status;
    private String message;
    private String corp_name; // 회사명
    private String stock_code; // 종목코드
    private String ceo_nm; // 대표자명
    private String jurir_no; // 법인등록번호
    private String bizr_no; // 사업자등록번호
    private String adres; // 주소
    private String hm_url; // 홈페이지 URL
    private String ir_url; // IR 홈페이지 URL
    private String phn_no; // 전화번호
    private String fax_no; // 팩스번호
    private String induty_code; // 업종코드
    private String est_dt; // 설립일자
    private String acc_mt; // 결산월
}
