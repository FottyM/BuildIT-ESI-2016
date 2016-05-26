/// <reference path="../../node_modules/angular2/typings/browser.d.ts" />

import {bootstrap} from 'angular2/platform/browser';
import {Component} from 'angular2/core';
import {HTTP_BINDINGS} from 'angular2/http';
import {
    RouteConfig, Route, ROUTER_PROVIDERS, ROUTER_DIRECTIVES, OnActivate, Router,
    ComponentInstruction
} from 'angular2/router';

import {AuthenticationService} from "./login/auth.services";
import {ProcurementService} from "./phr/procurement.service";
import {PHRWizardComponent} from "./phr/phr-wizard.component";
import {LogInComponent} from "./login/login.component";
import {Modify} from "./orders/modify";
import {POListingComponent} from "./orders/purchase-order-listing.component";
import {PlantCatalogService} from "./phr/catalog.service";
import {PHRListingComponent} from "./phr/phr-requests.component";
import {Invoices} from "./invoices/po-invoice";


@Component({
  selector: 'app',
  directives: [ROUTER_DIRECTIVES],
  template: `
    
      
    
    <div id="sb-site">


    
    <div id="page-wrapper" >

        <div id="page-sidebar" style="height: 100%">
            <div id="header-logo" class="logo-bg"><a href="index-2.html" class="logo-content-big" title="DelightUI">Rentit </a>
            </div>


            <!--    header-->



            <div class="scroll-sidebar">
                <ul id="sidebar-menu">
                    <li class="header"><span>Menu Items</span>
                    </li>
                  
                    <li>   <a [routerLink]="['Login']"><i class="glyph-icon icon-linecons-diamond"></i> <span>Login</span></a></li>
                     <li>   <a [routerLink]="['PHRWizard']"><i class="glyph-icon icon-linecons-diamond"></i> <span>Create</span></a></li>
                        <li>   <a [routerLink]="['PHRListing']"><i class="glyph-icon icon-linecons-diamond"></i> <span>PHR Requests</span></a></li>
                         <li>   <a [routerLink]="['POListing']"><i class="glyph-icon icon-linecons-diamond"></i> <span>Purchase Orders</span></a></li>
                       <li>   <a [routerLink]="['Invoices']"><i class="glyph-icon icon-linecons-diamond"></i> <span>Pending Invoices</span></a></li>


                </ul>
            </div>
        </div>



        <!--   body  -->
        <div id="page-content-wrapper">
            <div id="page-content"  style="margin-left: 10%">


    
  
    

                <div id="page-title">
           
          <nav>
      
           
    </nav>
    <h1> WELCOME TO BUILD IT </h1>
     <router-outlet></router-outlet>
                </div>

            </div>
        </div>
    </div>
   
</div>
  `
})
@RouteConfig([
  new Route({path: '/wizard', name: 'PHRWizard', component: PHRWizardComponent}),
  new Route({path: '/phrs', name: 'PHRListing', component: PHRListingComponent}),
   new Route({path: '/orders', name: 'POListing', component: POListingComponent}),
   new Route({path:'/login',name:'Login',component:LogInComponent}),
   new Route({path:'/modify',name:'Modify',component:Modify}),
    new Route({path:'/invoices',name:'Invoices',component:Invoices})
    
 ])
export class AppComponent implements OnActivate {
  constructor (private router: Router, private authenticationService: AuthenticationService) {}

  routerOnActivate(next: ComponentInstruction, prev: ComponentInstruction) {
    if (this.authenticationService.isLoggedIn()) return true;
    this.router.navigate(['PHRWizard']);
    return false;
  }
}

bootstrap(AppComponent, [HTTP_BINDINGS, ROUTER_PROVIDERS, PlantCatalogService, ProcurementService,AuthenticationService]);

