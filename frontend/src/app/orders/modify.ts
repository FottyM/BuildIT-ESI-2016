import {Component, Output, EventEmitter} from 'angular2/core';

import {Query} from '../phr/declarations';
import {ProcurementService} from "../phr/procurement.service";

@Component({
    selector: 'modify',
    templateUrl: '/app/orders/modify.html'
})
export class Modify {
   constructor(private procurement:ProcurementService){

   }

    query: Query = new Query();
    executeQuery() {
        this.procurement.modifyPurchaseOrder(this.query)


    }
}
