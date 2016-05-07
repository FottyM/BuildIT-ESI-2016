package com.buildit.sales.domain.model;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QPurchaseOrder is a Querydsl query type for PurchaseOrder
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QPurchaseOrder extends EntityPathBase<PurchaseOrder> {

    private static final long serialVersionUID = 94278562L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPurchaseOrder purchaseOrder = new QPurchaseOrder("purchaseOrder");

    public final ListPath<PurchaseOrderExtensionID, QPurchaseOrderExtensionID> extensions = this.<PurchaseOrderExtensionID, QPurchaseOrderExtensionID>createList("extensions", PurchaseOrderExtensionID.class, QPurchaseOrderExtensionID.class, PathInits.DIRECT2);

    public final QPurchaseOrderID id;

    public final DatePath<java.time.LocalDate> issueDate = createDate("issueDate", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> paymentSchedule = createDate("paymentSchedule", java.time.LocalDate.class);

    public final com.buildit.inventory.domain.model.QPlantInventoryEntryID plant;

    public final com.buildit.common.domain.model.QBusinessPeriod rentalPeriod;

    public final ListPath<com.buildit.inventory.domain.model.PlantReservationID, com.buildit.inventory.domain.model.QPlantReservationID> reservations = this.<com.buildit.inventory.domain.model.PlantReservationID, com.buildit.inventory.domain.model.QPlantReservationID>createList("reservations", com.buildit.inventory.domain.model.PlantReservationID.class, com.buildit.inventory.domain.model.QPlantReservationID.class, PathInits.DIRECT2);

    public final EnumPath<POStatus> status = createEnum("status", POStatus.class);

    public final NumberPath<java.math.BigDecimal> total = createNumber("total", java.math.BigDecimal.class);

    public QPurchaseOrder(String variable) {
        this(PurchaseOrder.class, forVariable(variable), INITS);
    }

    public QPurchaseOrder(Path<? extends PurchaseOrder> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPurchaseOrder(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QPurchaseOrder(PathMetadata<?> metadata, PathInits inits) {
        this(PurchaseOrder.class, metadata, inits);
    }

    public QPurchaseOrder(Class<? extends PurchaseOrder> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QPurchaseOrderID(forProperty("id")) : null;
        this.plant = inits.isInitialized("plant") ? new com.buildit.inventory.domain.model.QPlantInventoryEntryID(forProperty("plant")) : null;
        this.rentalPeriod = inits.isInitialized("rentalPeriod") ? new com.buildit.common.domain.model.QBusinessPeriod(forProperty("rentalPeriod")) : null;
    }

}

