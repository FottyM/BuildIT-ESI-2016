package com.buildit.orders.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.buildit.orders.domain.model.PurchaseOrderExtensionID;
import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QPurchaseOrderExtensionID is a Querydsl query type for PurchaseOrderExtensionID
 */
@Generated("com.mysema.query.codegen.EmbeddableSerializer")
public class QPurchaseOrderExtensionID extends BeanPath<PurchaseOrderExtensionID> {

    private static final long serialVersionUID = 538111128L;

    public static final QPurchaseOrderExtensionID purchaseOrderExtensionID = new QPurchaseOrderExtensionID("purchaseOrderExtensionID");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QPurchaseOrderExtensionID(String variable) {
        super(PurchaseOrderExtensionID.class, forVariable(variable));
    }

    public QPurchaseOrderExtensionID(Path<? extends PurchaseOrderExtensionID> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPurchaseOrderExtensionID(PathMetadata<?> metadata) {
        super(PurchaseOrderExtensionID.class, metadata);
    }

}

