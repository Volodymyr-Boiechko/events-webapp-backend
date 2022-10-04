import {Component, OnInit} from "@angular/core";
import {Location} from '@angular/common';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../auth.service";
import {HttpErrorResponse} from "@angular/common/http";
import {ClearObservable} from "../../../shared/components/clear-observable/clear-observable";
import {ToasterConfigService} from "../../../shared/services/toaster-config.service";
import {NgxSpinnerService} from "ngx-spinner";
import {filter, skipWhile, takeUntil} from "rxjs/operators";
import {flatMap} from "rxjs/internal/operators";
import {UserService} from "../../../shared/services/user-service";
import {User} from "../../../types/user";
import {AuthUser} from "../../../types/auth-user";
import {JWTToken} from "../../../types/jwt-token";

@Component({
  selector: 'app-login',
  templateUrl: 'login.component.html',
  styleUrls: ['login.component.scss']
})
export class LoginComponent extends ClearObservable implements OnInit {

  loginForm: FormGroup;
  loginToken: string = null;

  constructor(private route: ActivatedRoute,
              private authService: AuthService,
              public userService: UserService,
              private toaster: ToasterConfigService,
              private location: Location,
              private router: Router,
              private spinner: NgxSpinnerService) {
    super();
  }

  ngOnInit() {

    this.route.queryParams
    .pipe(takeUntil(this.destroy$), filter(el => !!el))
    .subscribe((params: { accessToken: string }) => {
      if (params.accessToken) {
        this.authService.setToken(params.accessToken);
        this.userService.getCurrentAccount()
        .subscribe((user: any | User) => {
          this.userService.setCurrentUser(user);
          this.spinner.hide('full')
          .then(() => {
            this.router.navigate(['/home']);
          })
        }, (err: HttpErrorResponse) => {
          this.spinner.hide('full')
          .then(() => this.toaster.error(err.error.message));
        });

      } else {
        this.initLoginForm();
      }
    });
  }

  private initLoginForm() {
    this.loginForm = new FormGroup({
      login: new FormControl(null, [
        Validators.required,
        Validators.minLength(4)
      ]),
      password: new FormControl(null, [
        Validators.required,
        Validators.minLength(5)
      ])
    });
  }

  submit() {
    const req: AuthUser = {
      username: this.loginForm.controls.login.value.trim(),
      password: this.loginForm.controls.password.value.trim()
    };
    this.login(req);
  }

  login(req: AuthUser) {
    this.spinner.show('full')
    .then(() => {
      this.authService.login(req)
      .pipe(skipWhile((res: JWTToken) => {
        if (res.jwtToken) {
          this.authService.setToken(res.jwtToken);
        } else {
          return null;
        }
      }), flatMap(() => this.userService.getCurrentAccount()))
      .subscribe((user: any | User) => {
        this.userService.setCurrentUser(user);
        this.spinner.hide('full')
        .then(() => {
          this.router.navigate(['/home']);
        })
      }, (err: HttpErrorResponse) => {
        this.spinner.hide('full')
        .then(() => this.toaster.error(err.message));
      });
    });
  }

  loginViaGoogle() {
    this.authService.googleLogin();
  }

  loginViaFacebook() {
    this.authService.facebookLogin();
  }

}
