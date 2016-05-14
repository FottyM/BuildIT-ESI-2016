package com.buildit.sales.domain.model;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
public class PurchaseOrder {

    @EmbeddedId
    PurchaseOrderID id;

    String link;

}
