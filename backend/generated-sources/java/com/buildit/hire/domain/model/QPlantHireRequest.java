package com.buildit.hire.domain.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QPlantHireRequest is a Querydsl query type for PlantHireRequest
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPlantHireRequest extends EntityPathBase<PlantHireRequest> {

    private static final long serialVersionUID = 1032827207L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPlantHireRequest plantHireRequest = new QPlantHireRequest("plantHireRequest");

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> constructionSiteId = createNumber("constructionSiteId", Long.class);

    public final QPlantHireRequestID id;

    public final BooleanPath isPaid = createBoolean("isPaid");

    public final NumberPath<Long> plantId = createNumber("plantId", Long.class);

    public final StringPath plantUrl = createString("plantUrl");

    public final StringPath poUrl = createString("poUrl");

    public final NumberPath<java.math.BigDecimal> price = createNumber("price", java.math.BigDecimal.class);

    public final com.buildit.common.domain.model.QBusinessPeriod rentalPeriod;

    public final EnumPath<PlantHireRequestStatus> status = createEnum("status", PlantHireRequestStatus.class);

    public final StringPath supplier = createString("supplier");

    public QPlantHireRequest(String variable) {
        this(PlantHireRequest.class, forVariable(variable), INITS);
    }

    public QPlantHireRequest(Path<? extends PlantHireRequest> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPlantHireRequest(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPlantHireRequest(PathMetadata<?> metadata, PathInits inits) {
        this(PlantHireRequest.class, metadata, inits);
    }

    public QPlantHireRequest(Class<? extends PlantHireRequest> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QPlantHireRequestID(forProperty("id")) : null;
        this.rentalPeriod = inits.isInitialized("rentalPeriod") ? new com.buildit.common.domain.model.QBusinessPeriod(forProperty("rentalPeriod")) : null;
    }

}

