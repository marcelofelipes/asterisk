import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {AuthenticationService} from "../../../services/authentication.service";
import {Router} from "@angular/router";
import {HttpErrorResponse, HttpResponse} from "@angular/common/http";
import {BaseResponse} from "../../../models/base-response.model";

@Component({
  selector: 'asterisk-frontend-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {

  public errorMessage: string | null = null;

  public readonly loginForm: FormGroup = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8),])
  });

  constructor(private readonly _authenticationService: AuthenticationService, private readonly _router: Router) {
  }

  public performLogin(): void {
    this._authenticationService.login(this.loginForm.get('email')?.value, this.loginForm.get('password')?.value).subscribe((response: HttpResponse<BaseResponse>) => {
      if (response.status === 200) {
        localStorage.setItem('_uid', response.body!.payload);
        this._router.navigate([''])
      }
    }, (error: HttpErrorResponse) => {
      this.errorMessage = error.error.hint;
    });
  }

  ngOnInit(): void {
  }
}
