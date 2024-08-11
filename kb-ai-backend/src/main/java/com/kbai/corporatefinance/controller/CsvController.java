package com.kbai.corporatefinance.controller;

import com.kbai.corporatefinance.service.CsvCustomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CsvController {

    private final CsvCustomService csvCustomService;

    @PostMapping("/import-csv")
    public String importCsv(@RequestParam(defaultValue = "src/main/resources/company_data_final_3.csv") String filePath) {
        csvCustomService.importCsvData(filePath);
        return "CSV data imported successfully!";
    }
}
