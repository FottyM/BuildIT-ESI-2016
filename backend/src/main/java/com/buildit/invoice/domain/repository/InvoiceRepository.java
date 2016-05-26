package com.buildit.invoice.domain.repository;

import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestID;
import com.buildit.hire.domain.repository.CustomPlantHireRequestRepository;
import com.buildit.invoice.domain.model.Invoice;
import com.buildit.invoice.domain.model.InvoiceID;
import com.buildit.invoice.domain.model.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rain on 27.04.16.
 */
@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, InvoiceID>,CustomInvoiceRepository {

}
