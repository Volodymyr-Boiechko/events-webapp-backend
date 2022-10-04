import {Injectable} from "@angular/core";
import {HttpClient, HttpErrorResponse, HttpParams} from "@angular/common/http";
import {SharedService} from "../../shared/services/shared.service";
import {Router} from "@angular/router";
import {ApiAuthUrl} from "../../types/api-auth-url";
import {NgxSpinnerService} from "ngx-spinner";
import {ToasterConfigService} from "../../shared/services/toaster-config.service";
import {AuthUser} from "../../types/auth-user";
import {finalize} from "rxjs/operators";


@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private userAuthApiUrl = 'api/login';
  private googleApiUrl = 'api/google';
  private facebookApiUrl = 'api/facebook';

  constructor(private http: HttpClient,
              private sharedService: SharedService,
              private router: Router,
              private spinner: NgxSpinnerService,
              private toaster: ToasterConfigService) {
  }

  login(req: AuthUser) {
    return this.http.post(this.userAuthApiUrl, req);
  }

  logout(isAdmin = false): void {
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

    this.spinner.show('full');
    this.authorizeSocialMediaUser(window.location.href, this.googleApiUrl)
    .pipe(
      finalize(() => this.spinner.hide('full'))
    )
    .subscribe((res: ApiAuthUrl) => {
      window.location.href = res.apiAuthUrl;
    }, (err: HttpErrorResponse) => {

      if (err.status === 429) {
        let errorObject = JSON.parse(err.error);
        this.toaster.error(errorObject.message);
      } else {
        this.sharedService.defaultError();
      }
    });

  }

  authorizeSocialMediaUser(redirectUrl: string, apiUrl: string) {
    let params = new HttpParams().set('redirectUrl', redirectUrl);
    return this.http.get(`${apiUrl}/create-authorization`, {params});
  }

  facebookLogin() {

    this.spinner.show('full');
    this.authorizeSocialMediaUser(window.location.href, this.facebookApiUrl)
    .pipe(
      finalize(() => this.spinner.hide('full'))
    )
    .subscribe((res: ApiAuthUrl) => {
      window.location.href = res.apiAuthUrl;
    }, (err: HttpErrorResponse) => {

      if (err.status === 429) {
        let errorObject = JSON.parse(err.error);
        this.toaster.error(errorObject.message);
      } else {
        this.sharedService.defaultError();
      }
    });

  }

}
