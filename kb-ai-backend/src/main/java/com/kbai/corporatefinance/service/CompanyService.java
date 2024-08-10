package com.kbai.corporatefinance.service;

import com.kbai.corporatefinance.entity.Company;
import com.kbai.corporatefinance.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    public Company getCompanyByCode(Long companyCode) {
        return companyRepository.findById(companyCode)
                .orElseThrow(() -> new RuntimeException("기업코드에 맞는 회사를 찾을 수 없습니다."));
    }

    // 모든 회사를 가져오는 메서드 추가
    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    // 회사 이름 리스트에 따라 회사 엔티티를 가져오는 메서드

    // 회사 코드 리스트에 따라 회사 엔티티를 가져오는 메서드
    public List<Company> getCompaniesByCodes(List<Long> companyCodes) {
        return companyRepository.findByCompanyCodeIn(companyCodes);
    }
}