import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {BaseResponse} from "../models/base-response.model";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private readonly authBaseUrl = "http://localhost:8080/api/auth";

  constructor(private readonly _http: HttpClient) {
  }

  public login(email: string, password: string): Observable<HttpResponse<BaseResponse>> {
    return this._http.post<BaseResponse>(environment.backendUrl + "/auth/login",
      {email: email, password: password}, {observe: 'response'});
  }

  public register(firstName: string, lastName: string, username: string, email: string, password: string): Observable<HttpResponse<BaseResponse>> {
    return this._http.post<BaseResponse>(this.authBaseUrl + "/register",
      {
        firstName: firstName,
        lastName: lastName,
        username: username,
        email: email,
        password: password
      }, {observe: 'response'});
  }
}
