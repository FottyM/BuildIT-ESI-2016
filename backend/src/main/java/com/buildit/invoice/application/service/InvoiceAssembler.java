package com.buildit.invoice.application.service;

import com.buildit.BuilditApplication;
import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.invoice.domain.model.Invoice;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

/**
 * Created by rain on 27.04.16.
 */
@Service
public class InvoiceAssembler extends ResourceAssemblerSupport<Invoice, InvoiceDTO> {

    public InvoiceAssembler() {
        super(BuilditApplication.class, InvoiceDTO.class);
    }

    @Override
    public InvoiceDTO toResource(Invoice invoice) {
        InvoiceDTO dto = null;
        try {
            dto = createResourceWithId(invoice.getId().getId(), invoice);
        } catch (IllegalStateException ex) {
            dto = new InvoiceDTO();
        }
        dto.setPoLink(invoice.getPoLink());
        dto.setTotal(invoice.getTotal());
        dto.setStatus(invoice.getStatus());
        dto.setReminderReceived(invoice.getReminderReceived());
        return dto;
    }
}
