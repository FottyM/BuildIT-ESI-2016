package com.buildit.common.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.domain.model.BusinessPeriod;
import org.springframework.stereotype.Service;

/**
 * Created by rain on 30.03.16.
 */
@Service
public class BusinessPeriodAssembler {

    public BusinessPeriodDTO toResource(BusinessPeriod bp){
        BusinessPeriodDTO bpDTO = new BusinessPeriodDTO();
        bpDTO.setStartDate(bp.getStartDate());
        bpDTO.setEndDate(bp.getEndDate());

        return bpDTO;
    }
}
