package com.buildit.inventory.application.dto;

import com.buildit.common.rest.ResourceSupport;
import lombok.Data;
import org.springframework.hateoas.Link;


import java.math.BigDecimal;
import java.util.List;

/**
 * Created by rain on 30.03.16.
 */
@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    String name;
    String description;
    BigDecimal price;
   // List<Link> _links;
}

