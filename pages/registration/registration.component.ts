import { Component } from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {AgentService} from "../../agent.service";
import {Agent} from "../../../../models/agent-module";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [
    MatIcon,
    MatIconButton,
    ReactiveFormsModule
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  agentForm: FormGroup;

  constructor(private fb: FormBuilder, private agentService: AgentService) {
    this.agentForm = this.fb.group({
      lastname: ['', Validators.required],
      firstname: ['', Validators.required],
      typeidentity: ['', Validators.required],
      numidentity: ['', Validators.required],
      dateofbirth: ['', Validators.required],
      address: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      confemail: ['', [Validators.required, Validators.email]],
      phonenumber: ['', Validators.required],
      immatriculation: ['', Validators.required],
      patente: ['', Validators.required],
      description: ['']
    });
  }

  onSubmit(): void {
    if (this.agentForm.valid) {
      const agent: Agent = this.agentForm.value;
      console.log("this is agnet",agent) ;
      this.agentService.addAgent(agent).subscribe(response => {
        console.log('Agent ajouté avec succès:', response);
        this.agentForm.reset(); // Réinitialiser le formulaire après soumission
      }, error => {
        console.error('Erreur lors de l\'ajout de l\'agent:', error);
      });
    }
  }

}
