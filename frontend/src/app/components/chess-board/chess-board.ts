import { Component, OnInit, ViewChild, Input, OnChanges, ElementRef, Output, EventEmitter, ChangeDetectorRef } from '@angular/core';
import { Chessground } from 'chessground';
import { ChessService } from '../../services/chess/chess.service';

@Component({
  selector: 'app-chess-board',
  standalone: true,
  imports: [],
  templateUrl: './chess-board.html',
  styleUrl: './chess-board.scss',
})
export class ChessBoard implements OnInit, OnChanges {
  @ViewChild('board', { static: true }) boardRef!: ElementRef;
  @Input() orientation: 'white' | 'black' = 'white';
  @Input() subtopicId!: number;
  @Output() fenAtualizado = new EventEmitter<string>();
  @Output() concluido = new EventEmitter<void>();

  private cg: any;
  private sessionId!: number;
  private currentPiece: string = '';

  moves: {
    from: string,
    to: string,
    piece?: string,
    status?: string,
    extra?: string
  }[] = [];

  constructor(
    private chessService: ChessService,
    private cdr: ChangeDetectorRef
  ) {}

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

      if (status == 'COMPLETED') {
        this.moves = [...this.moves, { from: orig, to: dest, piece: this.currentPiece, status, extra: 'PARABÉNS, VOCÊ CONCLUIU A PRÁTICA. Conclua o subtópico para continuar seu aprendizado.' }];
        this.cg.set({ movable: { color: undefined } })
        this.concluido.emit();
        this.cdr.detectChanges();
      }

      this.cdr.detectChanges();
    } catch (e: any) {
      this.moves = [];
      await this.onStartChess(this.subtopicId);
    }
  }

  async onStartChess(subtopicoId: number) {
    try {
      const { sessionId, fen } = await this.chessService.startChess(subtopicoId);
      this.sessionId = sessionId;
      this.cg.set({ fen });
      this.fenAtualizado.emit(fen);
    } catch (e) {
      console.error('Erro no start chess', e);
    }
  }
}
