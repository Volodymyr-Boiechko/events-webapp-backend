import {Injectable} from "@angular/core";
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from "@angular/router";
import {UserService} from "../shared/services/user-service";
import {AuthService} from "../pages/auth/auth.service";
import {RoleEnum} from "../enums/role.enum";
import {User} from "../types/user";

@Injectable({
  providedIn: 'root'
})
export class UserNoAuthGuard implements CanActivate {

  constructor(private router: Router,
              private userService: UserService,
              private authService: AuthService) {
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {

    const token: string = this.authService.getToken();
    const currentUser: User = this.userService.getCurrentUser();
    let currentUserRole: RoleEnum;

    currentUserRole = currentUser ? RoleEnum[currentUser.role.roleName as keyof RoleEnum] : null;

    if (token && currentUserRole === RoleEnum.USER) {
      this.router.navigate(['/home']);
      return false;
    } else {
      return true;
    }
  }
}
