import { Component } from '@angular/core';
import { RouterLink } from "@angular/router";

@Component({
	selector: 'app-not-found',
	imports: [RouterLink],
	template: `
		<img draggable="false" src="/notfound.png" alt="Página não encontrada">
		<a routerLink="/book">Voltar para o início</a>
	`,
	styles: [`
		:host {
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			height: 100dvh;
			gap: 2rem;
			background-color: color-mix(in srgb, var(--secondary) 95%, var(--neutral));
		}

		a:hover {
			font-weight: bold;
			color: var(--primaryHover);
		}
	`]
})
export class NotFoundComponent {}
