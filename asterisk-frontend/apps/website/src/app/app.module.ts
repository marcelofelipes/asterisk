import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule } from '@angular/forms';
import { HomeComponent } from './components/common/home/home.component';
import { AboutComponent } from './components/common/about/about.component';
import { LoginComponent } from './components/authentication/login/login.component';
import { RegisterComponent } from './components/authentication/register/register.component';
import { NotFoundComponent } from './components/common/not-found/not-found.component';
import { ProfileComponent } from './components/user/profile/profile.component';
import { ConfirmRegistrationComponent } from './components/authentication/confirm-registration/confirm-registration.component';
import { ForgotPasswordResetComponent } from './components/authentication/forgot-password-reset/forgot-password-reset.component';
import { ForgotPasswordComponent } from './components/authentication/forgot-password/forgot-password.component';
import { EditProfileComponent } from './components/user/edit-profile/edit-profile.component';
import { ChangePasswordComponent } from './components/user/change-password/change-password.component';
import { NavbarComponent } from './components/layout/navbar/navbar.component';
import { AppRoutingModule } from './app-routing.module';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    AboutComponent,
    LoginComponent,
    RegisterComponent,
    NotFoundComponent,
    ProfileComponent,
    ConfirmRegistrationComponent,
    ForgotPasswordResetComponent,
    ForgotPasswordComponent,
    EditProfileComponent,
    ChangePasswordComponent,
    NavbarComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    AppRoutingModule,
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
