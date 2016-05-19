package com.buildit.invoice.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Created by rain on 27.04.16.
 */
@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class Invoice {
    @EmbeddedId
    InvoiceID id;

    String poLink;

    @Column(precision = 8, scale = 2)
    BigDecimal total;
    @Enumerated(EnumType.STRING)
    InvoiceStatus status;

    LocalDate reminderReceived;


    public static Invoice of(InvoiceID id, String poLink, BigDecimal total) {
        Invoice invoice = new Invoice();
        invoice.id = id;
        invoice.poLink = poLink;
        invoice.total = total;
        return invoice;
    }
}
