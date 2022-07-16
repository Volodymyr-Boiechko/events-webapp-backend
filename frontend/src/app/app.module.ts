import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {LoginComponent} from "./pages/auth/login/login.component";
import {SharedModule} from "./shared/modules/shared.module";
import {ServiceWorkerModule} from "@angular/service-worker";
import {environment} from "../environments/environment.prod";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {ProjectInterceptor} from "./interceptor/project-interceptor.service";
import {ToasterConfigService} from "./shared/services/toaster-config.service";
import {ClearObservable} from "./shared/components/clear-observable/clear-observable";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {NgxSpinnerModule} from "ngx-spinner";

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    ClearObservable
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    SharedModule,
    ServiceWorkerModule.register('ngsw-worker.js', {enabled: environment.production}),
    NgxSpinnerModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: ProjectInterceptor,
      multi: true
    },
    ToasterConfigService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
