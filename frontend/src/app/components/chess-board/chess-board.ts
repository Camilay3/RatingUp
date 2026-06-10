import { Component, OnInit, ViewChild, Input, OnChanges, ElementRef, Output, EventEmitter } from '@angular/core';
import { Chessground } from 'chessground';
import { ChessService } from '../../services/chess/chess.service';
import { ChessPiece } from '../../interfaces/chess/chess-piece.enum';

@Component({
  selector: 'app-chess-board',
  standalone: true,
  imports: [],
  templateUrl: './chess-board.html',
  styleUrl: './chess-board.scss',
})

export class ChessBoard implements OnInit, OnChanges {
  @ViewChild('board', {static: true}) boardRef!: ElementRef;
  @Input() orientation: 'white'|'black' = 'white';
  @Input() subtopicId!: number;
  @Output() fenAtualizado = new EventEmitter<string>();

  constructor(private chessService : ChessService){}

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

  // ngOnInit roda DEPOIS dos @Inputs chegarem, então subtopicoId já existe aqui
  console.log('subtopicoId no ngOnInit:', this.subtopicId);
  this.onStartChess(this.subtopicId);
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

    console.log('fen retornado:', fen);
    console.log('status retornado:', status);

    this.cg.set({ fen });

    if (status === 'WRONG_MOVE') {
      console.log('Movimento errado, tente novamente!');
    } else if (status === 'COMPLETED') {
      console.log('Parabéns! Sequência concluída!');
    }

    this.moves.push({ from: orig, to: dest, piece: this.currentPiece, status });

   } catch (e: any) {
    // movimento inválido — back retorna 400
    console.log('Movimento inválido, resetando tabuleiro');
    await this.onStartChess(this.subtopicId); // reinicia a sessão
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

}
