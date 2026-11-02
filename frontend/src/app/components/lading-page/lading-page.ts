import { NgTemplateOutlet } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-lading-page',
  imports: [RouterLink, NgTemplateOutlet],
  templateUrl: './lading-page.html',
  styleUrls: ['./lading-page.scss'],
})
export class LadingPage {
  readonly rotaEntrar = '/acesso';
  readonly rotaCadastro = '/acesso';
  readonly email = 'ratingupadmin@gmail.com';

  readonly benefits = [
    {
      number: '01',
      icon: 'puzzle',
      title: 'Aprenda jogando',
      text: 'Desafios curtos e interativos transformam cada conceito em uma conquista prática.',
    },
    {
      number: '02',
      icon: 'book',
      title: 'Evolua no seu ritmo',
      text: 'Trilhas organizadas do básico ao avançado, sem termos complicados ou aulas cansativas.',
    },
    {
      number: '03',
      icon: 'flag',
      title: 'Veja seu progresso',
      text: 'Acompanhe sua evolução, conquiste níveis e saiba exatamente qual é o próximo passo.',
    },
  ];

  goTo(event: Event, id: string): void {
    event.preventDefault();
    document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }
}