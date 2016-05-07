package com.buildit.sales.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.application.exceptions.PlantNotFoundException;
import com.buildit.sales.application.dto.PurchaseOrderDTO;
import com.buildit.sales.domain.model.PurchaseOrder;
import com.buildit.sales.domain.model.PurchaseOrderExtension;
import com.buildit.sales.domain.model.PurchaseOrderExtensionID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpMethod.*;

@Service
public class PurchaseOrderAssembler extends ResourceAssemblerSupport<PurchaseOrder, PurchaseOrderDTO> {

    public PurchaseOrderAssembler() {
        super(PurchaseOrderRestController.class, PurchaseOrderDTO.class);
    }

    @Override
    public PurchaseOrderDTO toResource(PurchaseOrder purchaseOrder) {
        PurchaseOrderDTO dto = createResourceWithId(purchaseOrder.getId().getId(), purchaseOrder);
        dto.set_id(purchaseOrder.getId().getId());
        dto.setRentalPeriod(BusinessPeriodDTO.of(purchaseOrder.getRentalPeriod().getStartDate(), purchaseOrder.getRentalPeriod().getEndDate()));
        dto.setTotal(purchaseOrder.getTotal());
        dto.setStatus(purchaseOrder.getStatus());
        dto.setExtensions(purchaseOrder.getExtensions());

        return dto;
    }
    }

