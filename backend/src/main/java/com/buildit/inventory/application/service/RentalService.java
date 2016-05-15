package com.buildit.inventory.application.service;

import com.buildit.common.application.exceptions.PlantNotAvailableException;
import com.buildit.hire.application.service.PlantHireRequestAssembler;
import com.buildit.hire.domain.repository.PlantHireRequestRepository;
import com.buildit.hire.infrastructure.idgeneration.PlantHireRequestIdentifierGenerator;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;
import java.time.LocalDate;

import com.buildit.sales.application.dto.PurchaseOrderDTO;
import com.buildit.sales.domain.model.POStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * Created by rain on 30.03.16.
 */
@Service
public class RentalService {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    PlantHireRequestIdentifierGenerator identifierGenerator;
    @Autowired
    PlantHireRequestRepository plantHireRequestRepository;
    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;

    @Value("${rentit.host:}")
    String host;

    @Value("${rentit.port:}")
    String port;

    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO order) throws PlantNotAvailableException {
        try {
            ResponseEntity<PurchaseOrderDTO> result = restTemplate.postForEntity(
                    "http://" + host + ":" + port + "/api/sales/orders", order, PurchaseOrderDTO.class);

            return result.getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.CONFLICT)){
//
//                PurchaseOrderDTO purchaseOrderDTO  = order;
//                order.setStatus(POStatus.REJECTED);
//                System.out.println("ddddddddddddddddddddddddddddddddddddddddddddddd");
//                return purchaseOrderDTO;


                        }
        }
        return null;
    }


    public List<PlantInventoryEntryDTO> findAvailablePlants(Optional<String> plantName, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {
               PlantInventoryEntryDTO[] plants = restTemplate.getForObject(
                "http://" + host + ":" + port + "api/inventory/plants?name={name}&startDate={start}&endDate={end}",
                PlantInventoryEntryDTO[].class, plantName, startDate, endDate);
        return Arrays.asList(plants);
    }







}









