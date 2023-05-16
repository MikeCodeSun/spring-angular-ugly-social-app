export interface Greeting {
  id: string;
  content: string;
}

export interface Credentials {
  username: string;
  password: string;
}

export interface RegisterError {
  username: string;
  password: string;
}

export interface User {
  id: number;
  username: string;
  created_at: string;
  followersNum: number;
  followingsNum: number;
  follow: boolean;
  image?: string;
}

export interface Post {
  content: string;
  createdAt: string;
  id: number;
  created_by: User;
}
