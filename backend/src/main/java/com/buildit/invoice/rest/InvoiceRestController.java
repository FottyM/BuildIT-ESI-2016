package com.buildit.invoice.rest;

import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.invoice.application.service.InvoiceService;
import com.buildit.invoice.domain.model.InvoiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.util.List;
import java.util.Scanner;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * Created by rain on 27.04.16.
 */
@RestController
@RequestMapping("/api/buildit/invoice")
@CrossOrigin
public class InvoiceRestController {
    @Autowired
    InvoiceService invoiceService;

    @RequestMapping(method = GET, path = "/{id}")
    public InvoiceDTO getInvoice( @PathVariable Long id) throws Exception {
        InvoiceDTO response = invoiceService.findInvoice(id);

        response.removeLinks();

        return response;
    }

    @RequestMapping(method = GET, path = "/")
    public List<InvoiceDTO> listInvoices() throws Exception {
        List<InvoiceDTO> responses = invoiceService.findInvoiceByStatus(InvoiceStatus.RECEIVED);





        return responses;
    }


    @RequestMapping(method = POST, path = "")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) throws Exception {


        List<Link> lnks = invoiceDTO.getLinks();
        String link = "";
        for (Link l : lnks) {
            if(l.getRel().compareTo("self") == 0) {
                link = l.getHref();
                break;
            }
        }

        invoiceDTO = invoiceService.saveInvoice(invoiceDTO);

        if(invoiceDTO == null){ //Invalid invoice received
            invoiceDTO = new InvoiceDTO();
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<InvoiceDTO>(invoiceDTO, headers, HttpStatus.BAD_REQUEST);
        }

        if(invoiceDTO.getTotal().doubleValue() <= 800.0){
            invoiceService.payInvoice(invoiceDTO.getPoLink(),invoiceDTO.getTotal());
        }


        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(invoiceDTO.getId().getHref()));
        return new ResponseEntity<InvoiceDTO>(invoiceDTO, headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = POST, path = "/remind")
    public ResponseEntity<InvoiceDTO> remindInvoice(@RequestBody InvoiceDTO invoiceDTO) throws Exception {
        invoiceDTO = invoiceService.updateReminderReceived(invoiceDTO);

        if(invoiceDTO == null){ //Invalid invoice received
            invoiceDTO = new InvoiceDTO();
            HttpHeaders headers = new HttpHeaders();
            return new ResponseEntity<InvoiceDTO>(invoiceDTO, headers, HttpStatus.BAD_REQUEST);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI(invoiceDTO.getId().getHref()));
        return new ResponseEntity<InvoiceDTO>(invoiceDTO, headers, HttpStatus.CREATED);
    }
    @RequestMapping(method = POST, path = "/makepayment")
    public boolean makePayment(@RequestBody InvoiceDTO invoiceDTO) throws Exception {
        
        //// TODO: 5/26/2016  create a new dto example payementdto and let it take  so that it takes 3 parameters that where being sent from the
        ///  buildit backend

       return  true;
    }

    
    
}
