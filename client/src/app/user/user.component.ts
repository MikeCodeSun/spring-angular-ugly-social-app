import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AppService } from '../app.service';
import { User } from '../type';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css'],
})
export class UserComponent implements OnInit {
  constructor(private route: ActivatedRoute, private appService: AppService) {}
  user: User = {
    id: 0,
    username: '',
    created_at: '',
    followersNum: 0,
    followingsNum: 0,
    follow: false,
    image: '',
  };
  isChangeImage: boolean = false;
  isUser: boolean = false;
  file!: File;

  showUploadForm() {
    this.isChangeImage = true;
  }

  getImage(e: Event) {
    const input = e.target as HTMLInputElement;

    if (!input.files?.length) {
      return;
    }
    const file = input.files[0];
    this.file = file;
  }

  uploadImage() {
    // console.log(this.file);
    if (!this.file) return;
    this.appService.uploadImage(this.file).subscribe(
      (res: any) => {
        if (res && res['image']) {
          // console.log(res);
          this.isChangeImage = false;
          this.user.image = 'http://localhost:8080/image/' + res.image;
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }

  getUser() {
    const userId = Number(this.route.snapshot.paramMap.get('id'));
    this.appService.getUser(userId).subscribe(
      (res) => {
        // console.log(res);
        if (res.image) {
          this.user = {
            ...res,
            image: 'http://localhost:8080/image/' + res.image,
          };
        } else {
          this.user = { ...res, image: '../assets/user.jpeg' };
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }

  follow() {
    const userId = Number(this.route.snapshot.paramMap.get('id'));
    this.appService.follow(userId).subscribe((res) => {
      if (res === 'follow') {
        this.user.followersNum += 1;
        this.user.follow = true;
        if (this.user.username === sessionStorage.getItem('user')) {
          this.user.followingsNum += 1;
        }
      } else {
        this.user.followersNum -= 1;
        this.user.follow = false;
        if (this.user.username === sessionStorage.getItem('user')) {
          this.user.followingsNum -= 1;
        }
      }
    });
  }

  checkIsUser() {
    const currentUsername = sessionStorage.getItem('user');

    if (this.user.username !== '' && currentUsername === this.user.username) {
      this.isUser = true;
    } else {
      this.isUser = false;
    }
  }

  ngOnInit(): void {
    this.getUser();
  }
  ngDoCheck(): void {
    this.checkIsUser();
  }
}
