import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {UserNoAuthGuard} from "./guards/user-no-auth.guard";
import {LoginComponent} from "./pages/auth/login/login.component";
import {UserAuthGuard} from "./guards/user-auth.guard";

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'login',
    canLoad: [UserNoAuthGuard]
  },
  {
    path: 'login',
    component: LoginComponent,
    canLoad: [UserNoAuthGuard]
  },
  {
    path: 'home',
    loadChildren: () => import('./pages/portal/home/home.module').then(m => m.HomeModule),
    canLoad: [UserAuthGuard]
  },
  {
    path: '**',
    redirectTo: '/login'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
