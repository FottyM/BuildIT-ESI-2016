import {Component} from 'angular2/core';

import {Http, RequestOptions} from 'angular2/http';
import {PlantCatalogService} from "../phr/catalog.service";
import  {buildItPort} from "../Configuration"
import {AuthenticationService} from "../login/auth.services";
import {ComponentInstruction, OnActivate, Router} from "angular2/router";
import 'rxjs/Rx'
class Plant {
    name: string;
    description: string;
    price: number;
}

class RentalPeriod {
    startDate: Date;
    endDate: Date;
}

export class XLink {
    _rel: string;
    href: string;
    method: string;
}

class PurchaseOrder {
    plant: Plant;
    rentalPeriod: RentalPeriod;
    status: string;
    total: number;
    _xlinks: XLink[];
}
class PlantHireRequest{
    status: string;
    price: number;
    rentalPeriod: RentalPeriod;
    plant: Plant;
    supplier:string;
}

@Component({
  templateUrl: '/app/orders/list.html'
})
export class POListingComponent implements OnActivate {
    orders: PurchaseOrder[];
    constructor (private router: Router,public http:Http,public catalog: PlantCatalogService, private authenticationService: AuthenticationService){

    }

    routerOnActivate(next: ComponentInstruction, prev: ComponentInstruction) {
        if (this.authenticationService.checkRoles(['WORK_ENGINEER'])) {
            var options = new RequestOptions({ headers: this.authenticationService.headers()});
            console.log(options)
            this.http.get(buildItPort+"/api/buildit/po", options)
                .subscribe(resp => {this.orders = resp.json()


                        console.log(this.orders);
                    },
                    err => console.log(`there was an error ${err.status}`));




            return true;
        }
        this.router.navigate(['PHRWizard']);
        return false;
    }


    follow(link: XLink) {

        this.catalog.executeExtensionQuery(link)

       
    }
}
