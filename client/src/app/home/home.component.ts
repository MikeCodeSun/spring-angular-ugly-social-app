import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppService } from '../app.service';
import { PostService } from '../post.service';
import { Post } from '../type';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  username: string = '';
  posts: Post[] = [];
  constructor(
    private appService: AppService,
    private postService: PostService,
    private router: Router
  ) {}

  ngOnInit() {
    this.appService.getHello().subscribe((res) => {
      const sessionUserName = sessionStorage.getItem('user');
      if (sessionUserName && sessionUserName === res) {
        this.username = res;
      } else if (sessionUserName && res !== sessionUserName) {
        this.appService.logout();
        this.router.navigateByUrl('login');
      }
    });

    this.postService.getFollowPosts().subscribe(
      (res) => {
        // console.log(res);
        this.posts = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }
}
