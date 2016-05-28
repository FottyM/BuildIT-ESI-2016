import {Injectable} from 'angular2/core';
import {Http, RequestOptions, Headers} from 'angular2/http';
import {Router} from 'angular2/router';
import {buildItPort} from './../Configuration';

@Injectable()
export class AuthenticationService {
    private loggedIn = false;
    private roles: String[] = [];

    constructor (private http: Http, private router: Router) {}
    authenticate(callback) {
        var options = new RequestOptions({ headers: this.headers()});

        
        this.http.get(buildItPort+"/api/authenticate", options)
            .subscribe( resp => { this.roles = resp.json();
                console.log(resp.json());
                this.loggedIn = true; callback();});
    }

    headers() {
        var headers = new Headers();
        var code = localStorage.getItem('code');
      
        if (!!code) {
            headers.append('Authorization', `Basic ${code}`);
        }
        return headers;
    }

    headersJson() {
        var headers = new Headers({'Content-Type': 'application/json'});
        var code = localStorage.getItem('code');

        if (!!code) {
            headers.append('Authorization', `Basic ${code}`);
        }
        return headers;
    }
    optionsValueJson(){
        var headers = new Headers({'Content-Type': 'application/json'});
        var code = localStorage.getItem('code');

        if (!!code) {
            headers.append('Authorization', `Basic ${code}`);
        }
        var options = new RequestOptions({ headers: headers});
        return options
    }
    optionsValue(){
        var options = new RequestOptions({ headers: this.headers()});
        return options
    }

    logout() {
        this.loggedIn = false;
        this.roles = [];
        localStorage.removeItem('code');
        this.router.navigate(['Login'])
    }

    checkRoles(roles: String[]) {
        if (this.isLoggedIn)
            return this.roles.filter( n => roles.indexOf(n) != -1 ).length > 0;
        else
            return false;
    }

    isLoggedIn() {
        return this.loggedIn;
    }
}