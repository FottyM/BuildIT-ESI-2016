import {Component} from 'angular2/core';
import {Router} from 'angular2/router';

import {AuthenticationService} from "./auth.services";

@Component({
    template: `
<div style="margin-top: 100px">
<form class="form col-md-offset-3 col-md-6" (ngSubmit)="login()">
    <div class="form-group">
        <input type="text" class="form-control input-lg" placeholder="Username" [(ngModel)]="username">
    </div>
    <div class="form-group">
        <input type="password" class="form-control input-lg" placeholder="Password" [(ngModel)]="password">
    </div>
    <div class="form-group">
        <button class="btn btn-primary btn-lg btn-block">Log In</button>
    </div>
</form>
</div>

    `
})
export class LogInComponent {
    username: string = '';
    password: string = '';

    constructor (private router: Router, private authenticationService: AuthenticationService) {}

    login() {

      localStorage.setItem('code', window.btoa(this.username + ":" + this.password));

       
        this.authenticationService.authenticate(() => this.router.navigate(['PHRWizard']));
    }
}