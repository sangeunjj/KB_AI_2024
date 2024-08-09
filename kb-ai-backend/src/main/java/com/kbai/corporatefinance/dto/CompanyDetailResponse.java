package com.kbai.corporatefinance.dto;

import com.kbai.corporatefinance.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyDetailResponse {
    private Company company;
    private DartResponse dartResponse;
}

