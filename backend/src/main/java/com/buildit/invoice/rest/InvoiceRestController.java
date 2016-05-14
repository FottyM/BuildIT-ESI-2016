package com.buildit.invoice.rest;

import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.invoice.application.service.InvoiceService;
import com.buildit.invoice.domain.model.InvoiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
@RequestMapping("/api/invoice")
public class InvoiceRestController {
    @Autowired
    InvoiceService invoiceService;

    @RequestMapping(method = GET, path = "/{id}")
    public InvoiceDTO getInvoice( @PathVariable Long id) throws Exception {
        InvoiceDTO response = invoiceService.findInvoice(id);
        //response.
        //response.add(new Link("http://localhost:8080/asdf/1")); //spring hateoeas
        response.removeLinks();

        return response;
    }

    @RequestMapping(method = GET, path = "/")
    public List<InvoiceDTO> listInvoices() throws Exception {
        List<InvoiceDTO> responses = invoiceService.findInvoiceByStatus(InvoiceStatus.RECEIVED);
        //response.
        //response.add(new Link("http://localhost:8080/asdf/1")); //spring hateoeas
        //response.removeLinks();

        return responses;
    }


    @RequestMapping(method = POST, path = "")
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) throws Exception {

        //invoiceDTO.add(new Link("http://localhost:8080/asdf/1"));
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

}
