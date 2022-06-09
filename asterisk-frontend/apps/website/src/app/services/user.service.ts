import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {Observable} from "rxjs";
import {User} from "../models/user.model";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private readonly _http: HttpClient) {

  }

  /**
   *
   * @param uid
   */
  public readUser(uid: string): Observable<HttpResponse<User>> {
    return this._http.get<User>(environment.backendUrl + `/user/${uid}`, {observe: 'response'});
  }

  /**
   *
   * @param uid
   * @param firstName
   * @param lastName
   * @param email
   * @param username
   */
  public updateUser(uid: string, firstName?: string, lastName?: string, email?: string, username?: string): Observable<HttpResponse<never>> {
    return this._http.put<never>(environment.backendUrl + `/user/${uid}`, {
      firstName: firstName,
      lastName: lastName,
      email: email,
      username: username,
    }, {observe: 'response'})
  }

  /**
   *
   * @param uid
   * @param password
   * @param passwordConfirmation
   */
  public changePassword(uid: string, password: string, passwordConfirmation: string): Observable<HttpResponse<never>> {
    return this._http.post<never>(environment.backendUrl + `/user/${uid}/change-password`, {
        password: password,
        passwordConfirmation: passwordConfirmation
      },
      {observe: 'response'});
  }
}
