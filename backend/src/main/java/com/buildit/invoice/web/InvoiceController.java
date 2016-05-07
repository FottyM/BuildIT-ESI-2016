package com.buildit.invoice.web;

import com.buildit.inventory.application.service.RentalService;
import com.buildit.inventory.web.CatalogQueryDTO;
import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.invoice.application.service.InvoiceService;
import com.buildit.invoice.domain.model.Invoice;
import com.buildit.invoice.domain.model.InvoiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by rain on 4.05.16.
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {
    @Autowired
    InvoiceService invoiceService;


    @RequestMapping(method = GET, path = "/")
    public String listInvoices(Model model) {
        List<InvoiceDTO> responses = invoiceService.findInvoiceByStatus(InvoiceStatus.RECEIVED);
        model.addAttribute("invoices",responses);

        return "dashboard/invoice/query-result";
    }

    @RequestMapping(method = POST, path = "/")
    public String invoiceAction(Model model, InvoiceDTO invoiceDTO) {
        Invoice invoice1 = invoiceService.findByPoUrlAndPrice(invoiceDTO.getPoLink(),invoiceDTO.getTotal() );
        //model.addAttribute("invoices",responses);

        if (invoice1 == null){
            return "dashboard/invoice/error";
        }else{
            if(invoiceDTO.getStatus().equals(InvoiceStatus.PAID)) {
                invoiceService.payInvoice(invoice1.getPoLink(), invoice1.getTotal());
            }else{
                invoice1.setStatus(InvoiceStatus.REJECTED);
                invoiceService.updateInvoice(invoice1);
            }
        }

        model.addAttribute("invoice",invoiceDTO);
        return "dashboard/invoice/done";
    }


}

