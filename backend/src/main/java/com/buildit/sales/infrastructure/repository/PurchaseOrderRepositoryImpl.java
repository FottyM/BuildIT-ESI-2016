package com.buildit.sales.infrastructure.repository;

import com.buildit.hire.domain.repository.CustomPlantHireRequestRepository;
import com.buildit.sales.domain.model.PurchaseOrder;
import com.buildit.sales.domain.model.QPurchaseOrder;
import com.buildit.sales.domain.repository.CustomPurchaseOrderRepository;
import com.mysema.query.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Created by fotty on 5/15/16.
 */
public class PurchaseOrderRepositoryImpl implements CustomPurchaseOrderRepository {
    @Autowired
    EntityManager em;

    QPurchaseOrder qPurchaseOrder = QPurchaseOrder.purchaseOrder;
    @Override
    public List<PurchaseOrder> getAllPurchaseOrders() {
        return new JPAQuery(em)
                .from(qPurchaseOrder).list(qPurchaseOrder);
    }
}
