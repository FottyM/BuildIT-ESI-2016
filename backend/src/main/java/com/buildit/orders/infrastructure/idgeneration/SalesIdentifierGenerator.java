package com.buildit.orders.infrastructure.idgeneration;

import com.buildit.common.infrastructure.HibernateBasedIdentifierGenerator;
import com.buildit.orders.domain.model.PurchaseOrderExtensionID;
import com.buildit.orders.domain.model.PurchaseOrderID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SalesIdentifierGenerator {
    @Autowired
    HibernateBasedIdentifierGenerator hibernateGenerator;

    public PurchaseOrderID nextPurchaseOrderID() {
        return PurchaseOrderID.of(hibernateGenerator.getID("PurchaseOrderIDSequence"));
    }
    public PurchaseOrderExtensionID nextPurchaseOrderExtensionID() {
        return PurchaseOrderExtensionID.of(hibernateGenerator.getID("PurchaseOrderExtensionIDSequence"));
    }
}
