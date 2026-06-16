import { AuthService } from './../../services/user/auth.service';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router, RouterLink } from '@angular/router';
import { BookService } from '../../services/book/book.service';
import { ISubtopicoContent } from '../../interfaces/book/IBook';
import { ChessBoard } from '../chess-board/chess-board';
import { Quiz } from '../quiz/quiz';

@Component({
  selector: 'app-subtopico',
  templateUrl: './subtopico.component.html',
  styleUrls: ['./subtopico.component.scss'],
  imports: [RouterLink, ChessBoard, Quiz]
})
export class SubtopicoComponent implements OnInit {
	subtopicId: number;
	subtopicoContent: ISubtopicoContent | null = null;
	fenDoBackend = 'start';
	tipoFase: 'BOARD' | 'MULTIPLE_CHOICE' | null = null;
	praticaConcluida = false;

	constructor(
		private readonly router: Router,
		private readonly authService: AuthService,
		private readonly bookService: BookService,
		private readonly snackBar : MatSnackBar,
		private readonly cdr: ChangeDetectorRef,
	) {
		this.subtopicId = history.state?.subtopicoId;
	}

	ngOnInit() {
		if(!this.subtopicId) this.router.navigate(['']);

		this.bookService.getSubtopicContent(this.subtopicId).subscribe({
			next: (response) => {
				this.subtopicoContent = response.data;
				this.cdr.detectChanges();
			},
			error: (e) => {
				this.snackBar.open(e.error.message, 'Fechar', { duration: 3000 });
				this.router.navigate(['/'], {
					state: { executarAnimacao: true }
				});
			}
		})

		this.bookService.getSubtopicType(this.subtopicId).subscribe({
            next: (response) => {
                this.tipoFase = response.type;
                this.cdr.detectChanges();
            },
            error: (e) => this.snackBar.open(e.error.message, 'Fechar', { duration: 3000 })
            });
	    }

	concluirSubtopico() {
		this.authService.atualizarProgresso(this.subtopicoContent?.chapterId!, this.subtopicoContent?.displayOrder!).subscribe({
			next: () => {
				this.router.navigate(['/'], {
					state: { executarAnimacao: true }
				});
			},
			error: (e) => this.snackBar.open(e.error.message, 'Fechar', { duration: 3000 }),
		});
	}
}
