package com.kbai.corporatefinance.repository;

import com.kbai.corporatefinance.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    List<Company> findByCompanyCodeIn(List<Long> companyCodes);
}
