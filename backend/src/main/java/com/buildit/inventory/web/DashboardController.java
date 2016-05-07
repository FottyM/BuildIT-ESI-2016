package com.buildit.inventory.web;


import com.buildit.hire.application.dto.PlantHireRequestDTO;
import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestID;
import com.buildit.hire.domain.model.PlantHireRequestStatus;
import com.buildit.inventory.application.dto.PlantInventoryEntryDTO;
import com.buildit.inventory.application.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by rain on 30.03.16.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    RentalService rentalService;


    @RequestMapping(method = GET, path = "/catalog/form")
    public String getQueryForm(Model model) {
        model.addAttribute("catalogQuery", new CatalogQueryDTO());
        return "dashboard/catalog/query-form";
    }

    @RequestMapping(method = POST, path = "/catalog/query")
    public String queryPlantCatalog(Model model, CatalogQueryDTO query) {
//        List<PlantInventoryEntryDTO> availablePlants = rentalService.findAvailablePlants(
//                query.getName(),
//                query.getRentalPeriod().getStartDate(),
//                query.getRentalPeriod().getEndDate());
//        model.addAttribute("plants",availablePlants);
//        PlantHireRequestDTO phr = new PlantHireRequestDTO();
//        phr.setRentalPeriod(query.getRentalPeriod());
//        model.addAttribute("po", phr);

        return "dashboard/catalog/query-result";

    }


}
