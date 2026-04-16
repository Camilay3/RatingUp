import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-nivel',
  templateUrl: './nivel.component.html',
  styleUrls: ['./nivel.component.scss']
})
export class NivelComponent implements OnInit {
	capitulo: number;
	nivel: number;

	constructor( private readonly router: Router ) {
		this.capitulo = history.state?.capitulo;
		this.nivel = history.state?.nivel;
	}

	ngOnInit() {
		if(!history.state.capitulo) this.router.navigate(['']);
	}
}
