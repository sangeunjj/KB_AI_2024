package com.kbai.corporatefinance.service;


import com.kbai.corporatefinance.dto.OperatingCashFlowDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OperatingCashFlowService {

    // 외부 API를 호출하거나 데이터를 생성하는 로직
    public List<OperatingCashFlowDTO> getOperatingCashFlows(List<Long> companyIds) {
        List<OperatingCashFlowDTO> cashFlowList = new ArrayList<>();

        for (Long companyId : companyIds) {
            OperatingCashFlowDTO dto = new OperatingCashFlowDTO();
            // 예시 데이터, 실제로는 외부 API 호출 등을 통해 데이터를 가져옴
            dto.setOperatingCashFlowCurrent(1000000L + companyId); // 임의의 데이터
            dto.setOperatingCashFlowPrevious(950000L + companyId);
            dto.setOperatingCashFlowPrePrevious(900000L + companyId);
            cashFlowList.add(dto);
        }
        return cashFlowList;
    }
}
