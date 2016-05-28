package com.buildit.invoice.application.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

import javax.mail.internet.MimeMessage;

/**
 * Created by fotty on 5/26/16.
 */

@MessagingGateway
public interface InvoiceGateway {

    @Gateway(replyChannel = "sendInvoiceChannel")
    public void sendRemittance(MimeMessage message);

}


