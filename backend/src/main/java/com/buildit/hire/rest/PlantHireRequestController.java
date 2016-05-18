package com.buildit.hire.rest;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.application.exceptions.PlantNotAvailableException;
import com.buildit.common.domain.model.BusinessPeriod;
import com.buildit.hire.application.dto.PlantHireRequestDTO;
import com.buildit.hire.application.service.PlantHireRequestService;
import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestID;
import com.buildit.hire.domain.model.PlantHireRequestStatus;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;
import com.buildit.inventory.application.service.RentalService;
import com.buildit.invoice.application.dto.InvoiceDTO;
import com.buildit.sales.application.dto.PurchaseOrderDTO;
import com.buildit.sales.domain.model.POStatus;
import com.buildit.sales.domain.model.PurchaseOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@CrossOrigin
@RestController
@RequestMapping("/api/sales/")
public class PlantHireRequestController {
    @Autowired
    PlantHireRequestService phrService;
    @Autowired
    RentalService rentalService;

    @RequestMapping(method = GET, path = "")
    public List<PlantInventoryEntryDTO> findAvailablePlants(
            @RequestParam(name = "name", required = false) Optional<String> plantName,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate) throws IOException {
        List<PlantInventoryEntryDTO> response = rentalService.findAvailablePlants(plantName, startDate, endDate);
        return response;
    }

    @RequestMapping(method = POST, path = "")
    public ResponseEntity<PlantHireRequestDTO> createPlantHireRequest(@RequestBody PlantHireRequestDTO plantHireRequestDTO) throws Exception {
        PlantHireRequestDTO response = phrService.createPlantHireRequest(plantHireRequestDTO);


        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PlantHireRequestDTO>(response, headers, HttpStatus.CREATED);

    }


    @RequestMapping(method = POST, path = "/phrs/{id}")
    public ResponseEntity<PurchaseOrderDTO> acceptPlantHireQuestReject(@PathVariable Long id) throws PlantNotAvailableException {
        PurchaseOrderDTO phr = phrService.acceptPlantHireRequest(id, PlantHireRequestStatus.OPEN);

        HttpHeaders headers = new HttpHeaders();

        if (phr.getStatus() == POStatus.REJECTED) {
            return new ResponseEntity<PurchaseOrderDTO>(phr, headers, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<PurchaseOrderDTO>(phr, headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = POST, path = "/phrs/{id}/reject")
    public ResponseEntity<PlantHireRequestDTO> rejectPlantHireQuestReject(@PathVariable Long id, @RequestBody PlantHireRequestDTO plantHireRequestDTO) throws PlantNotAvailableException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<String> roles = new LinkedList<String>();

        UserDetails details = null;
        if (principal instanceof UserDetails) {
            details = (UserDetails) principal;
            System.out.println(details);
        }

        String role = details.getAuthorities().toArray()[0].toString();

        if (role.compareTo("SITE_ENGINEER") == 0 || role.compareTo("WORK_ENGINEER") == 0) {

            PlantHireRequestDTO phr = phrService.rejectPlantHireRequest(id, plantHireRequestDTO.getComment());

            HttpHeaders headers = new HttpHeaders();

            return new ResponseEntity<PlantHireRequestDTO>(phr, headers, HttpStatus.OK);
        }
        return new ResponseEntity<PlantHireRequestDTO>(new PlantHireRequestDTO(), HttpStatus.FORBIDDEN);
    }

    @RequestMapping(method = POST, path = "/phrs/{id}/modify")
    public ResponseEntity<PlantHireRequestDTO> modifyPlantHireQuestReject(@PathVariable Long id, @RequestBody PlantHireRequestDTO plantHireRequestDTO) throws PlantNotAvailableException {
        PlantHireRequestDTO phr = phrService.modifyPlantHireRequest(plantHireRequestDTO, id);

        HttpHeaders headers = new HttpHeaders();

        return new ResponseEntity<PlantHireRequestDTO>(phr, headers, HttpStatus.OK);
    }

    @RequestMapping(method = GET, path = "/phrs/{id}")
    public PlantHireRequestDTO getPhr(@PathVariable Long id) {
        PlantHireRequestDTO phr = phrService.findPlantHireRequest(PlantHireRequestID.of(id));
        return phr;
    }


    @RequestMapping(method = PUT, path = "")
    public ResponseEntity<PurchaseOrderDTO> reSubmitRejectedPo(@RequestBody PurchaseOrderDTO purchaseOrderDTO) throws PlantNotAvailableException {
        PurchaseOrderDTO phr = rentalService.createPurchaseOrder(purchaseOrderDTO);

        HttpHeaders headers = new HttpHeaders();

        if (phr.getStatus() == POStatus.REJECTED) {
            return new ResponseEntity<PurchaseOrderDTO>(phr, headers, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<PurchaseOrderDTO>(phr, headers, HttpStatus.ACCEPTED);
    }

    @RequestMapping(method = GET, path = "/po")
    public List<PurchaseOrderDTO> getSubmitedPos() {

        List<PurchaseOrderDTO> phrdto = phrService.findsubmitedPO();

        return phrdto;
    }

    @RequestMapping(method = POST, path = "/po/{id}/extension")
    public PurchaseOrderDTO askForExtension(@PathVariable Long id, @RequestBody BusinessPeriodDTO businessPeriodDTO) {
        return  phrService.extendPurchaseOrder(id,businessPeriodDTO);



    }

}
