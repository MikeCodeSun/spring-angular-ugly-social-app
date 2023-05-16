import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Credentials, RegisterError, User } from './type';

@Injectable({
  providedIn: 'root',
})
export class AppService {
  isLogin: boolean = false;
  baseUrl: string = 'http://localhost:8080';
  loginErr: string = '';
  registerErr: RegisterError = { username: '', password: '' };
  username: string = '';

  constructor(private http: HttpClient, private router: Router) {}

  // /login url need use formData
  authenticate(credentials: Credentials, callback: () => void) {
    const loginForm = new FormData();
    loginForm.append('username', credentials.username);
    loginForm.append('password', credentials.password);
    loginForm.append('submit', 'login');
    this.http
      .post(`${this.baseUrl}/login`, loginForm, {
        withCredentials: true,
        responseType: 'text',
      })
      .subscribe((res) => {
        console.log(res);

        if (res !== 'login') {
          this.loginErr = res;
          this.registerErr = { username: '', password: '' };
        } else {
          this.isLogin = true;
          this.loginErr = '';
          this.registerErr = { username: '', password: '' };
          sessionStorage.setItem('user', credentials.username);
          return callback && callback();
        }
      });
  }

  register(username: string, password: string) {
    this.http
      .post(`${this.baseUrl}/register`, { username, password })
      .subscribe((res: any) => {
        if (res && res['message'] === 'register successfully') {
          this.registerErr = { username: '', password: '' };
          this.loginErr = '';
          this.router.navigateByUrl('login');
        } else {
          this.loginErr = '';
          this.registerErr = res;
        }
      });
  }

  logout() {
    this.http
      .post(`${this.baseUrl}/logout`, {}, { responseType: 'text' })
      .subscribe((res) => console.log(res));
    this.isLogin = false;
    this.username = '';
    sessionStorage.removeItem('user');
  }

  checkIsLogin() {
    const username = sessionStorage.getItem('user');
    if (username) {
      this.isLogin = true;
    }
  }

  // authenticated url need header add authorization
  getAuthGood(credentials: Credentials, cb: () => void) {
    let headers = new HttpHeaders();

    // const headers = new HttpHeaders({
    //   Authorization:
    //     'Basic ' +
    //     window.btoa(credentials.username + ':' + credentials.password),
    // });

    headers = headers.append(
      'Authorization',
      `Basic ${window.btoa(credentials.username + ':' + credentials.password)}`
    );

    this.http
      .get(`${this.baseUrl}/api/v1/login`, {
        headers: headers,
        responseType: 'text',
        withCredentials: true,
      })
      .subscribe((res) => {
        if (res !== 'login') {
          console.log('login failed');
          this.loginErr = 'login failed';
        } else {
          this.isLogin = true;
          this.username = credentials.username;

          return cb && cb();
        }
      });
  }

  getUser(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/api/v1/user/${id}`);
  }

  getHello(): Observable<string> {
    return this.http.get(`${this.baseUrl}/api/v1/hello`, {
      responseType: 'text',
    });
  }

  follow(id: number): Observable<String> {
    return this.http.post(
      `${this.baseUrl}/follow/user/${id}`,
      {},
      { responseType: 'text' }
    );
  }

  uploadImage(file: File): Observable<any> {
    const formData = new FormData();
    formData.append('image', file);
    return this.http.post(`${this.baseUrl}/upload`, formData);
  }
}
