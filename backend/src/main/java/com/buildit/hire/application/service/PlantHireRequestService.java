package com.buildit.hire.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;

import com.buildit.common.application.exceptions.PlantNotAvailableException;
import com.buildit.common.application.exceptions.PlantNotFoundException;
import com.buildit.common.application.service.BusinessPeriodAssembler;
import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.hire.application.dto.PlantHireRequestDTO;
import com.buildit.hire.application.service.PlantHireRequestAssembler;
import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestID;
import com.buildit.hire.domain.model.PlantHireRequestStatus;
import com.buildit.hire.domain.repository.PlantHireRequestRepository;
import com.buildit.hire.infrastructure.idgeneration.PlantHireRequestIdentifierGenerator;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.buildit.inventory.application.service.RentalService;
import com.buildit.sales.application.dto.PurchaseOrderDTO;
import com.buildit.sales.domain.model.POStatus;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * Created by rain on 30.03.16.
 */
@Service
public class PlantHireRequestService {

    @Autowired
    PlantHireRequestIdentifierGenerator identifierGenerator;
    @Autowired
    PlantHireRequestRepository plantHireRequestRepository;
    @Autowired
    PlantHireRequestAssembler plantHireRequestAssembler;
    @Autowired
    RentalService rentalService;


    public PlantHireRequestDTO createPlantHireRequest(PlantHireRequestDTO plantHireRequestDTO) {
        PlantHireRequest phr = PlantHireRequest.of(
                identifierGenerator.nextPlantHireRequestID(),
                plantHireRequestDTO.getPlantId(),
                plantHireRequestDTO.getRentalPeriod(),
                plantHireRequestDTO.getPrice()
        );

        plantHireRequestRepository.save(phr);

        return plantHireRequestAssembler.toResource(phr);
    }

    public PlantHireRequestDTO findPlantHireRequest(PlantHireRequestID phrid) {
        return plantHireRequestAssembler.toResource(plantHireRequestRepository.findOne(phrid));
    }

    public PlantHireRequest _findPlantHireRequest(PlantHireRequestID phrid) {
        return plantHireRequestRepository.findOne(phrid);
    }

    public boolean hasPlantHireRequestByPoUrlAndPrice(String poUrl, BigDecimal price){
        return plantHireRequestRepository.hasPlantHireRequestByPoUrlAndPrice(poUrl, price);
    }

    public PlantHireRequest _getPlantHireRequestByPoUrlAndPrice(String poUrl, BigDecimal price){
        return plantHireRequestRepository.getPlantHireRequestByPoUrlAndPrice(poUrl, price);
    }


    public PlantHireRequestDTO rejectPlantHireRequest(Long id, String reason) throws PlantNotAvailableException {

        PlantHireRequestDTO phr = findPlantHireRequest(PlantHireRequestID.of(id));
        phr.setStatus(PlantHireRequestStatus.REJECTED);
        phr.setComment(reason);

        //allow reject till 1 day before
        if(ChronoUnit.DAYS.between(LocalDate.now(), phr.getRentalPeriod().getStartDate()) < 1){
            return null;
        }

        PlantHireRequest rejectedPhr = _findPlantHireRequest(PlantHireRequestID.of(id));
        rejectedPhr.setStatus(PlantHireRequestStatus.REJECTED);


        PlantHireRequestDTO pp=  new PlantHireRequestAssembler().toResource( plantHireRequestRepository.save(rejectedPhr));


        return   pp;
    }

    public PurchaseOrderDTO acceptPlantHireRequest(Long id, PlantHireRequestStatus status) throws PlantNotAvailableException {

        PlantHireRequest acceptedPhr = _findPlantHireRequest(PlantHireRequestID.of(id));

        acceptedPhr.setStatus(status);


        PlantHireRequestDTO pp=  new PlantHireRequestAssembler().toResource( plantHireRequestRepository.save(acceptedPhr));

        if(status==PlantHireRequestStatus.REJECTED){
            return null;
        }

        PlantInventoryEntryDTO plant = new PlantInventoryEntryDTO();
        plant.add(new Link("http://rentit.com/api/inventory/plants/13"));

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plant);
        order.setRentalPeriod(pp.getRentalPeriod());


        return   rentalService.createPurchaseOrder(order);
    }

    public void save(PlantHireRequest phr) {
        plantHireRequestRepository.save(phr);
    }

    public PlantHireRequestDTO modifyPlantHireRequest(PlantHireRequestDTO plantHireRequestDTO) {
        PlantHireRequestDTO phr = findPlantHireRequest(PlantHireRequestID.of(plantHireRequestDTO.get_id()));

        PlantHireRequestDTO pp = phr;
        if(phr != null) {


            PlantHireRequest modifiedPhr = _findPlantHireRequest(PlantHireRequestID.of(plantHireRequestDTO.get_id()));
            if(plantHireRequestDTO.getPlantId() != null){modifiedPhr.setPlantId(plantHireRequestDTO.getPlantId());}
            if(plantHireRequestDTO.getRentalPeriod() != null){
                BusinessPeriod bp = BusinessPeriod.of(plantHireRequestDTO.getRentalPeriod().getStartDate(), plantHireRequestDTO.getRentalPeriod().getEndDate());
                modifiedPhr.setRentalPeriod(bp);
            }
            if(plantHireRequestDTO.getPrice() != null){modifiedPhr.setPrice(plantHireRequestDTO.getPrice());}
            if(plantHireRequestDTO.getStatus() != null){modifiedPhr.setStatus(plantHireRequestDTO.getStatus());}
            if(plantHireRequestDTO.getComment() != null){modifiedPhr.setComment(plantHireRequestDTO.getComment());}


            pp = new PlantHireRequestAssembler().toResource(plantHireRequestRepository.save(modifiedPhr));
        }

        return   pp;
    }

//    public PlantHireRequestDTO savePlantHireRequestReject(Long id) throws PlantNotAvailableException {
//
//        PlantHireRequestDTO phr = findPlantHireRequest(PlantHireRequestID.of(id));
//        phr.setStatus(PlantHireRequestStatus.REJECTED);
//
//        PlantHireRequest acceptedPhr = PlantHireRequest.of(
//                PlantHireRequestID.of(phr.get_id()),
//                phr.getPlantId(),
//                phr.getRentalPeriod(),
//                phr.getPrice(),
//                phr.getStatus()
//        );
//
//
//
//
//        PlantHireRequestDTO pp=  new PlantHireRequestAssembler().toResource( plantHireRequestRepository.save(acceptedPhr));
//
//        PlantInventoryEntryDTO plant = new PlantInventoryEntryDTO();
//        plant.add(new Link("http://rentit.com/api/inventory/plants/13"));
//
//        PurchaseOrderDTO order = new PurchaseOrderDTO();
//        order.setPlant(plant);
//        order.setRentalPeriod(pp.getRentalPeriod());
//
//
//        return   phr;
//    }
}









