package com.kbai.corporatefinance.dto;

import lombok.Data;

@Data
public class OperatingCashFlowDTO {
    private Long operatingCashFlowCurrent; // 영업활동현금흐름_당액
    private Long operatingCashFlowPrevious; // 영업활동현금흐름_전기
    private Long operatingCashFlowPrePrevious; // 영업활동현금흐름_전전기
}