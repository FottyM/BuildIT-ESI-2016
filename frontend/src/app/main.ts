/// <reference path="../../node_modules/angular2/typings/browser.d.ts" />

import {bootstrap} from 'angular2/platform/browser';
import {Component} from 'angular2/core';
import {HTTP_BINDINGS} from 'angular2/http';
import {
    ROUTER_PROVIDERS, RouteConfig, Route, OnActivate, ComponentInstruction, Router,
    ROUTER_DIRECTIVES
} from "angular2/router";
import {AuthenticationService} from "./login/auth.services";
import {LogInComponent} from "./login/login.component";
import {POListingComponent} from './orders/purchase-order-listing.component';
import {PHRWizardComponent} from './phr/phr-wizard.component';
import {PlantCatalogService} from './phr/catalog.service';
import {ProcurementService} from './phr/procurement.service';
import {PHRListingComponent} from "./phr/phr-requests.component";
import 'rxjs/Rx';
import {Modify} from "./orders/modify";
import {Invoices} from "./invoices/po-invoice";

@Component({
  selector: 'app',
  directives: [ROUTER_DIRECTIVES],
  template: `
<section class="main-container">
    <div class="container-fluid">
        <div class="page-header filled img-bg">
            <div class="overlay-bg">
            </div>
            <div class="row">
                <div class="col-md-6">
                    <h2>BUILD IT </h2>
                    <p>Construction meets perfect</p>
                </div>
                <div class="col-md-6">
                    <ul class="list-page-breadcrumb">
                        <li> <a [routerLink]="['PHRWizard']">Create PHR</a></li>
                        <li><a [routerLink]="['PHRListing']">List all PHR's</a></li>
                        <li class="active-page">  <a [routerLink]="['POListing']">List all POs</a></li>
                        <li class="active-page">   <a [routerLink]="['Login']">Login Page</a></li>
                    </ul>
                </div>
            </div>
        </div>

    <nav>
     
       
       
    </nav>
   <router-outlet></router-outlet>

    </div>
    <!--Footer Start Here -->
    <footer class="footer-container">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-6 col-sm-6">
                    <div class="footer-left">
                        <span>© 2016 <a href="http://themeforest.net/user/westilian">Build it</a></span>
                    </div>
                </div>
                <div class="col-md-6 col-sm-6">
                    <div class="footer-right">
                        <span class="footer-meta">Build it &nbsp;<i class="fa fa-heart"></i>&nbsp;by&nbsp;<a href="#">Team</a></span>
                    </div>
                </div>
            </div>
        </div>
    </footer>
    <!--Footer End Here -->
</section>
    
  `

})
@RouteConfig([
  new Route({path: '/wizard', name: 'PHRWizard', component: PHRWizardComponent}),
  new Route({path: '/orders', name: 'POListing', component: POListingComponent}),
  new Route({path: '/phrs', name: 'PHRListing', component: PHRListingComponent}),
  new Route({path:'/login',name:'Login',component:LogInComponent}),
  new Route({path:'/modify',name:'Modify',component:Modify})


])
export class AppComponent implements OnActivate {
  constructor (private router: Router, private authenticationService: AuthenticationService) {}

  routerOnActivate(next: ComponentInstruction, prev: ComponentInstruction) {
    if (this.authenticationService.isLoggedIn()) return true;
    this.router.navigate(['PHRWizard']);
    return false;
  }
}

bootstrap(AppComponent, [HTTP_BINDINGS, ROUTER_PROVIDERS, PlantCatalogService, ProcurementService,,AuthenticationService]);

