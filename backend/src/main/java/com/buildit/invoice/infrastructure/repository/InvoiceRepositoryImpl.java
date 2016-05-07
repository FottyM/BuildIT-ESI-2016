package com.buildit.invoice.infrastructure.repository;

import com.buildit.hire.domain.model.QPlantHireRequest;
import com.buildit.invoice.domain.model.Invoice;
import com.buildit.invoice.domain.model.QInvoice;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.math.BigDecimal;

/**
 * Created by rain on 4.05.16.
 */
public class InvoiceRepositoryImpl {
    @Autowired
    EntityManager em;
    QInvoice qInvoice= QInvoice.invoice;

    public Invoice findInvoiceByPoLink(String link){
        return new JPAQuery(em)
                .from(qInvoice)
                .where(qInvoice.poLink.eq(link)).uniqueResult(qInvoice) ;
    }

    public Invoice findByPoUrlAndPrice(String link, BigDecimal price){
        return new JPAQuery(em)
                .from(qInvoice)
                .where(qInvoice.poLink.eq(link).and(qInvoice.total.eq(price))).uniqueResult(qInvoice);
    }

}
