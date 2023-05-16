import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  RouterStateSnapshot,
  UrlTree,
} from '@angular/router';
import { Observable } from 'rxjs';
import { AppService } from './app.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard {
  constructor(private appService: AppService, private router: Router) {}
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree>
    | boolean
    | UrlTree {
    const componentName = route.component?.name;
    const isAuth = this.appService.isLogin;
    if (
      !isAuth &&
      componentName !== 'LoginComponent' &&
      componentName !== 'RegisterComponent'
    ) {
      this.router.navigateByUrl('login');
      return false;
    }

    if (
      isAuth &&
      (componentName === 'LoginComponent' ||
        componentName === 'RegisterComponent')
    ) {
      this.router.navigateByUrl('home');
      return false;
    }

    return true;
  }
}
