import {Component, Input} from 'angular2/core';

import {PlantHireRequest} from '../declarations';
import {ProcurementService} from "../procurement.service";

@Component({
    selector: 'overview-component',
  templateUrl: '/app/phr/overview/overview.html'
})
export class OverviewComponent {
    @Input() phr: PlantHireRequest;
    constructor(public procumentService:ProcurementService) {

   
    }
    createPHR() {

         this.procumentService.executePlantHireRequest()
       

       
    }
    
    
    
}
