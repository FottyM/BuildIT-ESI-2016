package com.buildit.invoice.rest;

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

        System.out.println("Arrived at make payment ");
        //// TODO: 5/26/2016  create a new dto example payementdto and let it take  so that it takes 3 parameters that where being sent from the
        ///  buildit backend
        // poUrl
        // Total amount of money
        // email of the guy

        String email = paymentDTO.getEmail();
        String poUrl = paymentDTO.getPoUrl();
        int total = paymentDTO.getTotal();

        JavaMailSender mailSender = new JavaMailSenderImpl();

        String remmitance =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                        "<invoice>\n"+
                        "	<purchaseOrderHRef>http://rentit.com/api/sales/orders/"+ poUrl +"</purchaseOrderHRef>\n"+
                        "	<total>" + total + "</total> \n" +
                        "   <email>" + email + "</email> \n " +
                        "</invoice>\n";

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(rootMessage, true);
        helper.setFrom("agabaisaacsoftwares@gmail.com");
        helper.setTo(email);
        helper.setSubject("Invoice Purchase Order " + poUrl );
        helper.setText("Dear customer,\n\nPlease find attached the Invoice corresponding to your Purchase Order "+ poUrl +".\n\nKindly yours,\n\nRentIt Team!");

        helper.addAttachment("invoice-po-" + poUrl + ".xml", new ByteArrayDataSource(remmitance, "application/xml"));



        invoiceGateway.sendRemittance(rootMessage);

        System.out.println(paymentDTO);

       // if(salesService.invoiceSent(po.getPoId())){}

        return "sent";
    }

}
