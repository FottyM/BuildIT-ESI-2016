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
                        <span>© 2015 <a href="http://themeforest.net/user/westilian">westilian</a></span>
                    </div>
                </div>
                <div class="col-md-6 col-sm-6">
                    <div class="footer-right">
                        <span class="footer-meta">Crafted with&nbsp;<i class="fa fa-heart"></i>&nbsp;by&nbsp;<a href="http://themeforest.net/user/westilian">westilian</a></span>
                    </div>
                </div>
            </div>
        </div>
    </footer>
    <!--Footer End Here -->
</section>
   

 <aside class="leftbar material-leftbar">
    <div class="left-aside-container">
        <div class="user-profile-container">
            <div class="user-profile clearfix">
                <div class="admin-user-thumb">
                    <img src="assets/images/avatar/jaman_01.jpg" alt="admin">
                </div>
                <div class="admin-user-info">
                    <ul>
                        <li><a href="#">Site Engneer</a></li>
                        <li><a href="#">siteengineer@buildit.com</a></li>
                    </ul>
                </div>
            </div>
            <div class="admin-bar">
                <ul>
                    <li><a href="#"><i class="zmdi zmdi-power"></i>
                    </a>
                    </li>
                    <li><a href="#"><i class="zmdi zmdi-account"></i>
                    </a>
                    </li>
                    <li><a href="#"><i class="zmdi zmdi-key"></i>
                    </a>
                    </li>
                    <li><a href="#"><i class="zmdi zmdi-settings"></i>
                    </a>
                    </li>
                </ul>
            </div>
        </div>
        <ul class="list-accordion">
            <li class="list-title">Menus</li>
            <li>
                <a [routerLink]="['PHRWizard']" ><i class="zmdi zmdi-view-dashboard"></i><span class="list-label">Create PHR</span></a>
                
            </li>
            <li class="list-title">Others</li>
            <li>
                <a [routerLink]="['POListing']"><i class="zmdi zmdi-view-web"></i><span class="list-label">Purchase Orders</span></a>
                
            </li>
            <li>
                <a [routerLink]="['PHRListing']"><i class="zmdi zmdi-check"></i><span class="list-label">Plant Hire Requests</span></a>
                
            </li>
            <!--<li>-->
                <!--<a [routerLink]="['Invoices']"><i class="zmdi zmdi-check"></i><span class="list-label">Plant Invoices</span></a>-->
                <!---->
            <!--</li>-->
        </ul>
    </div>
</aside>
    
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

