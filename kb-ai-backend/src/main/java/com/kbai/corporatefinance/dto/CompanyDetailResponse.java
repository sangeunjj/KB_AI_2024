package com.kbai.corporatefinance.dto;

import com.kbai.corporatefinance.entity.Company1;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyDetailResponse {
    private Company1 company1;
    private DartResponse dartResponse;
}

