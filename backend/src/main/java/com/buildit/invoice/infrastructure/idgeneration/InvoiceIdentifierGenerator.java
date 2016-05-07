package com.buildit.invoice.infrastructure.idgeneration;

import com.buildit.common.infrastructure.HibernateBasedIdentifierGenerator;
import com.buildit.hire.domain.model.PlantHireRequestID;
import com.buildit.invoice.domain.model.InvoiceID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by rain on 27.04.16.
 */
@Service
public class InvoiceIdentifierGenerator {
    @Autowired
    HibernateBasedIdentifierGenerator hibernateGenerator;

    public InvoiceID nextInvoiceID() {
        return InvoiceID.of(hibernateGenerator.getID("InvoiceIDSequence"));
    }
}
