import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

@Component({
  selector: 'app-subtopico',
  templateUrl: './subtopico.component.html',
  styleUrls: ['./subtopico.component.scss'],
  imports: [RouterLink]
})
export class SubtopicoComponent implements OnInit {
	capitulo: number;
	subtopico: number;

	constructor( private readonly router: Router ) {
		this.capitulo = history.state?.capitulo;
		this.subtopico = history.state?.subtopico;
	}

	ngOnInit() {
		if(!history.state.capitulo) this.router.navigate(['']);
	}

	concluirSubtopico() {
		this.router.navigate(['']);
	}

	content = {
		titulo: "O que é xadrez?",
		texto: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Dicta vel excepturi incidunt nesciunt facere! Maxime accusantium eveniet, aliquid ea illo error natus inventore alias dignissimos aspernatur, molestiae quisquam eius nesciunt. Lorem ipsum dolor sit amet consectetur adipisicing elit. Perferendis, quod dolorem magni harum autem est molestiae dolor id et totam dolore laborum asperiores accusamus sunt facere vitae sequi quis. Quia? Lorem ipsum dolor sit amet consectetur, adipisicing elit. Mollitia adipisci, qui molestias fugiat debitis amet culpa! Hic fuga enim velit nemo reprehenderit voluptates, adipisci inventore accusantium optio ipsa. Tenetur, illum? Lorem ipsum dolor sit amet consectetur, adipisicing elit. Fugiat mollitia provident aspernatur impedit, aliquam error est in quam assumenda quae, vitae architecto obcaecati iusto rem accusantium inventore veritatis ex perspiciatis! Lorem ipsum dolor sit amet consectetur adipisicing elit. Nulla qui consequatur quos, fugiat rem placeat non voluptas, sunt totam itaque necessitatibus in sapiente nisi autem vitae a quae laborum ut! Lorem ipsum dolor sit amet consectetur adipisicing elit. Consequuntur, architecto sunt? Officiis aspernatur iste quos repellat magnam, harum officia nulla quod at eaque consequuntur necessitatibus blanditiis laudantium, velit dolorem porro.",
		contextoPratica: "Lorem ipsum dolor sit amet consectetur adipisicing elit. Error sed esse accusantium, non vero doloribus quis nobis ea nulla autem magni architecto consectetur blanditiis illum repellat facilis quae! Consequuntur, deserunt! Lorem, ipsum dolor sit amet consectetur adipisicing elit. Omnis minima accusamus voluptas numquam iusto voluptatum accusantium id? Optio perspiciatis minima fuga omnis, voluptatibus, facilis rerum hic quis quibusdam ratione natus! Lorem ipsum dolor sit amet consectetur adipisicing elit. Quod repellendus repellat, ullam distinctio laborum doloribus debitis quisquam inventore nemo, maiores nisi. Qui non natus ad odit nulla tenetur iste atque?",
	}
}
