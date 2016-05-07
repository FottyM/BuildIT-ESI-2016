package com.buildit.invoice.application.dto;

import com.buildit.invoice.domain.model.InvoiceStatus;
import com.buildit.sales.application.dto.PurchaseOrderDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

/**
 * Created by rain on 27.04.16.
 */
@Data
public class InvoiceDTO extends ResourceSupport {
    Long _id;
    BigDecimal total;
    String poLink;
    InvoiceStatus status;
}
