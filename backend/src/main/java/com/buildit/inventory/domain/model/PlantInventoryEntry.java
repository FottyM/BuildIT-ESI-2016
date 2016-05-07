package com.buildit.inventory.domain.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;

@Entity
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor(staticName = "of")
public class PlantInventoryEntry {
    @EmbeddedId
    PlantInventoryEntryID id;
    String name;
    String description;
    @Column(precision = 8, scale = 2)
    BigDecimal price;
}
