import { Component } from '@angular/core';
import {FormsModule} from "@angular/forms";
import {DecimalPipe, NgClass} from "@angular/common";
import { CommonModule } from '@angular/common'; // Importez CommonModule
import { Router } from '@angular/router';
@Component({
  selector: 'app-otpvalidation',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    DecimalPipe,
    NgClass
  ],
  templateUrl: './otpvalidation.component.html',
  styleUrl: './otpvalidation.component.css'
})
export class OtpvalidationComponent {
  constructor(private router: Router) {}

  otp: string = '';
  minutes: number = 5;
  seconds: number = 0;
  timer: any;
  message: string | null = null;
  isSuccess: boolean = false;

  ngOnInit() {
    this.startTimer();
  }

  ngOnDestroy() {
    this.clearTimer();
  }

  startTimer() {
    this.timer = setInterval(() => {
      if (this.seconds === 0) {
        if (this.minutes > 0) {
          this.minutes--;
          this.seconds = 59;
        } else {
          this.clearTimer();
          this.message = 'OTP expired. Please request a new one.';
          this.isSuccess = false;
        }
      } else {
        this.seconds--;
      }
    }, 1000);
  }

  clearTimer() {
    if (this.timer) {
      clearInterval(this.timer);
    }
  }

  onConfirmOtp() {
    if (this.minutes === 0 && this.seconds === 0) {
      this.message = 'OTP expired. Please request a new one.';
      this.isSuccess = false;
      return;
    }

    // Simuler la vérification de l'OTP
    this.verifyOtp(this.otp).then(isValid => {
      if (isValid) {
        this.message = 'OTP verified successfully!';
        this.isSuccess = true;
        this.router.navigate(['/agent-change-password'])
      } else {
        this.message = 'Invalid OTP. Please try again.';
        this.isSuccess = false;
      }
    });

    this.clearTimer();
  }

  // Méthode simulée pour vérifier l'OTP
  verifyOtp(otp: string): Promise<boolean> {
    return new Promise((resolve) => {
      // Simulez une vérification avec un délai
      setTimeout(() => {
        resolve(otp === '123456'); // Simule une OTP correcte
      }, 1000);
    });
  }
}
