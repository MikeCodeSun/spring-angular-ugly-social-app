import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from '../app.service';
import { Credentials } from '../type';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  constructor(public app: AppService, private router: Router) {}

  credentials: Credentials = { username: '', password: '' };
  loginErr?: string;
  login() {
    this.app.authenticate(this.credentials, () => {
      this.router.navigateByUrl('/');
    });

    // this.app.getAuthGood(this.credentials, () => {
    //   this.router.navigateByUrl('home');
    // });
  }
}
