package com.buildit.invoice.infrastructure.repository;

import com.buildit.hire.domain.model.QPlantHireRequest;
import com.buildit.invoice.domain.model.Invoice;
import com.buildit.invoice.domain.model.InvoiceStatus;
import com.buildit.invoice.domain.model.QInvoice;
import com.buildit.invoice.domain.repository.CustomInvoiceRepository;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rain on 4.05.16.
 */
public class InvoiceRepositoryImpl implements CustomInvoiceRepository {
    @Autowired
    EntityManager em;
    QInvoice qInvoice= QInvoice.invoice;

    public Invoice findInvoiceByPoLink(String link){
        return new JPAQuery(em)
                .from(qInvoice)
                .where(qInvoice.poLink.eq(link)).uniqueResult(qInvoice) ;
    }

    @Override
    public List<Invoice> findByStatus(InvoiceStatus status) {
        return new JPAQuery(em)
                .from(qInvoice)
                .where(qInvoice.status.eq(status)).list(qInvoice);
    }

    public Invoice findByPoUrlAndPrice(String link, BigDecimal price){
        return new JPAQuery(em)
                .from(qInvoice)
                .where(qInvoice.poLink.eq(link).and(qInvoice.total.eq(price))).uniqueResult(qInvoice);
    }

}
