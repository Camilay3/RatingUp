import { Component, OnInit, ViewChild, Input, OnChanges, ElementRef } from '@angular/core';
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
  @Input() fen: string = 'start';
  @Input() orientation: 'white'|'black' = 'white';
  @Input() subtopicoId!: number;

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
    fen: this.fen,
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
  console.log('subtopicoId no ngOnInit:', this.subtopicoId);
  this.onStartChess(this.subtopicoId);
 }

  ngOnChanges(): void {
    if (this.cg && this.subtopicoId && !this.sessionId) {
    this.onStartChess(this.subtopicoId);
    }
    if (this.cg) {
      this.cg.set({ fen: this.fen });
    }
  }

  async onMove(orig: string, dest: string){

      console.log('sessionId:', this.sessionId);
  console.log('piece:', this.currentPiece);
  console.log('orig:', orig.toUpperCase());
  console.log('dest:', dest.toUpperCase());


    const { fen , status } = await this.chessService.moveChess(
      this.sessionId,
      this.currentPiece,
      orig.toUpperCase(),
      dest.toUpperCase()
    )

    this.cg.set({ fen });

    this.moves.push({
      from: orig,
      to: dest,
      piece: this.currentPiece,
      status: status
    })

  }

  async onStartChess(subtopicoId: number){
    const { sessionId , fen } = await this.chessService.startChess(subtopicoId)
    this.sessionId = sessionId
    this.cg.set({ fen })
  }

}
