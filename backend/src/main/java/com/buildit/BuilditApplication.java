package com.buildit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.mail.Mail;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rain on 29.03.16.
 */


@EntityScan(basePackageClasses = { BuilditApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication
@Component
@IntegrationComponentScan
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class BuilditApplication {


    @Configuration
    static class ObjectMapperCustomizer {
        @Autowired
        @Qualifier("_halObjectMapper")
        private ObjectMapper springHateoasObjectMapper;

        @Bean(name = "objectMapper")
        ObjectMapper objectMapper() {
            return springHateoasObjectMapper
                    .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .configure(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                    .configure(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS, false)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                    .registerModules(new JavaTimeModule());
        }
        @Bean
        public RestTemplate restTemplate() {
            RestTemplate _restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new MappingJackson2HttpMessageConverter(springHateoasObjectMapper));
            _restTemplate.setMessageConverters(messageConverters);
            return _restTemplate;
        }
    }

    @MessagingGateway
    public interface GreetingsService {
        @Gateway(requestChannel = "sendInvoiceChannel")
        void sayHello(String msg);
    }



    @MessagingGateway
    interface InvoicingGateway {
        @Gateway(requestChannel = "sendInvoiceChannel")
        public void sendInvoice(MimeMessage msg);
    }


    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext ctx = SpringApplication.run(BuilditApplication.class, args);
        InvoicingGateway service = ctx.getBean(InvoicingGateway.class);



        JavaMailSender mailSender = new JavaMailSenderImpl();
        String invoice1 =
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                        "<invoice>\n"+
                        "	<purchaseOrderHRef>http://rentit.com/api/sales/orders/1</purchaseOrderHRef>\n"+
                        "	<total>150.00</total>\n"+
                        "</invoice>\n";

        MimeMessage rootMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(rootMessage, true);
        helper.setFrom("agabaisaacsoftwares@gmail.com");
        helper.setTo("agabaisaacsoftwares@gmail.com");
        helper.setSubject("Invoice Purchase Order 123");
        helper.setText("Dear customer,\n\nPlease find attached the Invoice corresponding to your Purchase Order 123.\n\nKindly yours,\n\nRentIt Team!");

        helper.addAttachment("invoice-po-123.xml", new ByteArrayDataSource(invoice1, "application/xml"));
        service.sendInvoice(rootMessage);
    }


    @Value("${gmail.username}")
    String gmailUsername;
    @Value("${gmail.password}")
    String gmailPassword;
    @Bean
    public  IntegrationFlow receiveInvoiceFlow(){
        return IntegrationFlows
                .from(Mail.imapIdleAdapter(String.format("imaps://%s:%s@imap.gmail.com/INBOX", gmailUsername, gmailPassword))
                        .selectorExpression("subject matches '.*Invoice.*'")
                )
                .transform("@invoiceProcessor.extractInvoice(payload)")
                .route("#xpath(payload, '//total <= 800', 'string')", mapping -> mapping
                        .subFlowMapping("true", sf -> sf
                                .handle("invoiceProcessor", "processInvoiceLesser"))
                        .subFlowMapping("false", sf -> sf
                                .handle("invoiceProcessor", "processInvoiceGreater"))
                )
                .get();
    }





    @Bean
    IntegrationFlow sendInvoiceFlow() {
        return IntegrationFlows.from("sendInvoiceChannel")
                .handle(Mail.outboundAdapter("smtp.gmail.com")
                        .port(465)
                        .protocol("smtps")
                        .credentials(gmailUsername, gmailPassword)
                        .javaMailProperties(p -> p.put("mail.debug", "false"))
                )

                .get();
    }
}