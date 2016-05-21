package com.buildit.orders.application.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.rest.ResourceSupport;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;

import com.buildit.orders.domain.model.POStatus;
import com.buildit.orders.domain.model.PurchaseOrderExtensionID;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class PurchaseOrderDTO extends ResourceSupport {
    PlantInventoryEntryDTO plant;
    BusinessPeriodDTO rentalPeriod;
    BigDecimal total;
    POStatus status;
    String email;
    List<PurchaseOrderExtensionID>extensions;
}
