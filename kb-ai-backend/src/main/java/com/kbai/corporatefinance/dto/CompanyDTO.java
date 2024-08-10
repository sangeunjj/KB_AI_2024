package com.kbai.corporatefinance.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CompanyDTO {
    private String companyName;
    private Long companyCode;
}
