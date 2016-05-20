package com.buildit.hire.application.service;

import com.buildit.common.application.dto.BusinessPeriodDTO;

import com.buildit.common.application.exceptions.PlantNotAvailableException;
import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.hire.application.dto.PlantHireRequestDTO;
import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestID;
import com.buildit.hire.domain.model.PlantHireRequestStatus;
import com.buildit.hire.domain.repository.PlantHireRequestRepository;
import com.buildit.hire.infrastructure.idgeneration.PlantHireRequestIdentifierGenerator;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.buildit.inventory.application.service.RentalService;
import com.buildit.orders.application.dto.PurchaseOrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        if(plantHireRequestDTO.getPlantUrl()!=null){
            Pattern p = Pattern.compile(".*s\\/ *(.*)");
            Matcher m = p.matcher(plantHireRequestDTO.getPlantUrl());
            m.find();
            plantHireRequestDTO.setPlantId(Long.parseLong(m.group(1)));
        }


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



    public  List<PlantHireRequestDTO> getPlantHireRequests() {
        List<PlantHireRequestDTO>   plantHireRequestDTOs = new ArrayList<PlantHireRequestDTO>();

        for (PlantHireRequest  plantHireRequest:plantHireRequestRepository.getPlantHireRequests() ) {
            System.out.println(plantHireRequest.getPlantId());
            PlantHireRequestDTO  plantHireRequestDTO =new PlantHireRequestDTO();
            plantHireRequestDTO.setPlantDetails(rentalService.findPlantDetails(plantHireRequest.getPlantId()));
            plantHireRequestDTO.setPrice(plantHireRequest.getPrice());
            plantHireRequestDTO.setSupplier(plantHireRequest.getSupplier());
            plantHireRequestDTO.setStatus(plantHireRequest.getStatus());
            BusinessPeriodDTO businessPeriodDTO = new BusinessPeriodDTO();
            businessPeriodDTO.setStartDate(plantHireRequest.getRentalPeriod().getStartDate());
            businessPeriodDTO.setEndDate(plantHireRequest.getRentalPeriod().getEndDate());
            plantHireRequestDTO.setRentalPeriod(businessPeriodDTO);
            plantHireRequestDTO.set_id(plantHireRequest.getId().getId());

         plantHireRequestDTOs.add(plantHireRequestDTO);
        }

        return plantHireRequestDTOs;
    }
    public PlantHireRequest _findPlantHireRequest(PlantHireRequestID phrid) {
        return plantHireRequestRepository.findOne(phrid);
    }

    public boolean hasPlantHireRequestByPoUrlAndPrice(String poUrl, BigDecimal price) {
        return plantHireRequestRepository.hasPlantHireRequestByPoUrlAndPrice(poUrl, price);
    }

    public PlantHireRequest _getPlantHireRequestByPoUrlAndPrice(String poUrl, BigDecimal price) {
        return plantHireRequestRepository.getPlantHireRequestByPoUrlAndPrice(poUrl, price);
    }

    public PlantHireRequestDTO rejectPlantHireRequest(Long id, String reason) throws PlantNotAvailableException {

        PlantHireRequestDTO phr = findPlantHireRequest(PlantHireRequestID.of(id));
        phr.setStatus(PlantHireRequestStatus.REJECTED);
        phr.setComment(reason);

        //allow reject till 1 day before
        if (ChronoUnit.DAYS.between(LocalDate.now(), phr.getRentalPeriod().getStartDate()) < 1) {
            return null;
        }

        PlantHireRequest rejectedPhr = _findPlantHireRequest(PlantHireRequestID.of(id));
        rejectedPhr.setStatus(PlantHireRequestStatus.REJECTED);


        PlantHireRequestDTO pp = new PlantHireRequestAssembler().toResource(plantHireRequestRepository.save(rejectedPhr));


        return pp;
    }


    public PurchaseOrderDTO acceptPlantHireRequest(Long id, PlantHireRequestStatus status) throws PlantNotAvailableException {

        PlantHireRequest acceptedPhr = _findPlantHireRequest(PlantHireRequestID.of(id));

        acceptedPhr.setStatus(status);


        PlantHireRequestDTO pp = new PlantHireRequestAssembler().toResource(plantHireRequestRepository.save(acceptedPhr));

        if (status == PlantHireRequestStatus.REJECTED) {
            return null;
        }

        PlantInventoryEntryDTO plant = new PlantInventoryEntryDTO();
        plant.add(new Link("http://rentit.com/api/inventory/plants/13"));

        PurchaseOrderDTO order = new PurchaseOrderDTO();
        order.setPlant(plant);
        order.setRentalPeriod(pp.getRentalPeriod());


        return rentalService.createPurchaseOrder(order); //Needs at least mock
        //return new PurchaseOrderDTO();
    }

    public void save(PlantHireRequest phr) {
        plantHireRequestRepository.save(phr);
    }

    public PlantHireRequestDTO modifyPlantHireRequest(PlantHireRequestDTO plantHireRequestDTO, Long phrId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = new LinkedList<String>();

        UserDetails details = null;
        if (principal instanceof UserDetails) {
            details = (UserDetails) principal;
            System.out.println(details);
        }

        String role = details.getAuthorities().toArray()[0].toString();

        PlantHireRequestDTO phr = findPlantHireRequest(PlantHireRequestID.of(phrId));
        if (role.compareTo("SITE_ENGINEER") == 0 && phr.getStatus().compareTo(PlantHireRequestStatus.OPEN) != 0
                || role.compareTo("WORK_ENGINEER") == 0) {

            PlantHireRequestDTO pp = phr;
            if (phr != null) {


                PlantHireRequest modifiedPhr = _findPlantHireRequest(PlantHireRequestID.of(phrId));
                if (plantHireRequestDTO.getPlantId() != null) {
                    modifiedPhr.setPlantId(plantHireRequestDTO.getPlantId());
                }
                if (plantHireRequestDTO.getRentalPeriod() != null) {
                    BusinessPeriod bp = BusinessPeriod.of(plantHireRequestDTO.getRentalPeriod().getStartDate(), plantHireRequestDTO.getRentalPeriod().getEndDate());
                    modifiedPhr.setRentalPeriod(bp);
                }
                if (plantHireRequestDTO.getPrice() != null) {
                    modifiedPhr.setPrice(plantHireRequestDTO.getPrice());
                }
                if (plantHireRequestDTO.getStatus() != null) {
                    modifiedPhr.setStatus(plantHireRequestDTO.getStatus());
                }
                if (plantHireRequestDTO.getComment() != null) {
                    modifiedPhr.setComment(plantHireRequestDTO.getComment());
                }


                pp = new PlantHireRequestAssembler().toResource(plantHireRequestRepository.save(modifiedPhr));
            }

            return pp;
        }
        return new PlantHireRequestDTO();
    }

    public List<PurchaseOrderDTO> findsubmitedPO() {

        List<PlantHireRequest> plantHireRequestList = plantHireRequestRepository.getListOfPos();

        List<PurchaseOrderDTO> purchaseOrderDTOList = new ArrayList<PurchaseOrderDTO>();

        for (PlantHireRequest plantHireRequest : plantHireRequestList) {

            purchaseOrderDTOList.add(rentalService.findPurchaseOrderDetails(plantHireRequest.getPoUrl()));

        }

        return purchaseOrderDTOList;

    }

    public PurchaseOrderDTO extendPurchaseOrder(Long id, BusinessPeriodDTO businessPeriodDTO) {

        PlantHireRequest plantHireRequest = plantHireRequestRepository.findOne(PlantHireRequestID.of(id));

        Pattern p = Pattern.compile(".*s\\/ *(.*)");
        Matcher m = p.matcher(plantHireRequest.getPoUrl());
        m.find();
        Long poId = Long.parseLong(m.group(1));

        return rentalService.extendPurchaseOrder(poId,businessPeriodDTO);
    }

}









