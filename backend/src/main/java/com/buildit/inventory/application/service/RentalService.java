package com.buildit.inventory.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;

import com.buildit.common.application.exceptions.PlantNotAvailableException;
import com.buildit.hire.application.service.PlantHireRequestAssembler;
import com.buildit.hire.domain.repository.PlantHireRequestRepository;
import com.buildit.hire.infrastructure.idgeneration.PlantHireRequestIdentifierGenerator;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;
import java.time.LocalDate;

import com.buildit.orders.application.dto.PurchaseOrderDTO;
import com.buildit.orders.domain.model.POStatus;
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

        System.out.println(order);
        try {
            ResponseEntity<PurchaseOrderDTO> result = restTemplate.postForEntity(
                    "http://" + host + ":" + port + "/api/sales/orders", order, PurchaseOrderDTO.class);

            return result.getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.CONFLICT)){

                PurchaseOrderDTO purchaseOrderDTO  = order;
               // order.setStatus(POStatus.REJECTED);
                 return purchaseOrderDTO;


            }
        }
        return null;
    }


    public List<PlantInventoryEntryDTO> findAvailablePlants(Optional<String> plantName, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {
        PlantInventoryEntryDTO[] plants = restTemplate.getForObject(
                "http://" + host + ":" + port + "api/inventory/plants?name={name}&startDate={start}&endDate={end}",
                PlantInventoryEntryDTO[].class, plantName.get(), startDate.get(),endDate.get());
        return Arrays.asList(plants);
    }


    public PurchaseOrderDTO findPurchaseOrderDetails(String poUlr) {
        PurchaseOrderDTO purchaseOrderDTO = restTemplate.getForObject(poUlr,
                PurchaseOrderDTO.class);
        return purchaseOrderDTO;
    }
    public PurchaseOrderDTO extendPurchaseOrder(Long id, BusinessPeriodDTO businessPeriodDTO) {
        try {
            ResponseEntity<PurchaseOrderDTO> result = restTemplate.postForEntity(
                    "http://" + host + ":" + port + "/api/sales/orders/{id}/extensions",businessPeriodDTO, PurchaseOrderDTO.class,id);

            return result.getBody();
        } catch (final HttpClientErrorException e) {
            if (e.getStatusCode().equals(HttpStatus.CONFLICT)){

                return new PurchaseOrderDTO();


            }
        }
        return null;
    }

    public PlantInventoryEntryDTO findPlantDetails(Long id) {
        System.out.println(id);
          if (id!=null){


        PlantInventoryEntryDTO plantInventoryEntryDTO = restTemplate.getForObject("http://" + host + ":" + port + "/api/inventory/plants/{id}",
                PlantInventoryEntryDTO.class,id);






        return plantInventoryEntryDTO;
          }
        else {
              return new PlantInventoryEntryDTO();
          }
    }
}