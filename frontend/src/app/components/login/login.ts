import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { BookComponent } from '../book/book.component';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IUser, ILoginPayload } from '../../interfaces/iuser';
import { LoginService } from '../../services/user/login.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';


@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ],
  templateUrl: './login.html',
  styleUrls: ['./login.scss'],
})
export class Login implements OnInit {
  LoginForm!: FormGroup;
  RegisterForm!: FormGroup;
  isFirstAcess: boolean = false;

  constructor(
    private readonly fb :FormBuilder,
    private readonly loginService : LoginService,
    private readonly router : Router
  ){}

  ngOnInit(): void {
    this.initLoginForm();
    this.initRegisterForm();
  }

  initLoginForm(){
    this.LoginForm = this.fb.group({
      email : ['',[Validators.required]],
      password: ['',[Validators.required]]
    })
  }

  initRegisterForm(){
    this.RegisterForm = this.fb.group({
      email: ['',[Validators.required]],
      password: ['',[Validators.required]],
      name: ['',[Validators.required]],
      nickname: ['',[Validators.required]],
      telefone: ['',[Validators.required, Validators.minLength(8), Validators.maxLength(11)]]
    })
  }

  choseForm(value : boolean): void{
    this.isFirstAcess = value;

    if(this.isFirstAcess === true){
      this.RegisterForm.reset();
    }else if(this.isFirstAcess === false){
      this.LoginForm.reset();
    }
  }

  onRegister(): void {
   const payload: IUser = this.RegisterForm.value
   this.loginService.register(payload).subscribe({
    next: (response: any) => {
      this.router.navigate(['/book']);
    },
    error: (err) => {
      console.error('Erro no cadastro', err);
    }
  });
}

  onLogin(): void {
  const payload: ILoginPayload = this.LoginForm.value;

  this.loginService.login(payload.email, payload.password).subscribe({
    next: (response) => {
      this.router.navigate(['/book']);
    },
    error: (err) => {
      console.error('Erro no login', err);
    }
  });
}

// formatPhone(event: Event): void {
//   const input = event.target as HTMLInputElement;
//   let value = input.value.replace(/\D/g, '');

//    if (value.length > 11) {
//     value = value.slice(0, 11);
//   }

//   if (value.length <= 8) {
//     value = value.replace(/(\d{4})(\d)/, '$1-$2');
//   } else if (value.length <= 10) {
//     value = value.replace(/(\d{2})(\d{4})(\d)/, '($1) $2-$3');
//   } else {
//     value = value.replace(/(\d{2})(\d{5})(\d)/, '($1) $2-$3');
//   }

//   input.value = value;
//   this.RegisterForm.get('phone')?.setValue(value, { emitEvent: false });
// }


}
