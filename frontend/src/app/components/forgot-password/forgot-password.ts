import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { LoginService } from '../../services/user/login.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';


@Component({
  selector: 'app-forgot-password',
  imports: [
    MatFormFieldModule,
    MatInputModule,
    ReactiveFormsModule,
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
    private readonly loginService : LoginService
  ){}

   ngOnInit(){
    this.initEmailForm();
    this.initCodeForm();
    this.initNewPassword();
  }

  initEmailForm(){
    this.EmailForm = this.fb.group({
      email : ['',[Validators.required, Validators.email]]
    })
  }

  emailSubmit(){
    const playload = this.EmailForm.value;
    this.loginService.recoverPassword(playload).subscribe({
      error : (err) => {
        this.snackBar.open('Ops, ocorreu um erro inesperado.Verifique se seu e-mail esta certo', 'Fechar',{
          duration: 3000
        })
      }
    })
  }

  codeSubmit(){
    const playload = this.CodeForm.value;
    //this.loginService.validateCode(playload).subscribe({
      //error : (err) => {
        //this.snackBar.open('Ops, ocorreu um erro, verifique se o codigo esta certo', 'Fechar', {
          //duration: 3000
        //})
      //}
    //})
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

  NewPasswordSubmit(){}

}
