import { ChangeDetectorRef, Component, HostListener, OnInit, QueryList, ViewChildren } from '@angular/core';
import { SheetComponent } from "../sheet/sheet.component";
import { ISheet } from '../../interfaces/book/IBook';

import { BookService } from '../../services/book/book.service';
import { AudioService } from '../../services/book/audio.service';
import { AuthService } from '../../services/user/auth.service';
import { LoaderComponent } from '../loader/loader.component';
import { switchMap } from 'rxjs';

@Component({
	selector: 'app-book',
	templateUrl: './book.component.html',
	styleUrls: ['./book.component.scss'],
	imports: [SheetComponent, LoaderComponent]
})
export class BookComponent implements OnInit {
	onFirstPage: boolean = true;
	onLastPage: boolean = false;
	isWaiting: boolean = false;
	isLoading: boolean = true;
	pageFlipStates: boolean[] = [];

	capituloAtual: number = 1;
	subtopicoAtual: number = 1;

	pages: ISheet[] = [];
	home: any[] = [];
	book: any[] = [];
	tamanhoLivro: number = 0;
	duracaoAnimacao: number = 1000;
	zIndexValues: number[] = [];
	nickname: string = '';

	private readonly ALTURA_UTIL = 569;
    private readonly ALTURA_HEADER_NIVEIS = 140; // h1 + span + margin-top 4rem
    private readonly ALTURA_CAPITULO = 52;
    private readonly ALTURA_SUBTOPICO = 20;

	constructor(
		private readonly cdr: ChangeDetectorRef,
		private readonly bookService: BookService,
		private readonly authService: AuthService,
		private readonly audioService: AudioService,
	) {}

	ngOnInit() {
		this.authService.getProgresso()
			.pipe(
				switchMap((response) => {
					this.capituloAtual = response.data.chapter;
					this.subtopicoAtual = response.data.subtopic;
					return this.bookService.getSheets();
				})

			).subscribe({
				next: (response) => {
					this.pages = response.data.pages.map(page => {
						let currentItem = false;

						(['front', 'verse'] as const).forEach(side => {
							const current = page[side];

							if (current.type == 'subtópico') {
								const isAfterCurrentChapter = current.chapterId > this.capituloAtual;
								const isAfterCurrentSubtopic =
									current.chapterId === this.capituloAtual &&
									current.displayOrder > this.subtopicoAtual;

								currentItem = isAfterCurrentChapter || isAfterCurrentSubtopic;
								page[side] = { ...current, isBlocked: currentItem };
							}
						});
						return page;
					}) || [];

					this.buildBookFromPages();
					this.cdr.detectChanges();
				},
				error: (e) => console.error(e),
				complete: () => this.isLoading = false
			})
	}

private buildHomePages(): any[] {
  type Grupo = { capSheet: ISheet; subSheets: ISheet[] };
  const grupos: Grupo[] = [];

  for (const sheet of this.pages) {
    if (sheet.front.type === 'capitulo') {
      grupos.push({ capSheet: sheet, subSheets: [] });
    } else if (sheet.front.type === 'subtópico' && grupos.length > 0) {
      grupos[grupos.length - 1].subSheets.push(sheet);
    }
  }

  // Altura útil da frente home (tem header com nickname+sair + h1 Níveis + span)
  const ALTURA_FRENTE_HOME = this.ALTURA_UTIL - this.ALTURA_HEADER_NIVEIS;
  // Altura útil do verso e páginas seguintes (sem header, só padding)
  const ALTURA_CONTINUACAO = this.ALTURA_UTIL;

  // Divide grupos em fatias (frente, verso, frente, verso...)
  const fatias: Grupo[][] = [];
  let fatiaAtual: Grupo[] = [];
  let alturaUsada = 0;
  let primeiraFatia = true;

  const alturaDisponivel = () => primeiraFatia ? ALTURA_FRENTE_HOME : ALTURA_CONTINUACAO;

  for (const grupo of grupos) {
    const totalSubs =
      (grupo.capSheet.verse?.type === 'subtópico' ? 1 : 0) +
      grupo.subSheets.reduce((acc, s) => {
        return acc
          + (s.front.type === 'subtópico' ? 1 : 0)
          + (s.verse?.type === 'subtópico' ? 1 : 0);
      }, 0);

    const alturaGrupo = this.ALTURA_CAPITULO + totalSubs * this.ALTURA_SUBTOPICO;

    if (alturaUsada + alturaGrupo <= alturaDisponivel()) {
      fatiaAtual.push(grupo);
      alturaUsada += alturaGrupo;
    } else {
      if (fatiaAtual.length > 0) {
        fatias.push(fatiaAtual);
        primeiraFatia = false;
      }
      fatiaAtual = [grupo];
      alturaUsada = alturaGrupo;
    }
  }
  if (fatiaAtual.length > 0) fatias.push(fatiaAtual);

  // Monta os sheets: fatia[0]=frente home, fatia[1]=verso home, fatia[2]=frente próxima folha...
  const nickname = this.authService.getMyUser()?.data.nickname;
  const toSummary = (fatia: Grupo[]) =>
    fatia.flatMap(g => [g.capSheet, ...g.subSheets]);

  const sheets: any[] = [];

  for (let i = 0; i < fatias.length; i += 2) {
    const frente = i === 0
  ? { type: 'home', nickname, summary: toSummary(fatias[0]) }
  : { type: 'sumario', displayOrder: i, summary: toSummary(fatias[i]) };

    const verso = fatias[i + 1]
  ? { type: 'sumario', displayOrder: i + 1, summary: toSummary(fatias[i + 1]) }
  : null;

    sheets.push({ front: frente, verse: verso });
  }

  return sheets;
}

	private buildBookFromPages(): void {
  const homePages = this.buildHomePages();

  this.book = [
    { capa: 'capa.png', frenteCapa: true },
    ...homePages,
    ...this.pages,
    { capa: 'quartaCapa.png' }
  ];

  this.tamanhoLivro = this.book.length;
  this.pageFlipStates = new Array(this.tamanhoLivro).fill(false);
  this.zIndexValues = [];
  for (let i = 0; i < this.tamanhoLivro; i++) {
    this.zIndexValues.push(this.tamanhoLivro - i + 1);
  }
}

	@ViewChildren(SheetComponent) sheets!: QueryList<SheetComponent>;
	paginaAtual: number = 0;

	@HostListener('window:keydown.arrowright')
	onArrowRight() {
		if (this.isWaiting) return;
		this.sheets.get(this.paginaAtual)?.virarPagina();
	}

	@HostListener('window:keydown.arrowleft')
	onArrowLeft() {
		if (this.isWaiting) return;
		const anterior = this.paginaAtual - 1;
		if (anterior >= 0) this.sheets.get(anterior)?.virarPagina();
	}

	onFlip(pageIndex: number, flipped: boolean) {
		this.zIndexValues[pageIndex] = Math.max(...this.zIndexValues) + 1;
		this.pageFlipStates[pageIndex] = flipped;
		this.checkPagesFlipped();
		this.paginaAtual = flipped ? pageIndex + 1 : pageIndex;
		if (!flipped) setTimeout(() => { this.reordenarZIndex(); }, this.duracaoAnimacao);
	}

	reordenarZIndex() {
		this.zIndexValues = this.pageFlipStates.map((flipped, index) => {
			return flipped ? index + 1 : this.tamanhoLivro - index;
		});
	}

	multiplasPaginas(qnt: number) {
		let next = true;
		this.isWaiting = true;
		this.duracaoAnimacao = 160;
		this.setVelocidade(this.duracaoAnimacao);
		if (qnt < 0) {
			qnt = this.paginaAtual - 1
			next = false;
		};
		let viradas = 0;

		this.audioService.playFlips();
		const tempoTotal = (qnt - 1) * 100 + this.duracaoAnimacao;
		setTimeout(() => { this.audioService.stopFlips(); }, tempoTotal);

		const virar = () => {
			if (next) {
				if (this.paginaAtual >= this.tamanhoLivro) return;
				this.sheets.get(this.paginaAtual)?.virarPagina(true);

			} else {
				const anterior = this.paginaAtual - 1;
				if (anterior < 0) return;
				this.sheets.get(anterior)?.virarPagina(true);
			}
			viradas++;

			if (viradas >= qnt) {
				setTimeout(() => {
					this.duracaoAnimacao = 1000;
					this.setVelocidade(this.duracaoAnimacao);
				}, this.duracaoAnimacao);
				this.isWaiting = false;
				return;
			}
			setTimeout(virar, 100);
		};

		virar();
	}

	setVelocidade(valor: number) {
		document.documentElement.style.setProperty('--duracao', `${valor}ms`);
	}

	checkPagesFlipped() {
		this.onFirstPage = false;
		this.onLastPage = false;

		if (!this.pageFlipStates[0] || !this.pageFlipStates.length) this.onFirstPage = true;
		if (this.pageFlipStates[this.tamanhoLivro - 1]) this.onLastPage = true;
	}
}
