package com.buildit;

import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.invoice.application.service.InvoiceService;
import com.buildit.invoice.domain.model.Invoice;
import com.buildit.invoice.domain.model.InvoiceID;
import com.buildit.invoice.domain.model.InvoiceStatus;
import com.buildit.invoice.infrastructure.idgeneration.InvoiceIdentifierGenerator;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rain on 27.04.16.
 */
@Service
public class InvoiceProcessor {
    @Autowired
    InvoiceService invoiceService;

    public String extractInvoice(MimeMessage msg) throws Exception{
        Multipart multipart = (Multipart) msg.getContent();
        for (int i = 0; i < multipart.getCount(); i++){
            BodyPart bodyPart = multipart.getBodyPart(i);
            if(bodyPart.getContentType().contains("xml") && bodyPart.getFileName().startsWith("invoice")){
                return IOUtils.toString(bodyPart.getInputStream(), "UTF-8");
            }
        }
        throw new Exception("Oops no invoice found");
    }


    public String extractUrl(String invoice){
        Pattern p = Pattern.compile("<purchaseOrderHRef>(.*?)</purchaseOrderHRef>");
        Matcher m = p.matcher(invoice);
        while(m.find())
        {
            return m.group(1);
        }
        return "";
    }

    public BigDecimal extractPrice(String invoice){
        Pattern p = Pattern.compile("<total>(.*?)</total>");
        Matcher m = p.matcher(invoice);
        while(m.find())
        {
            Double d = Double.parseDouble(m.group(1));
            return new BigDecimal(d);

        }
        return new BigDecimal(0.0);
    }

    public void processInvoiceLesser(String invoice) throws Exception{

        BigDecimal price = extractPrice(invoice);
        String url = extractUrl(invoice);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setPoLink(url);
        invoiceDTO.setTotal(price);
        invoiceDTO.setStatus(InvoiceStatus.ACCEPTED);
        invoiceService.saveInvoice(invoiceDTO);
        invoiceService.payInvoice(url,price);

        System.out.println("PAYED");
        System.out.println(invoice);



    }
    public void processInvoiceGreater(String invoice) throws Exception
    {
        BigDecimal price = extractPrice(invoice);
        String url = extractUrl(invoice);

        InvoiceDTO invoiceDTO = new InvoiceDTO();
        invoiceDTO.setPoLink(url);
        invoiceDTO.setTotal(price);
        invoiceDTO.setStatus(InvoiceStatus.RECEIVED);
        invoiceService.saveInvoice(invoiceDTO);


        System.out.println(invoice);

    }
}