import {Component} from 'angular2/core';

import {Http, RequestOptions} from 'angular2/http';
import  {buildItPort} from  './../Configuration'


import {XLink} from "../orders/purchase-order-listing.component.ts";
import {PurchaseOrder, PlantHireRequest} from "./declarations";
import {ProcurementService} from "./procurement.service";
import {OnActivate, Router, ComponentInstruction} from "angular2/router";
import {AuthenticationService} from "../login/auth.services";



@Component({

  templateUrl: '/app/phr/phr-requests-list.html'
})
export class PHRListingComponent  implements OnActivate {
    plantRequests: PlantHireRequest[];
    constructor (public http:Http, public catalog: ProcurementService, private authenticationService: AuthenticationService,private router: Router) {

    }

    routerOnActivate(next: ComponentInstruction, prev: ComponentInstruction) {
        if (this.authenticationService.checkRoles(['WORK_ENGINEER'])) {
            var options = new RequestOptions({ headers: this.authenticationService.headers()});

            this.http.get(buildItPort+"/api/buildit/phrs/", options)
                .subscribe(resp => {
                    this.plantRequests = resp.json();
                    console.log(resp.json())


                },
                    err => console.log(`there was an error ${err.status}`));
            return true;
        }
        this.router.navigate(['Login']);
        return false;
    }
    follow(link: XLink) {

        this.catalog.executeAcceptQuery(link);
    }
    response(id,bo:boolean){

        this.catalog.executePhr(id,bo);

    }
}
