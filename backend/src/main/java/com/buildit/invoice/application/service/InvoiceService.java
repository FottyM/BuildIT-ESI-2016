package com.buildit.invoice.application.service;

import com.buildit.hire.application.service.PlantHireRequestService;
import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.inventory.application.service.RentalService;
import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.invoice.domain.model.Invoice;
import com.buildit.invoice.domain.model.InvoiceID;
import com.buildit.invoice.domain.model.InvoiceStatus;
import com.buildit.invoice.domain.repository.InvoiceRepository;
import com.buildit.invoice.infrastructure.idgeneration.InvoiceIdentifierGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
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
    @Autowired
    RentalService rentalService;


    public Invoice findByPoUrlAndPrice(String poUrl, BigDecimal price){
        return invoiceRepository.findByPoUrlAndPrice(poUrl, price);
    }

    public InvoiceDTO findInvoice(Long id) {
        return invoiceAssembler.toResource(invoiceRepository.findOne(InvoiceID.of(id)));
    }

    public List<InvoiceDTO> findInvoiceByStatus(InvoiceStatus status) {



        List<InvoiceDTO>  invoiceDTOs   = new ArrayList<InvoiceDTO>();

        for (Invoice invoice : invoiceRepository.findByStatus(status)){

            InvoiceDTO invoiceDTO = invoiceAssembler.toResource(invoice);
            invoiceDTO.setPurchase(rentalService.findPurchaseOrderDetails(invoice.getPoLink()));
                invoiceDTOs.add(invoiceDTO);
        }

        return invoiceDTOs;

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

    public InvoiceDTO updateReminderReceived(InvoiceDTO invoiceDTO) {
        boolean isValidInvoice = phrService.hasPlantHireRequestByPoUrlAndPrice(invoiceDTO.getPoLink(), invoiceDTO.getTotal());

        if(isValidInvoice) {

            Invoice incoice = invoiceRepository.findByPoUrlAndPrice(invoiceDTO.getPoLink(), invoiceDTO.getTotal());
            incoice.setReminderReceived(LocalDate.now());

            invoiceRepository.save(incoice);
            return invoiceAssembler.toResource(incoice);
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
