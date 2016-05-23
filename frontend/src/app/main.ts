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
    <nav>
      <a [routerLink]="['PHRWizard']">Create PHR</a>
       <a [routerLink]="['PHRListing']">List all PHR's</a>
      <a [routerLink]="['POListing']">List all POs</a>
      <a [routerLink]="['Login']">Login Page</a>
    </nav>
    <h1> WELCOME TO BUILD IT</h1>
    
    <router-outlet></router-outlet>
  `
})
@RouteConfig([
  new Route({path: '/wizard', name: 'PHRWizard', component: PHRWizardComponent}),
  new Route({path: '/orders', name: 'POListing', component: POListingComponent}),
  new Route({path: '/phrs', name: 'PHRListing', component: PHRListingComponent}),
  new Route({path:'/login',name:'Login',component:LogInComponent}),
  new Route({path:'/modify',name:'Modify',component:Modify}),
  new Route({path:'/Invoices',name:'Invoices',component:Invoices})


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

