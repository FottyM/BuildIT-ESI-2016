package com.buildit.invoice.application.dto;

import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;
import com.buildit.invoice.domain.model.InvoiceStatus;
import com.buildit.orders.application.dto.PurchaseOrderDTO;
import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by rain on 27.04.16.
 */
@Data
public class InvoiceDTO extends ResourceSupport {
    Long _id;
    PurchaseOrderDTO purchase;
    BigDecimal total;
    String poLink;
    InvoiceStatus status;
    LocalDate reminderReceived;
}
