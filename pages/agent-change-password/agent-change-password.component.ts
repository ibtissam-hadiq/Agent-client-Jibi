import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {FormsModule} from "@angular/forms";
@Component({
  selector: 'app-agent-change-password',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './agent-change-password.component.html',
  styleUrl: './agent-change-password.component.css'
})
export class AgentChangePasswordComponent {
  passwordModel: any = {};

  constructor(private router: Router
  ) {
  }

  onChangePassword() {
    // Logic to change the password
    console.log('Password changed successfully');
    this.router.navigate(['/login']);
  }
}
