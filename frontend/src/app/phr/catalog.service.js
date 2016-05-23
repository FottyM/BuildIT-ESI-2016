"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('angular2/core');
var Configuration_1 = require('../Configuration');
var PlantCatalogService = (function () {
    function PlantCatalogService(http, auth, router) {
        this.http = http;
        this.auth = auth;
        this.router = router;
        this.plants = [];
        this.extension = "";
        this.purchaseOrderUrl = "";
    }
    PlantCatalogService.prototype.executeQuery = function (query) {
        var _this = this;
        this.http.get(Configuration_1.buildItPort + "/api/buildit/?name=" + query.name + "&startDate=" + query.startDate + "&endDate=" + query.endDate, this.auth.optionsValue())
            .subscribe(function (response) {
            _this.plants = response.json();
            // console.log(response.json())
        }, null);
    };
    PlantCatalogService.prototype.executeExtensionQuery = function (url) {
        var _this = this;
        if (url.method == "POST") {
            if (url._rel == "extend") {
                this.purchaseOrderUrl = url.href;
                this.router.navigate(['Modify']);
            }
            else {
                this.http.post(url.href, null)
                    .subscribe(function (response) {
                    var x = response.json();
                    _this.extension = x.status.response;
                }, function (error) {
                    _this.extension = "Rejected";
                });
            }
        }
        else {
            this.http.delete(url.href)
                .subscribe(function (response) {
                var x = response.json();
                _this.extension = x.status.response;
            });
        }
    };
    PlantCatalogService = __decorate([
        core_1.Injectable()
    ], PlantCatalogService);
    return PlantCatalogService;
}());
exports.PlantCatalogService = PlantCatalogService;
//# sourceMappingURL=catalog.service.js.map