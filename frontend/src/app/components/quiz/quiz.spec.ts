import { of, throwError } from 'rxjs';
import Swal from 'sweetalert2';
import { Quiz } from './quiz';

jest.mock('sweetalert2', () => ({
	__esModule: true,
	default: { fire: jest.fn().mockResolvedValue(undefined) },
}));

describe('Quiz', () => {
	let component: Quiz;
	let service: { searchQuiz: jest.Mock; answerQuiz: jest.Mock };
	let cdr: { detectChanges: jest.Mock };

	beforeEach(() => {
		service = { searchQuiz: jest.fn(), answerQuiz: jest.fn() };
		cdr = { detectChanges: jest.fn() };
		component = new Quiz(service as any, cdr as any);
		component.subtopicId = 7;
		(component.concluido.emit as jest.Mock) = jest.fn();
		(Swal.fire as jest.Mock).mockClear();
	});

	it('loads a question and leaves the loading state on errors', () => {
		const question = { question: '2 + 2?', options: [{ id: 1, text: '4' }] } as any;
		service.searchQuiz.mockReturnValueOnce(of(question));
		component.ngOnInit();
		expect(component.pergunta).toBe(question);
		expect(component.loading).toBe(false);

		service.searchQuiz.mockReturnValueOnce(throwError(() => new Error('offline')));
		component.loading = true;
		component.ngOnInit();
		expect(component.loading).toBe(false);
	});

	it('selects options and ignores changes after completion or while sending', () => {
		component.selecionarOpcao(2);
		expect(component.opcaoSelecionada).toBe(2);
		expect(component.getClasse(2)).toBe('selecionada');
		component.errou = true;
		component.opcaoConfirmada = 2;
		expect(component.getClasse(2)).toBe('incorreta');
		component.quizConcluido = true;
		expect(component.getClasse(2)).toBe('correta');
		expect(component.getClasse(99)).toBe('');

		component.enviando = true;
		component.selecionarOpcao(3);
		expect(component.opcaoSelecionada).toBe(2);
		component.enviando = false;
		component.quizConcluido = true;
		component.selecionarOpcao(4);
		expect(component.opcaoSelecionada).toBe(2);
	});

	it('confirms a wrong answer and clears it for another attempt', () => {
		component.opcaoSelecionada = 2;
		service.answerQuiz.mockReturnValueOnce(of({ correct: false }));
		component.confirmarResposta();
		expect(component.enviando).toBe(false);
		expect(component.errou).toBe(true);
		expect(component.opcaoSelecionada).toBeNull();
		expect(component.getClasse(2)).toBe('incorreta');
});

	it('completes a correct answer, shows feedback and handles request errors', async () => {
		component.opcaoSelecionada = 1;
		service.answerQuiz.mockReturnValueOnce(of({ correct: true }));
		component.confirmarResposta();
		expect(component.quizConcluido).toBe(true);
		expect(component.concluido.emit).toHaveBeenCalled();
		expect(Swal.fire).toHaveBeenCalled();
		await Promise.resolve();

		component.quizConcluido = false;
		component.opcaoSelecionada = 1;
		service.answerQuiz.mockReturnValueOnce(throwError(() => new Error('offline')));
		component.confirmarResposta();
		expect(component.enviando).toBe(false);
	});

	it('does not submit without a selected option or during another request', () => {
		component.confirmarResposta();
		expect(service.answerQuiz).not.toHaveBeenCalled();
		component.opcaoSelecionada = 1;
		component.enviando = true;
		component.confirmarResposta();
		expect(service.answerQuiz).not.toHaveBeenCalled();
	});
});
