import {Component, OnInit} from '@angular/core';
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";

const googleLogoURL = "../assets/icons/Google.svg";
const facebookLogoURL = "../assets/icons/Facebook.svg";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {

  ngOnInit(): void {

  }

  constructor(private matIconRegistry: MatIconRegistry,
              private domSanitizer: DomSanitizer) {
    this.registerIcons();
  }

  registerIcons() {
    this.matIconRegistry.addSvgIcon(
      "google-logo",
      this.domSanitizer.bypassSecurityTrustResourceUrl(googleLogoURL));

    this.matIconRegistry.addSvgIcon(
      "facebook-logo",
      this.domSanitizer.bypassSecurityTrustResourceUrl(facebookLogoURL));

  }

}
