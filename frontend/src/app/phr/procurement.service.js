"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('angular2/core');
var moment_1 = require('moment');
var Configuration_1 = require('../Configuration');
var declarations_1 = require('./declarations');
var ProcurementService = (function () {
    function ProcurementService(http, auth, router, catalog) {
        this.http = http;
        this.auth = auth;
        this.router = router;
        this.catalog = catalog;
        this.phr = new declarations_1.PlantHireRequest();
    }
    ProcurementService.prototype.setPlant = function (plant, query) {
        this.phr.plant = plant;
        this.phr.rentalPeriod = new declarations_1.RentalPeriod();
        this.phr.rentalPeriod.startDate = query.startDate;
        this.phr.rentalPeriod.endDate = query.endDate;
        this.phr.total = (moment_1["default"](query.endDate).diff(moment_1["default"](query.startDate), 'days') + 1) * plant.price;
    };
    ProcurementService.prototype.executeAcceptQuery = function (url) {
        var _this = this;
        if (url.method == "POST") {
            this.http.post(url.href, null)
                .subscribe(function (response) {
                var x = JSON.parse(JSON.stringify(response));
                var xx = JSON.parse(x["_body"]);
                if (xx["status"] == "accepted") {
                    var elemId = url.href.split('/')[5];
                    // $("#"+elemId).css("background-color","lightgreen")
                    _this.returnedStatus = "accepted";
                }
            }, function (error) {
                alert("Unhandled exception: 0x0C000005");
            });
        }
        else if (url.method == "DELETE") {
            this.http.delete(url.href)
                .subscribe(function (response) {
                var x = JSON.parse(JSON.stringify(response));
                var xx = JSON.parse(x["_body"]);
                // if(xx["status"] == "deleted"){
                //     var elemId = url.href.split('/')[5];
                //     $("#"+elemId).remove();
                //     this.returnedStatus =  "rejected";
                // }
            });
        }
    };
    ProcurementService.prototype.executePlantHireRequest = function () {
        var _this = this;
        this.http.post(Configuration_1.buildItPort + "/api/buildit/", JSON.stringify({
            plantUrl: this.phr.plant.url,
            price: this.phr.plant.price,
            rentalPeriod: this.phr.rentalPeriod
        }), this.auth.optionsValueJson())
            .subscribe(function (response) {
            if (response.status == 201) {
                _this.router.navigate(['PHRListing']);
            }
            else {
                alert("Sorry something went wrong");
            }
        }, function (error) {
            alert("Sorry something went wrong");
        });
    };
    ProcurementService.prototype.executePhr = function (id, accept) {
        var _this = this;
        var bb = "reject";
        if (accept) {
            bb = "accept";
        }
        this.http.post(Configuration_1.buildItPort + "/api/buildit/phrs/" + id + "/" + bb, null, this.auth.optionsValue())
            .subscribe(function (response) {
            if (response.status == 201) {
                _this.router.navigate(['POListing']);
            }
            else {
                alert("Sorry something went wrong");
            }
        }, function (error) {
            alert("error");
        });
    };
    ProcurementService.prototype.modifyPurchaseOrder = function (query) {
        console.log(this.auth.optionsValue());
        var x = Configuration_1.buildItPort + "/api/buildit/po/extension";
        this.http.post(x, JSON.stringify({
            poUrl: this.catalog.purchaseOrderUrl,
            businessPeriodDTO: { startDate: query.startDate, endDate: query.endDate }
        }), this.auth.optionsValueJson())
            .subscribe(function (response) {
            console.log(response);
        }, function (error) {
            alert("error");
        });
    };
    ProcurementService = __decorate([
        core_1.Injectable()
    ], ProcurementService);
    return ProcurementService;
}());
exports.ProcurementService = ProcurementService;
//# sourceMappingURL=procurement.service.js.map