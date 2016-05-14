package com.buildit.sales.rest;

import com.buildit.sales.application.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rain on 31.03.16.
 */

@RestController
@RequestMapping(path = "/pos")
public class PurchaseOrderRestController {

    @Autowired
    PurchaseOrderService purchaseOrderService;

    @RequestMapping(method = GET , path = "/")
    public String getAllPurchaseOrders(){

    return purchaseOrderService.getPurchaseOrdersLinks().toString();
    }

}
