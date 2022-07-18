import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FlexLayoutModule} from "@angular/flex-layout";
import {AppMaterialModule} from "../../app-material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {HttpClientModule} from "@angular/common/http";
import {MatDialogModule, MatDialogRef} from "@angular/material/dialog";
import {ToasterConfigService} from "../services/toaster-config.service";
import {HeaderComponent} from "../components/header/header.component";

@NgModule({
  imports: [
    CommonModule,
    FlexLayoutModule,
    AppMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    MatDialogModule
  ],
  providers: [
    ToasterConfigService,
    {
      provide: MatDialogRef,
      useValue: {}
    }
  ],
  declarations: [
    HeaderComponent
  ],
  exports: [
    CommonModule,
    FlexLayoutModule,
    AppMaterialModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule,
    HttpClientModule,
    HeaderComponent
  ]
})
export class SharedModule {
}
