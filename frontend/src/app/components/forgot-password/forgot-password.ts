import { Component, OnInit , ChangeDetectorRef} from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../../services/user/login.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';


@Component({
  selector: 'app-forgot-password',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
    MatSnackBarModule,
    CommonModule,
    MatIconModule,
    MatButtonModule
  ],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.scss',
})
export class ForgotPassword implements OnInit{

  EmailForm!: FormGroup;
  CodeForm!: FormGroup;
  NewPasswordForm!: FormGroup;
  hidePassword = true

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
      if(res.status){
        this.email = email;
        this.step = 'code';
        this.cdr.detectChanges();
        this.snackBar.open('Email enviado com sucesso!', 'Fechar', { duration: 3000, panelClass: ['snack-success']  });
      } else {
        this.snackBar.open(res.message, 'Fechar', { duration: 3000});
      }
    },
    error: (err) => {
      const message = err.error?.message;
      if(err.error?.data?.email){
        this.EmailForm.get('email')?.setErrors({ backendError: err.error.data.email });
      } else if(message){
        this.snackBar.open(message, 'Fechar', { duration: 3000 });
      } else {
        this.snackBar.open('Ops, ocorreu um erro inesperado', 'Fechar', { duration: 3000 });
      }
    }
  });
}

codeSubmit(){
  const { code } = this.CodeForm.value;
  this.loginService.validateToken(code).subscribe({
    next: () => {
      this.step = 'password';
      this.cdr.detectChanges();
      this.snackBar.open('Codigo enviado com sucesso!', 'Fechar', { duration: 3000, panelClass: ['snack-success']  });
    },
    error: (err) => {
      const message = err.error?.message;
      if(message){
        this.snackBar.open(message, 'Fechar', { duration: 3000 });
      } else {
        this.snackBar.open('Código inválido', 'Fechar', { duration: 3000 });
      }
    }
  });
}

NewPasswordSubmit(){
  const { NewPassword } = this.NewPasswordForm.value;
  this.loginService.resetPassword(NewPassword).subscribe({
    next: (res) => {
      if(res.status){
        this.snackBar.open('Senha redefinida com sucesso!', 'Fechar', { duration: 3000, panelClass: ['snack-success'] });
        this.router.navigate(['/acesso'])
        this.cdr.detectChanges();
      } else {
        this.snackBar.open(res.message, 'Fechar', { duration: 3000 });
      }
    },
    error: (err) => {
      const message = err.error?.message;
      if(err.error?.data?.newPassword){
        this.NewPasswordForm.get('NewPassword')?.setErrors({ backendError: err.error.data.newPassword });
      } else if(message){
        this.snackBar.open(message, 'Fechar', { duration: 3000 });
      } else {
        this.snackBar.open('Ops, ocorreu um erro inesperado', 'Fechar', { duration: 3000 });
      }
    }
  });
}

  initEmailForm(){
    this.EmailForm = this.fb.group({
      email : ['',[Validators.required, Validators.email]]
    })
  }

  initCodeForm(){
    this.CodeForm = this.fb.group({
      code: ['',[Validators.required, Validators.minLength(5), Validators.maxLength(5), Validators.pattern(/^\d+$/)]]
    })
  }

  initNewPassword(){
    this.NewPasswordForm = this.fb.group({
      NewPassword: ['',[Validators.required, Validators.pattern(/^(?!.*<[^>]+>)(?!.*[\u{1F000}-\u{1FFFF}\u{2600}-\u{27FF}\u{2300}-\u{23FF}\u{FE00}-\u{FE0F}\u{1F900}-\u{1F9FF}\u{1FA00}-\u{1FA9F}])[A-Za-z\d@$!%*?&]+$/u)]]
    })
  }

  togglePassword(){
  this.hidePassword = !this.hidePassword
  }

}
