package com.buildit.hire.infrastructure.repository;

import com.buildit.hire.domain.model.PlantHireRequest;
import com.buildit.hire.domain.model.PlantHireRequestStatus;
import com.buildit.hire.domain.model.QPlantHireRequest;
import com.buildit.hire.domain.repository.CustomPlantHireRequestRepository;
import com.mysema.query.jpa.JPASubQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


public class PlantHireRequestRepositoryImpl implements CustomPlantHireRequestRepository {
    @Autowired
    EntityManager em;
    QPlantHireRequest qPlantHireRequest=QPlantHireRequest.plantHireRequest;
    @Override
    public Boolean hasPlantHireRequestByPoUrlAndPrice(String poUrl, BigDecimal price) {
         return new JPAQuery(em)
                .from(qPlantHireRequest)
                .where(qPlantHireRequest.poUrl.eq(poUrl).and(qPlantHireRequest.price.eq(price))).exists();
    }

    @Override
    public PlantHireRequest getPlantHireRequestByPoUrlAndPrice(String poUrl, BigDecimal price) {
        return new JPAQuery(em)
                .from(qPlantHireRequest)
                .where(qPlantHireRequest.poUrl.eq(poUrl).and(qPlantHireRequest.price.eq(price))).uniqueResult(qPlantHireRequest) ;
    }

    @Override
    public List<PlantHireRequest> getListOfPos() {
        return new JPAQuery(em).from(qPlantHireRequest).where(qPlantHireRequest.status.eq(PlantHireRequestStatus.OPEN)).list(qPlantHireRequest);
    }
}
