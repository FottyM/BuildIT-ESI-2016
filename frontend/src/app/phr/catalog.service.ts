import {Injectable} from 'angular2/core';
import {Http, RequestOptions} from 'angular2/http';
import {Observable} from 'rxjs/Rx';


import {Plant, Query} from './declarations';
import {XLink} from "../orders/purchase-order-listing.component";
import {buildItPort} from '../Configuration'
import {AuthenticationService} from '../login/auth.services'
import {Router} from "angular2/router";

@Injectable()
export class PlantCatalogService {
    plants: Plant[] = [];
    extension= "";
    purchaseOrderUrl="";

    constructor(public http: Http,private auth:AuthenticationService,private router:Router) {}




    executeQuery(query: Query) {


        this.http.get(buildItPort+"/api/buildit/?name="+query.name+"&startDate="+query.startDate+"&endDate="+query.endDate,this.auth.optionsValue())
            .subscribe(response => {this.plants = response.json();
           // console.log(response.json())

            },null);

    }


    executeExtensionQuery(url:XLink){

        if(url.method=="POST"){


            if(url._rel=="extend"){


              this.purchaseOrderUrl= url.href;

                this.router.navigate(['Modify']);

            }

            else


            {


                this.http.post(url.href,null)
                    .subscribe(response => {

                            var x = response.json();
                            this.extension = x.status.response;

                        },
                        error => {
                            this.extension = "Rejected";


                        });
            }

        }




        else   if(url.method=="GET"){
            if(url._rel=="cancel"){

                this.http.delete(url.href,null)
                    .subscribe(response => {



                            this.router.navigate(['PHRWizard']);

                        },
                        error => {
                            alert("sorry something went wrong")


                        });

            }

        }

        else{


               this.http.get(url.href)
                .subscribe(response =>{
                    var x = response.json();
                    this.extension = x.status.response;
                });
        }



    }
}
