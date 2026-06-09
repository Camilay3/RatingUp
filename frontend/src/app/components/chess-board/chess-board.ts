import { Component, OnInit, ViewChild, Input, OnChanges, ElementRef } from '@angular/core';
import { Chessground } from 'chessground';
import { ChessService } from '../../services/chess/chess.service';
import { ChessPiece } from '../../interfaces/chess/chess-piece.enum';

@Component({
  selector: 'app-chess-board',
  imports: [],
  templateUrl: './chess-board.html',
  styleUrl: './chess-board.scss',
})

export class ChessBoard implements OnInit, OnChanges {
  @ViewChild('board', {static: true}) boardRef!: ElementRef;
  @Input() fen: string = 'start';
  @Input() orientation: 'white'|'black' = 'white';

  private cg: any;
  moves: {
    from: string,
    to: string,
    piece?: string
  }[] = [];

  ngOnInit(): void {
    this.cg = Chessground(this.boardRef.nativeElement, {
      fen: this.fen,
      orientation: this.orientation,
      movable: {
        free: false,
        events: {
          after: (orig: string,dest: string) => this.onMove(orig,dest)
        }
      }
    });
  }

  ngOnChanges(): void {
    if (this.cg) {
      this.cg.set({ fen: this.fen });
    }
  }

  onMove(orig: string, dest: string): void {
    //Por enquanto só vai printar a jogada, mas quando a logica estiver pronta no backend
    //quando o back estiver pronto coloca a logica de chamada
    console.log(`Jogada: ${orig} → ${dest}`);

    this.moves.push({
      from: orig,
      to: dest
    })

  }

  onStartChess(subtopico: number){

  }

}
