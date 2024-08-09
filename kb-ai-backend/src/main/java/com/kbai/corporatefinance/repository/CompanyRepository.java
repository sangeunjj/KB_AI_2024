package com.kbai.corporatefinance.repository;

import com.kbai.corporatefinance.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}
