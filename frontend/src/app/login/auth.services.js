"use strict";
var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    var c = arguments.length, r = c < 3 ? target : desc === null ? desc = Object.getOwnPropertyDescriptor(target, key) : desc, d;
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") r = Reflect.decorate(decorators, target, key, desc);
    else for (var i = decorators.length - 1; i >= 0; i--) if (d = decorators[i]) r = (c < 3 ? d(r) : c > 3 ? d(target, key, r) : d(target, key)) || r;
    return c > 3 && r && Object.defineProperty(target, key, r), r;
};
var core_1 = require('angular2/core');
var http_1 = require('angular2/http');
var Configuration_1 = require('./../Configuration');
var AuthenticationService = (function () {
    function AuthenticationService(http, router) {
        this.http = http;
        this.router = router;
        this.loggedIn = false;
        this.roles = [];
    }
    AuthenticationService.prototype.authenticate = function (callback) {
        var _this = this;
        var options = new http_1.RequestOptions({ headers: this.headers() });
        this.http.get(Configuration_1.buildItPort + "/api/authenticate", options)
            .subscribe(function (resp) {
            _this.roles = resp.json();
            console.log(resp.json());
            _this.loggedIn = true;
            callback();
        });
    };
    AuthenticationService.prototype.headers = function () {
        var headers = new http_1.Headers();
        var code = localStorage.getItem('code');
        if (!!code) {
            headers.append('Authorization', "Basic " + code);
        }
        return headers;
    };
    AuthenticationService.prototype.headersJson = function () {
        var headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        var code = localStorage.getItem('code');
        if (!!code) {
            headers.append('Authorization', "Basic " + code);
        }
        return headers;
    };
    AuthenticationService.prototype.optionsValueJson = function () {
        var headers = new http_1.Headers({ 'Content-Type': 'application/json' });
        var code = localStorage.getItem('code');
        if (!!code) {
            headers.append('Authorization', "Basic " + code);
        }
        var options = new http_1.RequestOptions({ headers: headers });
        return options;
    };
    AuthenticationService.prototype.optionsValue = function () {
        var options = new http_1.RequestOptions({ headers: this.headers() });
        return options;
    };
    AuthenticationService.prototype.logout = function () {
        this.loggedIn = false;
        this.roles = [];
        localStorage.removeItem('code');
        this.router.navigate(['Login']);
    };
    AuthenticationService.prototype.checkRoles = function (roles) {
        if (this.isLoggedIn)
            return this.roles.filter(function (n) { return roles.indexOf(n) != -1; }).length > 0;
        else
            return false;
    };
    AuthenticationService.prototype.isLoggedIn = function () {
        return this.loggedIn;
    };
    AuthenticationService = __decorate([
        core_1.Injectable()
    ], AuthenticationService);
    return AuthenticationService;
}());
exports.AuthenticationService = AuthenticationService;
//# sourceMappingURL=auth.services.js.map