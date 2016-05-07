package com.buildit.hire.domain.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPlantHireRequestID is a Querydsl query type for PlantHireRequestID
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QPlantHireRequestID extends BeanPath<PlantHireRequestID> {

    private static final long serialVersionUID = 409502882L;

    public static final QPlantHireRequestID plantHireRequestID = new QPlantHireRequestID("plantHireRequestID");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QPlantHireRequestID(String variable) {
        super(PlantHireRequestID.class, forVariable(variable));
    }

    public QPlantHireRequestID(Path<? extends PlantHireRequestID> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPlantHireRequestID(PathMetadata<?> metadata) {
        super(PlantHireRequestID.class, metadata);
    }

}

