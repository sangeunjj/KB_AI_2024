package com.kbai.corporatefinance.repository;

import com.kbai.corporatefinance.entity.Company1;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company1, Long> {
    List<Company1> findByCompanyCodeIn(List<Long> companyCodes);
}
