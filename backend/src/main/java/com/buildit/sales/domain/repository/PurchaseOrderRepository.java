package com.buildit.sales.domain.repository;

import com.buildit.sales.domain.model.PurchaseOrderID;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by fotty on 5/14/16.
 */


public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderRepository,PurchaseOrderID>, CustomPurchaseOrderRepository {




}
