import {Injectable} from '@angular/core';
import {
  MatSnackBar,
  MatSnackBarConfig,
  MatSnackBarHorizontalPosition,
  MatSnackBarVerticalPosition
} from "@angular/material/snack-bar";

@Injectable({
  providedIn: 'root'
})
export class ToasterConfigService {
  horizontalPosition: MatSnackBarHorizontalPosition = 'center';
  verticalPosition: MatSnackBarVerticalPosition = 'top';

  public config: MatSnackBarConfig = {
    verticalPosition: this.verticalPosition,
    horizontalPosition: this.horizontalPosition,
    duration: 4000
  };

  constructor(public snackBar: MatSnackBar) {
  }

  public text(message: string, duration?: number): void {

    if (duration) {
      this.config.duration = duration * 1000;
    }

    this.config.panelClass = 'green-snackbar';
    this.snackBar.open(message, '', this.config);
    this.resetConfig();
  }

  public error(message: string, duration?: number): void {

    if (duration) {
      this.config.duration = duration * 1000;
    }

    this.config.panelClass = 'red-snackbar';
    this.snackBar.open(message, '', this.config);
    this.resetConfig();
  }

  public info(message: string, duration?: number): void {

    if (duration) {
      this.config.duration = duration * 1000;
    }

    this.config.panelClass = 'yellow-snackbar';
    this.snackBar.open(message, '', this.config);
    this.resetConfig();
  }

  resetConfig() {
    this.config.horizontalPosition = this.horizontalPosition;
    this.config.verticalPosition = this.verticalPosition;
    this.config.duration = 4000;
  }
}
