package com.buildit.hire.domain.model;

import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.common.domain.model.BusinessPeriod;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)

public class PlantHireRequest {
    @EmbeddedId
    PlantHireRequestID id;
    Long constructionSiteId;
    String supplier;
    Long plantId;
    @Column(precision = 8, scale = 2)
    BigDecimal price;
    @Embedded
    BusinessPeriod rentalPeriod;
    @Enumerated(EnumType.STRING)
    PlantHireRequestStatus status;
    Boolean isPaid;
    String poUrl;
    String comment;

    public static PlantHireRequest of(PlantHireRequestID id, Long plantId, BusinessPeriodDTO rentalPeriod, BigDecimal price) {
        BusinessPeriod bp = BusinessPeriod.of(rentalPeriod.getStartDate(),rentalPeriod.getEndDate());

        PlantHireRequest phr = new PlantHireRequest();
        phr.id = id;
        phr.plantId = plantId;
        phr.rentalPeriod = bp;
        phr.price = price;
        phr.status = PlantHireRequestStatus.PENDING;
        phr.isPaid = false;
        return phr;
    }

    public static PlantHireRequest of(PlantHireRequestID id, Long plantId, BusinessPeriodDTO rentalPeriod, BigDecimal price, PlantHireRequestStatus status) {
        BusinessPeriod bp = BusinessPeriod.of(rentalPeriod.getStartDate(),rentalPeriod.getEndDate());

        PlantHireRequest phr = new PlantHireRequest();
        phr.id = id;
        phr.plantId = plantId;
        phr.rentalPeriod = bp;
        phr.price = price;
        phr.status = status;
        phr.isPaid = false;
        return phr;
    }

    public static PlantHireRequest of(PlantHireRequestID id, Long plantId, BusinessPeriodDTO rentalPeriod, BigDecimal price, PlantHireRequestStatus status, String comment) {
        BusinessPeriod bp = BusinessPeriod.of(rentalPeriod.getStartDate(),rentalPeriod.getEndDate());

        PlantHireRequest phr = new PlantHireRequest();
        phr.id = id;
        phr.plantId = plantId;
        phr.rentalPeriod = bp;
        phr.price = price;
        phr.status = status;
        phr.isPaid = false;
        phr.comment = comment;
        return phr;
    }

}
