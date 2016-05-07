/// <reference path="../../node_modules/angular2/typings/browser.d.ts" />
System.register(["angular2/http", "rxjs/Rx", 'angular2/platform/browser', 'angular2/core'], function(exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
        var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
        if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
        else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
        return c > 3 && r && Object.defineProperty(target, key, r), r;
    };
    var __metadata = (this && this.__metadata) || function (k, v) {
        if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
    };
    var http_1, browser_1, core_1;
    var Plant, PlantComponent, AppComponent;
    return {
        setters:[
            function (http_1_1) {
                http_1 = http_1_1;
            },
            function (_1) {},
            function (browser_1_1) {
                browser_1 = browser_1_1;
            },
            function (core_1_1) {
                core_1 = core_1_1;
            }],
        execute: function() {
            Plant = (function () {
                function Plant() {
                }
                return Plant;
            }());
            PlantComponent = (function () {
                function PlantComponent(http) {
                    var _this = this;
                    this.http = http;
                    this.http.get('http://localhost:8080/api/plants')
                        .subscribe(function (resp) { return _this.plants = resp.json(); });
                }
                PlantComponent = __decorate([
                    core_1.Component({
                        selector: 'plants',
                        template: "\n  {{plants|json}}\n  "
                    }), 
                    __metadata('design:paramtypes', [http_1.Http])
                ], PlantComponent);
                return PlantComponent;
            }());
            AppComponent = (function () {
                function AppComponent() {
                }
                AppComponent = __decorate([
                    core_1.Component({
                        selector: 'app',
                        directives: [PlantComponent],
                        template: "Hello world\n  <p>\n  <plants>\n  "
                    }), 
                    __metadata('design:paramtypes', [])
                ], AppComponent);
                return AppComponent;
            }());
            exports_1("AppComponent", AppComponent);
            browser_1.bootstrap(AppComponent, [http_1.HTTP_PROVIDERS]);
        }
    }
});

//# sourceMappingURL=main.js.map
