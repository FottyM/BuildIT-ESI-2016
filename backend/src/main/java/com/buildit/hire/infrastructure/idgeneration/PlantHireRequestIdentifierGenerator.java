package com.buildit.hire.infrastructure.idgeneration;

import com.buildit.common.infrastructure.HibernateBasedIdentifierGenerator;
import com.buildit.hire.domain.model.PlantHireRequestID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlantHireRequestIdentifierGenerator {
    @Autowired
    HibernateBasedIdentifierGenerator hibernateGenerator;

    public PlantHireRequestID nextPlantHireRequestID() {
        return PlantHireRequestID.of(hibernateGenerator.getID("PlantHireRequestIDSequence"));
    }
}
