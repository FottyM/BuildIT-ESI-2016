/// <reference path="../../node_modules/angular2/typings/browser.d.ts" />

import {Http, HTTP_PROVIDERS} from "angular2/http";

import "rxjs/Rx";

class Plant {
  plant: string;
  description: string;
  price: number;
  _links: any;
}

@Component({
  selector: 'plants',
  template: `
  {{plants|json}}
  `
})
class PlantComponent {
  plants : Plant[];
  constructor(private http:Http) {
    this.http.get('http://localhost:8080/api/plants')
        .subscribe( resp => this.plants = resp.json());
  }
}

import {bootstrap} from 'angular2/platform/browser';
import {Component} from 'angular2/core';
@Component({
  selector: 'app',
  directives: [PlantComponent],
  template: `Hello world
  <p>
  <plants>
  `
})
export class AppComponent {    
}

bootstrap(AppComponent, [HTTP_PROVIDERS]);
