import {Component} from 'angular2/core';

import {Http, RequestOptions} from 'angular2/http';
import {buildItPort} from "../Configuration";
import {ProcurementService} from "../phr/procurement.service";
import {OnActivate, Router, ComponentInstruction} from "angular2/router";
import {AuthenticationService} from "../login/auth.services";
@Component({
      templateUrl: '/app/invoices/po-invoice-list.html'
})

export class Invoices implements OnActivate {
    results;


    constructor (private router: Router,public http:Http,private procurementService:ProcurementService
        ,private authenticationService: AuthenticationService){

    }

    routerOnActivate(next: ComponentInstruction, prev: ComponentInstruction) {
        if (this.authenticationService.checkRoles(['WORK_ENGINEER'])) {
            var options = new RequestOptions({ headers: this.authenticationService.headers()});

            this.http.get(buildItPort+"/api/buildit/invoice/", options)
                .subscribe(resp => {this.results = resp.json()

                    },
                    err => console.log(`there was an error ${err.status}`));

            return true;
        }
        this.router.navigate(['PHRWizard']);
        return false;
    }
    sendInvoice(poid:string,email:string,total:string){

        this.procurementService.executeQueryInvoice(poid,email,total)
        
    }
 }

