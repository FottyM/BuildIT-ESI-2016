package com.buildit.sales.domain.model;

import com.buildit.common.domain.model.BusinessPeriod;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class PurchaseOrderExtension {
    @EmbeddedId
    PurchaseOrderExtensionID id;

    @Embedded
    @AttributeOverrides({@AttributeOverride(name="id", column=@Column(name="order_id"))})
    PurchaseOrderID purchaseOrder;
    @Embedded
    BusinessPeriod rentalPeriod;

    @Enumerated(EnumType.STRING)
    POStatus status;

    public static PurchaseOrderExtension of(PurchaseOrderExtensionID id, PurchaseOrderID purchaseOrder, BusinessPeriod period,POStatus poStatus) {
        PurchaseOrderExtension po = new PurchaseOrderExtension();
        po.id = id;
        po.purchaseOrder = purchaseOrder;
        po.rentalPeriod = period;
        po.status=poStatus;
        return po;
    }

//    public void confirmReservation(PlantReservationID reservation, BigDecimal plantPrice) {
//        reservations.add(reservation);
//        total = plantPrice.multiply(BigDecimal.valueOf(rentalPeriod.numberOfWorkingDays()));
//        //status = POStatus.OPEN;
//    }


}
