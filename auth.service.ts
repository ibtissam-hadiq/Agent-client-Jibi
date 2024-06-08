import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private authUrl = 'http://localhost:8080/auth/authenticate';
  constructor(private http: HttpClient) { }
  authenticate(username: string, password: string): Observable<any> {
    return this.http.post<any>(this.authUrl, { username, password });
  }
}
