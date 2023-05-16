import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../post.service';
import { Post } from '../type';

@Component({
  selector: 'app-single-post',
  templateUrl: './single-post.component.html',
  styleUrls: ['./single-post.component.css'],
})
export class SinglePostComponent implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private router: Router
  ) {}
  post?: Post;
  isEdit: boolean = false;
  editContent: string = '';
  editErr: string = '';
  openEdit() {
    this.isEdit = true;
  }
  updatePost() {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    console.log(this.editContent);

    this.postService.updateOnePost(postId, this.editContent).subscribe(
      (res) => {
        this.post = res;
        this.editErr = '';
        this.isEdit = false;
      },
      (err) => {
        this.editErr = err.error.content;
      }
    );
  }
  getPost() {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.getOnePost(postId).subscribe(
      (res) => {
        this.post = res;
      },
      (err) => {
        console.log(err);
      }
    );
  }
  deletePost() {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    this.postService.deleteOnePost(postId).subscribe(
      (res) => {
        console.log(res);
        if (res === 'Delete') {
          this.router.navigateByUrl('post');
        }
      },
      (err) => {
        console.log(err);
      }
    );
  }
  checkPostAuthor(): boolean {
    const currentUsername = sessionStorage.getItem('user');
    const postAuthor = this.post?.created_by.username;
    return currentUsername === postAuthor;
  }
  ngOnInit(): void {
    this.getPost();
  }
}
