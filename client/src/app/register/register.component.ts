import { Component } from '@angular/core';

import { AppService } from '../app.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css'],
})
export class RegisterComponent {
  constructor(public app: AppService) {}
  username: string = '';
  password: string = '';
  register() {
    this.app.register(this.username, this.password);
  }
}
