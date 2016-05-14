package com.buildit.inventory.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.buildit.inventory.domain.model.PlantReservationID;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPlantReservationID is a Querydsl query type for PlantReservationID
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QPlantReservationID extends BeanPath<PlantReservationID> {

    private static final long serialVersionUID = 1247271639L;

    public static final QPlantReservationID plantReservationID = new QPlantReservationID("plantReservationID");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QPlantReservationID(String variable) {
        super(PlantReservationID.class, forVariable(variable));
    }

    public QPlantReservationID(Path<? extends PlantReservationID> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlantReservationID(PathMetadata<?> metadata) {
        super(PlantReservationID.class, metadata);
    }

}

