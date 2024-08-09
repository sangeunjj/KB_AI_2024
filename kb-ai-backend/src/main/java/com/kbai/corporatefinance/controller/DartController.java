package com.kbai.corporatefinance.controller;

import com.kbai.corporatefinance.dto.CompanyResponse;
import com.kbai.corporatefinance.service.DartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class DartController {

    @Value("${dart.api.key}")
    private String dartApiKey;

    @Autowired
    private DartService dartService;

    @GetMapping(value = "/company/{corpCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CompanyResponse> getCompanyByCode(@PathVariable String corpCode) {
        CompanyResponse company = dartService.getCompanyInfo(corpCode, dartApiKey);
        return ResponseEntity.ok(company);
    }
}
