import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { BookComponent } from '../book/book.component';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { IUser, ILoginPayload } from '../../interfaces/user/iuser';
import { LoginService } from '../../services/user/login.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSnackBarModule
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
    private readonly router : Router,
    private snackBar : MatSnackBar
  ){}

  ngOnInit(): void {
    this.initLoginForm();
    this.initRegisterForm();
  }

  initLoginForm(){
    this.LoginForm = this.fb.group({
      email : ['',[Validators.required]],
      password: ['',[
        Validators.required,
        Validators.pattern(/^[^\u{1F000}-\u{1FFFF}\u{2600}-\u{27FF}\u{2300}-\u{23FF}\u{FE00}-\u{FE0F}\u{1F900}-\u{1F9FF}\u{1FA00}-\u{1FA9F}]*$/u)]]
    })
  }

  initRegisterForm(){
    this.RegisterForm = this.fb.group({
      email: ['',[Validators.required, Validators.email]],
      password: ['',[
        Validators.required,
        Validators.pattern(/^[^\u{1F000}-\u{1FFFF}\u{2600}-\u{27FF}\u{2300}-\u{23FF}\u{FE00}-\u{FE0F}\u{1F900}-\u{1F9FF}\u{1FA00}-\u{1FA9F}]*$/u)]
      ],
      name: ['',[Validators.required, Validators.pattern('^[a-zA-ZÀ-ÿ ]+$')]],
      nickname: ['',[Validators.required, Validators.pattern('^[a-zA-Z0-9_-]+$')]],
      telefone: ['',[Validators.required, Validators.minLength(8), Validators.maxLength(11), Validators.pattern('^[0-9]{8,11}$')]]
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
      this.LoginafterRegister(payload.email,payload.password)
      this.router.navigate(['/']);
    },
    error: (err) => {

    if(err.error.data){
     if (err.error.data.name) {
      this.RegisterForm.get('name')?.setErrors({ backendError: err.error.data.name });
     }
     if (err.error.data.nickname) {
      this.RegisterForm.get('nickname')?.setErrors({ backendError: err.error.data.nickname });
     }
     if (err.error.data.telefone) {
      this.RegisterForm.get('telefone')?.setErrors({ backendError: err.error.data.telefone });
     }
     if (err.error.data.email) {
      this.RegisterForm.get('email')?.setErrors({ backendError: err.error.data.email });
     }
     if (err.error.data.password) {
      this.RegisterForm.get('password')?.setErrors({ backendError: err.error.data.password });
     }
    }
    if (err.error.code === 'CONFLICT') {
      const msgs: string[] = err.error.messages ?? [];

     msgs.forEach((msg: string) => {
     if (msg.toLowerCase().includes('email'))     this.RegisterForm.get('email')?.setErrors({ backendError: msg });
     if (msg.toLowerCase().includes('nickname'))  this.RegisterForm.get('nickname')?.setErrors({ backendError: msg });
     if (msg.toLowerCase().includes('telefone'))  this.RegisterForm.get('telefone')?.setErrors({ backendError: msg });
  });
    }
    else{

    this.snackBar.open('Ops, ocorreu um erro inesperado', 'Fechar', {
      duration: 3000
    });
  }
    }
  });
}

  onLogin(): void {
  const payload: ILoginPayload = this.LoginForm.value;

  this.loginService.login(payload.email, payload.password).subscribe({
    next: (response) => {
      this.router.navigate(['/']);
    },
    error: (err) => {
    const message = err.error?.message;

      if (err.error.data?.email) {
    this.LoginForm.get('email')?.setErrors({ backendError: err.error.data.email });
    }else if(message){
      this.LoginForm.get('password')?.setErrors({ backendError: message });
    } else {

     this.snackBar.open('Ops, ocorreu um erro inesperado', 'Fechar', {
      duration: 3000,
     });

    }

    }
  });
}

private LoginafterRegister(email: string, password: string ){
  this.loginService.login(email, password).subscribe({
    next: () => {
      this.router.navigate(['/']);
    },
    error: () => {
      this.snackBar.open('Um erro inesperado aconteceu, tente fazer login com os dados que cadastrou. Se não der certo, verifique sua conexão', 'Fechar', {
        duration: 3000
      });
      this.choseForm(false);
    }
  })
}

hidePassword = true

togglePassword(){
  this.hidePassword = !this.hidePassword
}

maskPhone(event: Event){
  const input = event.target as HTMLInputElement;
  let digits = input.value.replace(/\D/g, '');

  if (digits.length === 0) {
    input.value = '';
    this.RegisterForm.get('telefone')?.setValue('', { emitEvent: false });
    return;
  }

  if(digits.length > 11){
    digits = digits.slice(0, 11);
  }

  let masked = digits;

  if (digits.length <= 2) {
    masked = `(${digits}`;
  } else if (digits.length <= 6) {
    masked = digits.replace(/(\d{2})(\d{0,4})/, '($1) $2');
  } else if (digits.length <= 10) {
    masked = digits.replace(/(\d{2})(\d{4})(\d{0,4})/, '($1) $2-$3');
  } else {
    masked = digits.replace(/(\d{2})(\d{5})(\d{0,4})/, '($1) $2-$3');
  }

  this.RegisterForm.get('telefone')?.setValue(digits, { emitEvent: false });
  setTimeout(() => { input.value = masked; }, 0)
}


}
