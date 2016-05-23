"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('angular2/core');
var http_1 = require('angular2/http');
var Configuration_1 = require("../Configuration");
require('rxjs/Rx');
var Plant = (function () {
    function Plant() {
    }
    return Plant;
}());
var RentalPeriod = (function () {
    function RentalPeriod() {
    }
    return RentalPeriod;
}());
var XLink = (function () {
    function XLink() {
    }
    return XLink;
}());
exports.XLink = XLink;
var PurchaseOrder = (function () {
    function PurchaseOrder() {
    }
    return PurchaseOrder;
}());
var PlantHireRequest = (function () {
    function PlantHireRequest() {
    }
    return PlantHireRequest;
}());
var POListingComponent = (function () {
    function POListingComponent(router, http, catalog, authenticationService) {
        this.router = router;
        this.http = http;
        this.catalog = catalog;
        this.authenticationService = authenticationService;
    }
    POListingComponent.prototype.routerOnActivate = function (next, prev) {
        var _this = this;
        if (this.authenticationService.checkRoles(['WORK_ENGINEER'])) {
            var options = new http_1.RequestOptions({ headers: this.authenticationService.headers() });
            console.log(options);
            this.http.get(Configuration_1.buildItPort + "/api/buildit/po", options)
                .subscribe(function (resp) {
                _this.orders = resp.json();
                console.log(_this.orders);
            }, function (err) { return console.log("there was an error " + err.status); });
            return true;
        }
        this.router.navigate(['PHRWizard']);
        return false;
    };
    POListingComponent.prototype.follow = function (link) {
        this.catalog.executeExtensionQuery(link);
    };
    POListingComponent = __decorate([
        core_1.Component({
            templateUrl: '/app/orders/list.html'
        })
    ], POListingComponent);
    return POListingComponent;
}());
exports.POListingComponent = POListingComponent;
//# sourceMappingURL=purchase-order-listing.component.js.map