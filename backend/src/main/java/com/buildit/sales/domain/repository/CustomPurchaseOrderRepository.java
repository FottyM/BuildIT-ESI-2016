package com.buildit.sales.domain.repository;

import com.buildit.sales.domain.model.*;

import java.util.List;

/**
 * Created by fotty on 5/14/16.
 */
public interface CustomPurchaseOrderRepository {

    public List<PurchaseOrder> getAllPurchaseOrders();
}
