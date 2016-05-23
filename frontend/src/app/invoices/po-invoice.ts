import {Component} from 'angular2/core';

import {Http} from 'angular2/http';


import {Invoice} from "../orders/purchase-order-listing.component.ts";
import {PurchaseOrder} from "../phr/declarations";
import {buildItPort} from "../Configuration";
import {ProcurementService} from "../phr/procurement.service";







@Component({

  templateUrl: '/app/invoice/po-invoice-list.html'
})
export class Invoices {
    orders: PurchaseOrder[];
    constructor (public http:Http,private procurementService:ProcurementService) {
        this.http.get(buildItPort+"/api/rentit/invoice/orders")
            .subscribe(resp => this.orders = resp.json());
    }
    follow(invoice: Invoice) {

        this.procurementService.executeQuery(invoice);
    }
}
