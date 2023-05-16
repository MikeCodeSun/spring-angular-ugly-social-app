import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Post } from './type';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  constructor(private http: HttpClient) {}
  baseUrl: string = 'http://localhost:8080/post';
  getAllPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/all`);
  }
  addNewPost(content: string): Observable<Post> {
    return this.http.post<Post>(`${this.baseUrl}/add`, { content });
  }
  getOnePost(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.baseUrl}/id/${id}`);
  }
  deleteOnePost(id: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/delete/${id}`, {
      responseType: 'text',
    });
  }
  updateOnePost(id: number, content: string): Observable<Post> {
    return this.http.patch<Post>(`${this.baseUrl}/update/${id}`, { content });
  }

  getFollowPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.baseUrl}/follow`);
  }
}
