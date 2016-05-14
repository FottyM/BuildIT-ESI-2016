package com.buildit.invoice.application.service;

import com.buildit.hire.application.service.PlantHireRequestService;
import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.invoice.domain.model.Invoice;
import com.buildit.invoice.domain.model.InvoiceID;
import com.buildit.invoice.domain.model.InvoiceStatus;
import com.buildit.invoice.domain.repository.InvoiceRepository;
import com.buildit.invoice.infrastructure.idgeneration.InvoiceIdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rain on 27.04.16.
 */
@Service
public class InvoiceService {
    @Autowired
    InvoiceIdentifierGenerator identifierGenerator;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    InvoiceAssembler invoiceAssembler;
    @Autowired
    PlantHireRequestService phrService;


    public Invoice findByPoUrlAndPrice(String poUrl, BigDecimal price){
        return invoiceRepository.findByPoUrlAndPrice(poUrl, price);
    }

    public InvoiceDTO findInvoice(Long id) {
        return invoiceAssembler.toResource(invoiceRepository.findOne(InvoiceID.of(id)));
    }

    public List<InvoiceDTO> findInvoiceByStatus(InvoiceStatus status) {
        return invoiceAssembler.toResources(invoiceRepository.findByStatus(status));
    }

    public InvoiceDTO saveInvoice(InvoiceDTO invoiceDTO) {
        boolean isValidInvoice = phrService.hasPlantHireRequestByPoUrlAndPrice(invoiceDTO.getPoLink(), invoiceDTO.getTotal());

        if(isValidInvoice) {
            Invoice i = Invoice.of(
                    identifierGenerator.nextInvoiceID(),
                    invoiceDTO.getPoLink(),
                    invoiceDTO.getTotal()
            );

            invoiceRepository.save(i);
            return invoiceAssembler.toResource(i);
        }
        return null;
    }

    public void updateInvoice(Invoice invoice){
        invoiceRepository.save(invoice);
    }

    public void payInvoice(String poLink, BigDecimal poTotal){

        PlantHireRequest phr = phrService._getPlantHireRequestByPoUrlAndPrice(poLink,poTotal);
        phr.setIsPaid(true);
        phrService.save(phr);

        Invoice invoice = invoiceRepository.findInvoiceByPoLink(poLink);
        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);

    }
}
