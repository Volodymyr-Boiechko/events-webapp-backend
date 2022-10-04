import {Component, OnInit} from "@angular/core";
import {UserService} from "../../../shared/services/user-service";
import {User} from "../../../types/user";
import {AuthService} from "../../auth/auth.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {

  user: User

  constructor(public userService: UserService,
              private authService: AuthService) {
  }

  ngOnInit(): void {
    this.user = this.userService.getCurrentUser();
  }

  logout() {
    this.authService.logout();
  }

}
