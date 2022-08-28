import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {SharedService} from "../../shared/services/shared.service";
import {Router} from "@angular/router";
import {ApiAuthUrl} from "../../types/api-auth-url";
import {NgxSpinnerService} from "ngx-spinner";
import {ToasterConfigService} from "../../shared/services/toaster-config.service";
import {AuthUser} from "../../types/auth-user";


@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private userAuthApiUrl = 'api/login';
  private googleApiUrl = 'api/google';

  constructor(private http: HttpClient,
              private sharedService: SharedService,
              private router: Router,
              private spinner: NgxSpinnerService,
              private toaster: ToasterConfigService) {
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

  googleLogin() {

    this.authorizeGoogleUser(window.location.href)
    .subscribe((res: ApiAuthUrl) => {
      window.location.href = res.apiAuthUrl;
    }, (err: HttpErrorResponse) => {

      this.spinner.hide('full');
      if (err.status === 429) {
        let errorObject = JSON.parse(err.error);
        this.toaster.error(errorObject.message);
      } else {
        this.sharedService.defaultError();
      }

    });

  }

  authorizeGoogleUser(redirectUrl: string) {
    let params = new HttpParams().set('redirectUrl', redirectUrl);
    return this.http.get(`${this.googleApiUrl}/create-authorization`, {params});
  }

}
