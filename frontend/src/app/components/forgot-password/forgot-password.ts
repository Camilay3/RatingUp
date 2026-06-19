import { Component, OnInit , ChangeDetectorRef} from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../../services/user/login.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-forgot-password',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    CommonModule
  ],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.scss',
})
export class ForgotPassword implements OnInit{

  EmailForm!: FormGroup;
  CodeForm!: FormGroup;
  NewPasswordForm!: FormGroup;

  step: 'email' | 'code' | 'password' = 'email';
  email = '';
  code = '';

  constructor(
    private readonly fb : FormBuilder,
    private readonly snackBar : MatSnackBar,
    private readonly loginService : LoginService,
    private readonly router : Router,
    private readonly cdr : ChangeDetectorRef
  ){}

   ngOnInit(){
    this.initEmailForm();
    this.initCodeForm();
    this.initNewPassword();
  }

  emailSubmit(){
  const { email } = this.EmailForm.value;
  this.loginService.recoverPassword(email).subscribe({
    next: (res) => {
      console.log(res);
      if(res.status){
        this.email = email;
        this.step = 'code';
        console.log('step:', this.step);
        this.cdr.detectChanges();
        this.snackBar.open('Email enviado com sucesso!', 'Fechar', { duration: 3000 });
      } else {
        this.snackBar.open(res.message, 'Fechar', { duration: 3000 });
      }
    },
    error: () => this.snackBar.open('Erro ao enviar email', 'Fechar', { duration: 3000 })
  });
}

codeSubmit(){
  const { code } = this.CodeForm.value;
  this.loginService.validateToken(code).subscribe({
    next: () => { 
      this.step = 'password';
      this.cdr.detectChanges();
    },
    error: () => this.snackBar.open('Código inválido', 'Fechar', { duration: 3000 })
  });
}

NewPasswordSubmit(){
  const { NewPassword } = this.NewPasswordForm.value;
  this.loginService.resetPassword(NewPassword).subscribe({
    next: (res) => {
      if(res.status){ 
        this.snackBar.open('Senha redefinida com sucesso!', 'Fechar', { duration: 3000 });
        this.router.navigate(['/acesso'])
        this.cdr.detectChanges();
      } else {
        this.snackBar.open(res.message, 'Fechar', { duration: 3000 });
      }
    },
    error: () => this.snackBar.open('Erro ao redefinir senha', 'Fechar', { duration: 3000 })
  });
}

  initEmailForm(){
    this.EmailForm = this.fb.group({
      email : ['',[Validators.required, Validators.email]]
    })
  }

  initCodeForm(){
    this.CodeForm = this.fb.group({
      code: ['',[Validators.required, Validators.pattern(/^[^\u{1F000}-\u{1FFFF}\u{2600}-\u{27FF}\u{2300}-\u{23FF}\u{FE00}-\u{FE0F}\u{1F900}-\u{1F9FF}\u{1FA00}-\u{1FA9F}]*$/u)]]
    })
  }

  initNewPassword(){
    this.NewPasswordForm = this.fb.group({
      NewPassword: ['',[Validators.required, Validators.pattern(/^[^\u{1F000}-\u{1FFFF}\u{2600}-\u{27FF}\u{2300}-\u{23FF}\u{FE00}-\u{FE0F}\u{1F900}-\u{1F9FF}\u{1FA00}-\u{1FA9F}]*$/u)]]
    })
  }

}
