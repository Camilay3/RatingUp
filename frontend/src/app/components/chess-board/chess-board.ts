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
	private previousFen: string = 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1';

	moves: {
		from: string;
		to: string;
		piece?: string;
		status?: string;
		extra?: string;
		isSystem?: boolean;
		error?: boolean;
	}[] = [];

	constructor(
		private chessService: ChessService,
		private cdr: ChangeDetectorRef,
	) {}

	ngOnInit(): void {
		this.cg = Chessground(this.boardRef.nativeElement, {
			fen: 'rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1',
			orientation: this.orientation,
			movable: {
				free: true,
				events: {
					after: (orig: string, dest: string) => this.onMove(orig, dest),
				},
			},
			events: {
				select: (orig: string) => {
					const pieces = this.cg.state.pieces;
					const piece = pieces.get(orig);
					if (piece) {
						this.currentPiece = `${piece.color}_${piece.role}`.toUpperCase();
					}
				},
			},
		});
		this.onStartChess(this.subtopicId);
	}

	ngOnChanges(): void {
		if (this.cg && this.subtopicId && !this.sessionId) {
			this.onStartChess(this.subtopicId);
		}
	}

	private fenToPositions(fen: string): Map<string, string> {
		const positions = new Map<string, string>();
		const board = fen.split(' ')[0];
		const rows = board.split('/');

		rows.forEach((row, rowIdx) => {
			let colIdx = 0;
			for (const char of row) {
				if (isNaN(Number(char))) {
					const square = String.fromCharCode(97 + colIdx) + (8 - rowIdx);
					positions.set(square, char);
					colIdx++;
				} else {
					colIdx += Number(char);
				}
			}
		});

		return positions;
	}

	private getComputerMove(
		oldFen: string,
		newFen: string,
	): { from: string; to: string; piece: string } | null {
		const oldPos = this.fenToPositions(oldFen);
		const newPos = this.fenToPositions(newFen);

		let from = '';
		let to = '';
		let movedPiece = '';

		for (const [square, piece] of oldPos) {
			if (!newPos.has(square)) {
				from = square;
				movedPiece = piece;
				break;
			}
		}

		for (const [square, piece] of newPos) {
			if (!oldPos.has(square) && piece === movedPiece) {
				to = square;
				break;
			}
		}

		if (from && to) {
			return {
				from,
				to,
				piece: this.mapPieceToName(movedPiece),
			};
		}

		return null;
	}

	private mapPieceToName(piece: string): string {
		const map: { [key: string]: string } = {
			k: 'BLACK_KING',
			q: 'BLACK_QUEEN',
			r: 'BLACK_ROOK',
			b: 'BLACK_BISHOP',
			n: 'BLACK_KNIGHT',
			p: 'BLACK_PAWN',
			K: 'WHITE_KING',
			Q: 'WHITE_QUEEN',
			R: 'WHITE_ROOK',
			B: 'WHITE_BISHOP',
			N: 'WHITE_KNIGHT',
			P: 'WHITE_PAWN',
		};
		return map[piece] || piece;
	}

	async onMove(orig: string, dest: string) {
		try {
			const { fen, status, initialFen } = await this.chessService.moveChess(
				this.sessionId,
				this.currentPiece,
				orig.toUpperCase(),
				dest.toUpperCase(),
			);

			this.moves = [
				...this.moves,
				{
					from: orig,
					to: dest,
					piece: this.currentPiece,
					status,
					isSystem: false,
					error: status === 'WRONG_MOVE',
				},
			];

			if (status === 'WRONG_MOVE') {
				this.cg.set({ fen: initialFen });
				this.previousFen = initialFen;
				this.cdr.detectChanges();
				return;
			}

			if (status !== 'COMPLETED') {
				const computerMove = this.getComputerMove(this.previousFen, fen);

				if (computerMove) {
					this.moves = [
						...this.moves,
						{
							from: computerMove.from,
							to: computerMove.to,
							piece: computerMove.piece,
							status: 'NORMAL',
							isSystem: true,
						},
					];
				}
			}

			this.cg.set({ fen });
			this.previousFen = fen;

			if (status == 'COMPLETED') {
				this.moves = [
					...this.moves,
					{
						from: '',
						to: '',
						status: 'COMPLETED',
						extra: 'PARABÉNS, VOCÊ CONCLUIU A PRÁTICA. Conclua o subtópico para continuar seu aprendizado.',
						isSystem: true,
					},
				];
				this.cg.set({ movable: { color: undefined } });
				this.concluido.emit();
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
			this.previousFen = fen;
			this.cg.set({ fen });
			this.fenAtualizado.emit(fen);
		} catch (e) {
			console.error('Erro no start chess', e);
		}
	}
}
