package com.buildit.inventory.domain.model;

import com.buildit.common.domain.model.BusinessPeriod;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor(staticName = "of")
public class PlantReservation {
    @EmbeddedId
    PlantReservationID id;

    @Embedded
    BusinessPeriod schedule;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name="id", column=@Column(name="plant_id"))})
    PlantInventoryItemID plant;
}
