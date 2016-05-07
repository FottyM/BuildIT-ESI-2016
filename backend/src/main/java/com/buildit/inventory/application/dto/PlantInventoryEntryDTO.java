package com.buildit.inventory.application.dto;

import lombok.Data;
import org.springframework.hateoas.ResourceSupport;

import java.math.BigDecimal;

/**
 * Created by rain on 30.03.16.
 */
@Data
public class PlantInventoryEntryDTO extends ResourceSupport {
    Long _id;
    String name;
    String description;
    BigDecimal price;
}

