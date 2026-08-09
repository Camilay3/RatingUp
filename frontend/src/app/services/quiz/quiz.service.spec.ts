import { of } from 'rxjs';
import { QuizService } from './quiz.service';

describe('QuizService', () => {
	it('loads a quiz and submits the selected option', () => {
		const http = { post: jest.fn().mockReturnValue(of({ correct: true })) };
		const service = new QuizService(http as any);

		service.searchQuiz(3).subscribe();
		service.answerQuiz(3, 9).subscribe();

		expect(http.post).toHaveBeenNthCalledWith(1, expect.stringContaining('/move/session/quiz'), { subtopicId: 3 });
		expect(http.post).toHaveBeenNthCalledWith(2, expect.stringContaining('/move/session/quiz/answer'), {
			subtopicId: 3,
			selectedOptionId: 9,
		});
	});
});
