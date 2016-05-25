package com.buildit.invoice.domain.repository;

import com.buildit.invoice.domain.model.Invoice;
import com.buildit.invoice.domain.model.InvoiceStatus;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by akaiz on 5/25/2016.
 */
public interface CustomInvoiceRepository {
    public Invoice findInvoiceByPoLink(String link);

    List<Invoice> findByStatus(InvoiceStatus status);

    Invoice findByPoUrlAndPrice(String poUrl, BigDecimal price);
}
