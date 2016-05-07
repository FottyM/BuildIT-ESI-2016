package com.buildit.hire.rest;

import com.buildit.common.application.exceptions.PlantNotAvailableException;
import com.buildit.hire.application.dto.PlantHireRequestDTO;
import com.buildit.hire.application.service.PlantHireRequestService;
import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestID;
import com.buildit.hire.domain.model.PlantHireRequestStatus;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;
import com.buildit.inventory.application.service.RentalService;
import com.buildit.sales.application.dto.PurchaseOrderDTO;
import com.buildit.sales.domain.model.POStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
@RequestMapping("/api/inventory/plants")
public class PlantHireRequestController {
    @Autowired
    PlantHireRequestService phrService;
    @Autowired
    RentalService rentalService;

    @RequestMapping(method = GET, path = "")
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name", required = false) Optional<String> plantName,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) {
        List<PlantInventoryEntryDTO> response= rentalService.findAvailablePlants(plantName,startDate,endDate);
        return response;
    }

    @RequestMapping(method = POST, path = "")
    public ResponseEntity<PlantHireRequestDTO> createPlantHireRequest( @RequestBody PlantHireRequestDTO plantHireRequestDTO) throws Exception {
       PlantHireRequestDTO response = phrService.createPlantHireRequest(plantHireRequestDTO);


        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PlantHireRequestDTO>(response, headers, HttpStatus.CREATED);

    }



    @RequestMapping(method = POST, path = "/phrs/{id}")
    public ResponseEntity<PurchaseOrderDTO>  acceptPlantHireQuestReject(@PathVariable Long id) throws PlantNotAvailableException {
        PurchaseOrderDTO phr = phrService.acceptPlantHireRequest(id,PlantHireRequestStatus.OPEN);

        HttpHeaders headers = new HttpHeaders();

           if (phr.getStatus()==POStatus.REJECTED){
               return new ResponseEntity<PurchaseOrderDTO>(phr, headers, HttpStatus.CONFLICT);
           }
        return new ResponseEntity<PurchaseOrderDTO>(phr, headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = GET, path = "/phrs/{id}")
    public PlantHireRequestDTO  getPhr(@PathVariable Long id) {
        PlantHireRequestDTO phr = phrService.findPlantHireRequest( PlantHireRequestID.of(id) );
        return phr;
    }


    @RequestMapping(method = PUT, path = "")
    public ResponseEntity<PurchaseOrderDTO>  reSubmitRejectedPo(@RequestBody PurchaseOrderDTO purchaseOrderDTO) throws PlantNotAvailableException {
        PurchaseOrderDTO phr = rentalService.createPurchaseOrder(purchaseOrderDTO);

        HttpHeaders headers = new HttpHeaders();

        if (phr.getStatus()==POStatus.REJECTED){
            return new ResponseEntity<PurchaseOrderDTO>(phr, headers, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<PurchaseOrderDTO>(phr, headers, HttpStatus.ACCEPTED);
    }





}
