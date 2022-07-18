import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from "@angular/common/http";
import {Injectable} from "@angular/core";
import {Observable, throwError} from "rxjs";
import {Router} from "@angular/router";
import {catchError} from "rxjs/operators";
import {ToasterConfigService} from "../shared/services/toaster-config.service";
import {AuthService} from "../pages/auth/auth.service";

@Injectable()
export class ProjectInterceptor implements HttpInterceptor {

  constructor(private router: Router,
              private authService: AuthService,
              private toaster: ToasterConfigService) {
  }

  clearStorageAndLogout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.authService.getToken();

    if (token && request.url.includes('api/') && !(request.body instanceof FormData)) {

      request = request.clone({
        setHeaders: {
          'Authorization': `Bearer ${token}`,
          'Cache-Control': 'no-store, must-revalidate',
          'Pragma': 'no-cache',
          'Content-Type': 'application/json; charset=utf-8',
          'Expires': '0',
          'Access-Control-Allow-Origin': '*'
        }
      });

    }

    return next.handle(request).pipe(catchError(err => {
      if (err instanceof HttpErrorResponse) {
        if (err.status === 401) {
          this.clearStorageAndLogout();
          this.toaster.error('Your token has expired');
        }
        return throwError(err);
      }
    }));

  }

}
