import { Component, OnInit } from '@angular/core';
import { AppService } from '../app.service';
import { PostService } from '../post.service';
import { Post } from '../type';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css'],
})
export class PostComponent implements OnInit {
  constructor(
    private postService: PostService,
    private appService: AppService
  ) {}
  posts: Post[] = [];
  content: string = '';
  isUserlogin: boolean = this.appService.isLogin;
  contentInputErr: string = '';

  onSubmit() {
    this.postService.addNewPost(this.content).subscribe(
      (res) => {
        this.contentInputErr = '';
        this.posts.unshift(res);
      },
      (err) => {
        this.contentInputErr = err.error?.content;
      }
    );
  }
  ngOnInit(): void {
    this.postService.getAllPosts().subscribe((res: any) => {
      this.posts = res;
    });
  }
}
