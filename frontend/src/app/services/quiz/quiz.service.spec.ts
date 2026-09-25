import { of } from 'rxjs';
import { QuizService } from './quiz.service';

describe('QuizService', () => {
	it('loads a quiz and submits the selected option', () => {
		const http = { get: jest.fn().mockReturnValue(of({})), post: jest.fn().mockReturnValue(of({ correct: true })) };
		const service = new QuizService(http as any);

		service.searchQuiz(3).subscribe();
		service.answerQuiz(3, 9).subscribe();

		expect(http.get).toHaveBeenNthCalledWith(1, expect.stringContaining('/multiple-choice/session/quiz/3'));
		expect(http.post).toHaveBeenNthCalledWith(1, expect.stringContaining('/multiple-choice/session/quiz/answer'), {
			subtopicId: 3,
			selectedOptionId: 9,
		});
	});
});
