import { Component, Input, OnInit, ChangeDetectorRef, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ChessService } from '../../services/chess/chess.service';
import { IQuiz } from '../../interfaces/chess/iquiz';
import { QuizService } from '../../services/quiz/quiz.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-quiz',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './quiz.html',
  styleUrl: './quiz.scss',
})
export class Quiz implements OnInit {
  @Input() subtopicId!: number;
  @Output() concluido = new EventEmitter<void>();

  pergunta: IQuiz | null = null;
  opcaoSelecionada: number | null = null;
  errou = false;
  loading = true;
  quizConcluido = false;
  opcaoConfirmada: number | null = null;
  enviando = false;

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
    if (this.quizConcluido || this.enviando) return;

    this.opcaoSelecionada = optionId;
    this.errou = false;
    this.opcaoConfirmada = null;
  }

  confirmarResposta() {
    if (this.opcaoSelecionada === null || this.enviando) return;

    this.enviando = true;
    const opcaoEnviada = this.opcaoSelecionada;

    this.quizService.answerQuiz(this.subtopicId, this.opcaoSelecionada).subscribe({
      next: (res) => {
        this.opcaoConfirmada = opcaoEnviada;
        this.enviando = false;

        if (res.correct) {
          this.errou = false;
          this.quizConcluido = true;
          this.concluido.emit();
          this.cdr.detectChanges();

          Swal.fire({
          title: '🎉 Resposta correta!',
          text: 'Parabéns! Você concluiu a prática.',
          icon: 'success',
          confirmButtonText: 'Continuar',
          confirmButtonColor: '#4CAF50',
        }).then(() => {
          this.concluido.emit();
        });


        } else {
          this.errou = true;
          this.opcaoSelecionada = null;
          this.cdr.detectChanges();
        }
      },
      error: (err) => {
        console.error('erro ao confirmar resposta:', err);
        this.enviando = false;
      }
    });
  }

  getClasse(optionId: number): string {
    if (this.quizConcluido && optionId === this.opcaoConfirmada) {
      return 'correta';
    }
    if (this.errou && optionId === this.opcaoConfirmada) {
      return 'incorreta';
    }
    if (this.opcaoSelecionada === optionId) {
      return 'selecionada';
    }
    return '';
  }

}
