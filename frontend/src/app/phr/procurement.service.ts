import {Injectable} from 'angular2/core';
import {Http, RequestOptions, Headers} from 'angular2/http';

import moment from 'moment';
import {buildItPort} from '../Configuration'

import {Plant, Query, PlantHireRequest, RentalPeriod} from './declarations';
import {XLink, Invoice} from "../orders/purchase-order-listing.component";
import {AuthenticationService} from "../login/auth.services";
import {Router} from "angular2/router";
import {PlantCatalogService} from "./catalog.service";

@Injectable()
export class ProcurementService {
    phr:PlantHireRequest = new PlantHireRequest();

    constructor(public http:Http, private auth:AuthenticationService, private router:Router, private catalog:PlantCatalogService) {
    }

    setPlant(plant:Plant, query:Query) {
        this.phr.plant = plant;
        this.phr.rentalPeriod = new RentalPeriod();
        this.phr.rentalPeriod.startDate = query.startDate;
        this.phr.rentalPeriod.endDate = query.endDate;

        this.phr.total = (moment(query.endDate).diff(moment(query.startDate), 'days') + 1) * plant.price;
    }

    returnedStatus:String;

    executeAcceptQuery(url:XLink) {

        if (url.method == "POST") {
            this.http.post(url.href, null)
                .subscribe(response => {
                        var x = JSON.parse(JSON.stringify(response));
                        var xx = JSON.parse(x["_body"]);
                        if (xx["status"] == "accepted") {
                            var elemId = url.href.split('/')[5];
                            // $("#"+elemId).css("background-color","lightgreen")
                            this.returnedStatus = "accepted";
                        }

                    },
                    error => {
                        alert("Unhandled exception: 0x0C000005");

                    });
        } else if (url.method == "DELETE") {
            this.http.delete(url.href)
                .subscribe(response => {
                    var x = JSON.parse(JSON.stringify(response));
                    var xx = JSON.parse(x["_body"]);
                    // if(xx["status"] == "deleted"){
                    //     var elemId = url.href.split('/')[5];
                    //     $("#"+elemId).remove();
                    //     this.returnedStatus =  "rejected";
                    // }

                });
        }
    }

    executePlantHireRequest() {

        this.http.post(buildItPort + "/api/buildit/", JSON.stringify({
            plantUrl: this.phr.plant.url,
            price: this.phr.plant.price,
            rentalPeriod: this.phr.rentalPeriod
        }), this.auth.optionsValueJson())
            .subscribe(response => {


                    if (response.status == 201) {


                        this.router.navigate(['PHRListing']);

                    }
                    else {
                        alert("Sorry something went wrong")
                    }


                },
                error => {
                    alert("Sorry something went wrong")

                });

    }

    executePhr(id, accept:boolean) {
        var bb = "reject"
        if (accept) {
            bb = "accept";
        }

        this.http.post(buildItPort + "/api/buildit/phrs/" + id + "/" + bb, null, this.auth.optionsValue())
            .subscribe(response => {

                    if (response.status == 201) {


                        this.router.navigate(['POListing']);

                    }
                    else {
                        alert("Sorry something went wrong")
                    }
                },
                error => {
                    alert("error");

                });

    }


    modifyPurchaseOrder(query:Query) {


        var regex = /\d+/g;
        var string =  this.catalog.purchaseOrderUrl;
        var matches = string.match(regex);



        var x = buildItPort + "/api/buildit/po/"+matches[matches.length-1]+"/extension";

        this.http.post(x,
            JSON.stringify(
                {startDate: query.startDate, endDate: query.endDate}
            ), this.auth.optionsValueJson())
            .subscribe(response => {

                    this.router.navigate(['POListing']);
                },
                error => {
                    alert("error");

                });


    }
    executeQueryInvoice(poId:string,email:string,total:string) {
        var headers = new Headers();
        headers.append('Content-type', 'application/json');

        this.http.post(buildItPort+"/api/buildit/invoice/makepayment", JSON.stringify({"poUrl":poId,"email":email,"total":total}),this.auth.optionsValueJson())
            .subscribe(response => {

                    alert("success invoice sent")
                },
                error => {
                    alert("success invoice sent")
                    this.router.navigate(['PHRListing']);

                });
    }

}