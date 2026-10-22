import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-lading-page',
  imports: [],
  templateUrl: './lading-page.html',
  styleUrl: './lading-page.scss',
})
export class LadingPage {

  constructor(
    private readonly route: Router,
  ){}

  redirectLogin(){
    this.route.navigate(['/acesso'])
  }

}
