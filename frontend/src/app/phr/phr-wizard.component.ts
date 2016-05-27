import {Component} from 'angular2/core';

import {Query, Plant} from './declarations';

import {QueryComponent} from './query/query.component';
import {SelectionComponent} from './selection/selection.component';
import {OverviewComponent} from './overview/overview.component';

import {PlantCatalogService} from './catalog.service';
import {ProcurementService} from './procurement.service';
import {AuthenticationService} from "../login/auth.services";
import {OnActivate, ComponentInstruction, Router} from "angular2/router";
import {RequestOptions} from "angular2/http";

@Component({
    directives: [QueryComponent, SelectionComponent, OverviewComponent],
    templateUrl: '/app/phr/wizard.html',
    styles: [`
        .nav-tabs > li > a {
        -webkit-border-radius: 0;
        -moz-border-radius: 0;
        border-radius: 0;
        background-color: #ddd;
        border-color: #ffffff;
        color:#888;
        text-align:left;
        font-size:large;
        padding-top: 18px;
        padding-bottom: 18px;
        padding-left: 30px;
        }
        .nav-tabs > li.active > a,
        .nav-tabs > li.active > a:hover,
        .nav-tabs > li.active > a:focus {
            color: #fff;
            background-color: #000066;
        }
  `]
})
export class PHRWizardComponent  implements OnActivate {
    plant: Plant = new Plant();
    query: Query = new Query();
    
    isQueryTabActive = true;
    isSelectionTabActive = false;
    isReviewTabActive = false;
    options= null;
    constructor(private router: Router,public catalog: PlantCatalogService, public procurementService: ProcurementService, private authenticationService: AuthenticationService) {
    }

    routerOnActivate(next: ComponentInstruction, prev: ComponentInstruction) {
        if (this.authenticationService.checkRoles(['WORK_ENGINEER','SITE_ENGINEER'])) {
            var options = new RequestOptions({ headers: this.authenticationService.headers()});
               this.options=options;
            return true;
        }
        this.router.navigate(['Login']);
        return false;
    }
    executeQuery(query:Query) {
        this.query = query;
        this.catalog.executeQuery(query);
        this.isQueryTabActive = false;
        this.isSelectionTabActive = true;
    }
    selectPlant(plant:Plant) {
        this.plant = plant;
        console.log(plant)
        this.procurementService.setPlant(plant,this.query);
        this.isSelectionTabActive = false;
        this.isReviewTabActive = true;
    }
    backToQueryTab(){
        this.router.navigate(['PHRWizard']);


    }
}
