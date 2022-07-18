import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {fromEvent} from 'rxjs';
import {filter} from 'rxjs/operators';
import {AuthService} from 'src/app/pages/auth/auth.service';
import {MatDialog} from "@angular/material/dialog";
import {User} from "../../types/user";


@Injectable({
  providedIn: 'root'
})
export class UserService {

  public isIE: boolean = false;
  private accountApiUrl = 'api/user/me';

  constructor(private http: HttpClient,
              private authService: AuthService,
              private matDialog: MatDialog) {
  }

  getCurrentAccount() {
    return this.http.get(this.accountApiUrl);
  }


  setCurrentUser(user: User) {
    localStorage.setItem('currentUser', JSON.stringify(user));
  }


  getCurrentUser(): User | any {
    if ('currentUser' in localStorage) {
      return JSON.parse(localStorage.currentUser);
    } else {
      return null;
    }
  }

  subscribeForLocaleStorageChanges() {
    fromEvent(window, 'storage')
      .pipe(
        filter((event: StorageEvent) => !event.newValue || event.key === 'currentUser')
      )
      .subscribe(() => {
        let currentRoute: string = window.location.pathname.split('/')[1].split('?')[0];

        if (localStorage.getItem('currentUser') === null && currentRoute !== 'login') {
          this.authService.logout();
          this.matDialog.closeAll();
        }
      })
  }

}
