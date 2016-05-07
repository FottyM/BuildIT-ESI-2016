package com.buildit.hire.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.hire.application.dto.PlantHireRequestDTO;
import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestID;
import com.buildit.hire.domain.repository.PlantHireRequestRepository;
import com.buildit.hire.rest.PlantHireRequestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PlantHireRequestAssembler extends ResourceAssemblerSupport<PlantHireRequest, PlantHireRequestDTO> {

    @Autowired
    PlantHireRequestRepository phrRepository;

    public PlantHireRequestAssembler() {
        super(PlantHireRequestController.class, PlantHireRequestDTO.class);
    }


    @Override
    public PlantHireRequestDTO toResource(PlantHireRequest plantHireRequest) {
        PlantHireRequestDTO dto = createResourceWithId(plantHireRequest.getId().getId(), plantHireRequest);
        dto.set_id(plantHireRequest.getId().getId());
        dto.setConstructionSiteId(plantHireRequest.getConstructionSiteId());
        dto.setSupplier(plantHireRequest.getSupplier());
        dto.setPrice(plantHireRequest.getPrice());
        dto.setRentalPeriod(BusinessPeriodDTO.of(plantHireRequest.getRentalPeriod().getStartDate(), plantHireRequest.getRentalPeriod().getEndDate()));
        dto.setStatus(plantHireRequest.getStatus());
        dto.setIsPaid(plantHireRequest.getIsPaid());
        dto.setPoUrl(plantHireRequest.getPoUrl());
        return dto;
    }

    public PlantHireRequestDTO toResource(PlantHireRequestID plantInfo) {
        return toResource(phrRepository.getOne(plantInfo));
    }
}
