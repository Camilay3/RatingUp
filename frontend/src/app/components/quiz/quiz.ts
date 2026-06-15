import { Component, Input, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChessService } from '../../services/chess/chess.service';
import { IQuiz } from '../../interfaces/chess/iquiz';
import { QuizService } from '../../services/quiz/quiz.service';

@Component({
  selector: 'app-quiz',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './quiz.html',
  styleUrl: './quiz.scss',
})
export class Quiz implements OnInit {
  @Input() subtopicId!: number;

  pergunta: IQuiz | null = null;
  opcaoSelecionada: number | null = null;
  errou = false;
  loading = true;

  constructor(
    private quizService: QuizService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.quizService.searchQuiz(this.subtopicId).subscribe({
      next: (data) => {
        this.pergunta = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('erro no quiz:', err);
        this.loading = false;
      }
    });
  }

  selecionarOpcao(optionId: number) {
    this.opcaoSelecionada = optionId;
  }

  confirmarResposta() {
    if (this.opcaoSelecionada === null) return;

    this.quizService.answerQuiz(this.subtopicId, this.opcaoSelecionada).subscribe({
      next: (res) => {
        if (res.correct) {
          // aqui você pode emitir um evento pro subtopico ou navegar
        } else {
          this.errou = true;
          this.opcaoSelecionada = null;
          this.cdr.detectChanges();
        }
      }
    });
  }
}