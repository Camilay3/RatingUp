import { Component, OnInit, ViewChild, Input, OnChanges, ElementRef, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { Chessground } from 'chessground';
import { ChessService } from '../../services/chess/chess.service';
import { IQuiz } from '../../interfaces/chess/iquiz';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-chess-board',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './chess-board.html',
  styleUrl: './chess-board.scss',
})

export class ChessBoard implements OnInit, OnChanges {
  @ViewChild('board', {static: true}) boardRef!: ElementRef;
  @Input() orientation: 'white'|'black' = 'white';
  @Input() subtopicId!: number;
  @Output() fenAtualizado = new EventEmitter<string>();

  pergunta: IQuiz | null = null;
  opcaoSelecionada: number | null = null;
  quizResolvido = false;   // controla se o blur some
  errou = false;
  loading = true;

  constructor(
    private chessService : ChessService,
    private cdr : ChangeDetectorRef
  )
  {}

  private cg: any;
  private sessionId!: number;
  private currentPiece: string = '';

  moves: {
    from: string,
    to: string,
    piece?: string,
    status?:string
  }[] = [];

  ngOnInit(): void {
  this.cg = Chessground(this.boardRef.nativeElement, {
    fen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
    orientation: this.orientation,
    movable: {
      free: true,
      events: {
         after: (orig: string, dest: string) => this.onMove(orig, dest)
      }
    },
    events: {
      select: (orig: string) => {
        const pieces = this.cg.state.pieces;
        const piece = pieces.get(orig);
        if (piece) {
          this.currentPiece = `${piece.color}_${piece.role}`.toUpperCase();
        }
      }
    }
  });

  this.onStartChess(this.subtopicId);
  console.log('subtopicId:', this.subtopicId);

  this.chessService.searchQuiz(this.subtopicId).subscribe({
  next: (data) => {
    console.log('quiz recebido:', data); // 👈
    this.pergunta = data;
    this.loading = false;
    this.cdr.detectChanges(); // 👈 força atualização da tela
  },
  error: (err) => {
    console.error('erro no quiz:', err); // 👈
    this.loading = false;
  }
  });
 }

  ngOnChanges(): void {
    if (this.cg && this.subtopicId && !this.sessionId) {
    this.onStartChess(this.subtopicId);
    }
    
  }

  async onMove(orig: string, dest: string) {
   try {
    const { fen, status } = await this.chessService.moveChess(
      this.sessionId,
      this.currentPiece,
      orig.toUpperCase(),
      dest.toUpperCase()
    );

    this.cg.set({ fen });

    if(status == 'COMPLETED') {
     this.moves = [...this.moves, { from: orig, to: dest, piece: this.currentPiece, status }];
    }

    this.cdr.detectChanges();

   } catch (e: any) {
    this.moves = [];
    await this.onStartChess(this.subtopicId); 
   }
  }

  async onStartChess(subtopicoId: number){
    console.log("Chamando os startChess");

    try{
     const { sessionId , fen } = await this.chessService.startChess(subtopicoId)
     console.log("Session id", sessionId);
     this.sessionId = sessionId
     this.cg.set({ fen })
     this.fenAtualizado.emit(fen)
    }catch(e){
      console.error("Erro no start chess",e);
      
    }

  }

  selecionarOpcao(optionId: number) {
    this.opcaoSelecionada = optionId;
  }

  confirmarResposta() {
  if (this.opcaoSelecionada === null) return;

  this.chessService.answerQuiz(this.subtopicId, this.opcaoSelecionada).subscribe({
    next: (res) => {
      if (res.correct) {
        this.quizResolvido = true;
        this.cdr.detectChanges();
      } else {
        this.errou = true;
        this.opcaoSelecionada = null;
        this.cdr.detectChanges();
      }
    }
  });
}

}
