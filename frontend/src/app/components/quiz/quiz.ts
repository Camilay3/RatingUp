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
          this.errou = false;  // ← reseta o erro
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
      }
    });
  }
}