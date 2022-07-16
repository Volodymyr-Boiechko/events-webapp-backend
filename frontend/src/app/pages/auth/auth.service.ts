import {Injectable} from "@angular/core";
import {HttpClient, HttpParams} from "@angular/common/http";
import {SharedService} from "../../shared/services/shared.service";
import {Router} from "@angular/router";
import {AuthUser} from "./login/login.component";


@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private userAuthApiUrl = 'api/authenticate';

  constructor(private http: HttpClient,
              private sharedService: SharedService,
              private router: Router) {
  }

  login(req: AuthUser) {
    return this.http.post(this.userAuthApiUrl, req);
  }

  logout(isAdmin = false) {
    localStorage.clear();
    if (!this.sharedService.isPublicPage()) {
      isAdmin ? this.router.navigate(['/admin-login']) : this.router.navigate(['/login']);
    }
  }

  setToken(token: string): void {
    localStorage.setItem('token', JSON.stringify(token));
  }

  getToken() {
    if ('token' in localStorage) {
      return JSON.parse(localStorage.getItem('token'));
    }
    return null;
  }

  getJWTTokenFromLoginToken(token: string) {
    const params = new HttpParams().append('token', token)
    return this.http.get(this.userAuthApiUrl, {params: params})
  }

}
