import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Greeting } from './type';

import { Router } from '@angular/router';
import { AppService } from './app.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  baseUrl: string = 'http://localhost:8080';
  constructor(
    private http: HttpClient,
    private router: Router,
    public appService: AppService
  ) {
    http
      .get<Greeting>(`${this.baseUrl}/home`)
      .subscribe((data) => (this.greeting = data));
  }

  title = 'spring-angular';
  greeting?: Greeting;

  logout() {
    this.appService.logout();
    this.router.navigate(['login']);
  }

  ngOnInit(): void {
    this.appService.checkIsLogin();
  }
}
