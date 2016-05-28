package com.buildit.invoice.rest;

import com.buildit.inventory.application.service.RentalService;
import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.invoice.application.dto.PaymentDTO;

import com.buildit.invoice.application.service.InvoiceGateway;
import com.buildit.invoice.application.service.InvoiceService;
import com.buildit.invoice.domain.model.InvoiceStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.net.URI;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Autowired
    InvoiceGateway invoiceGateway;
    @Autowired
    RentalService rentalService;

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
    public String makePayment(@RequestBody PaymentDTO paymentDTO ) throws Exception {

            String email = paymentDTO.getEmail();
        String poUrl = paymentDTO.getPoUrl();


        Pattern p = Pattern.compile(".*s\\/ *(.*)");
         Matcher m = p.matcher(poUrl);
        m.find();
        Long poId = Long.parseLong(m.group(1));

        boolean result = rentalService.sendRemintance(poId);
        if(result){
              invoiceService.invoicePaid(poUrl);
            return "sent";



        }
        else {
            return "error";
        }

    }

}
