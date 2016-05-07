package com.buildit.hire.domain.repository;

import com.buildit.hire.domain.model.PlantHireRequest;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface CustomPlantHireRequestRepository {
   public Boolean hasPlantHireRequestByPoUrlAndPrice(String poUrl, BigDecimal price);

   PlantHireRequest getPlantHireRequestByPoUrlAndPrice(String poUrl, BigDecimal price);
}
