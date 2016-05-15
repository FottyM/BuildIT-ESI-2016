package com.buildit.sales.application.service;

import com.buildit.sales.application.dto.PurchaseOrderDTO;
import com.buildit.sales.domain.model.PurchaseOrder;
import com.buildit.sales.domain.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fotty on 5/15/16.
 */

@Service
public class PurchaseOrderService {

    @Autowired
    PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    PurchaseOrderAssembler purchaseOrderAssembler;




    public List<PurchaseOrderDTO> getPurchaseOrdersLinks() {


        List<PurchaseOrderDTO> purchaseOrderDTOs = new ArrayList<PurchaseOrderDTO>();

        for (String purchaseUrl : purchaseOrderRepository.getAllPurchaseOrders()) {

            PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();
               purchaseOrderDTO.setLink(purchaseUrl);
            purchaseOrderDTOs.add(purchaseOrderDTO);

        }


        return purchaseOrderDTOs;
    }


}
