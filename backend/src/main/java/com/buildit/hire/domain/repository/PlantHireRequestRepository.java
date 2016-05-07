package com.buildit.hire.domain.repository;

import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PlantHireRequestRepository extends JpaRepository<PlantHireRequest, PlantHireRequestID>, CustomPlantHireRequestRepository {


}
