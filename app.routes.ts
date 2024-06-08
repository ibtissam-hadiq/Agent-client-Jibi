import { Routes } from '@angular/router';
import {RegistrationComponent} from "./pages/registration/registration.component";
import {AgentChangePasswordComponent} from "./pages/agent-change-password/agent-change-password.component";
import {OtpvalidationComponent} from "./pages/otpvalidation/otpvalidation.component";
import {LoginComponent} from "./pages/login/login.component";
import {QrCodeComponent} from "./pages/qr-code/qr-code.component";

export const routes: Routes = [
  {path:'add-agent',component:RegistrationComponent},
  { path: 'otp-validation', component: OtpvalidationComponent },
  { path: 'agent-change-password', component: AgentChangePasswordComponent },
  { path: 'login', component: LoginComponent },
  //{ path: 'qr-scanner', component: QrCodeScannerComponent },
  { path: '**', redirectTo: 'qr-scanner' }
];
