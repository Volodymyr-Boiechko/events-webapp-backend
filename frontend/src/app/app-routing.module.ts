import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserNoAuthGuard} from "./guards/user-no-auth.guard";
import {LoginComponent} from "./pages/auth/login/login.component";
import {UserAuthGuard} from "./guards/user-auth.guard";

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'home'
  },
  {
    path: 'login',
    component: LoginComponent,
    canActivate: [UserNoAuthGuard]
  },
  {
    path: 'home',
    loadChildren: () => import('./pages/portal/home/home.module').then(m => m.HomeModule),
    canActivate: [UserAuthGuard]
  },
  {
    path: '**',
    redirectTo: '/login'
  }
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  providers: [
    UserNoAuthGuard, UserAuthGuard
  ]
})
export class AppRoutingModule {
}
