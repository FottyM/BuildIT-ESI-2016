package com.buildit.hire.application.dto;


import com.buildit.common.application.dto.BusinessPeriodDTO;
import com.buildit.hire.domain.model.PlantHireRequestStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.ResourceSupport;


import java.math.BigDecimal;

@Data

public class PlantHireRequestDTO extends ResourceSupport {
    Long _id;
    Long constructionSiteId;
    String supplier;
    Long plantId;
    BigDecimal price;
    BusinessPeriodDTO rentalPeriod;
    PlantHireRequestStatus status;
    Boolean isPaid;
    String poUrl;


}
