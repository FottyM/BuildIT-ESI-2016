package com.buildit.sales.application.dto;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;
import com.buildit.sales.domain.model.POStatus;
import com.buildit.sales.domain.model.PurchaseOrderExtensionID;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor(force = true)
public class PurchaseOrderDTO extends ResourceSupport {

    String link;
}
