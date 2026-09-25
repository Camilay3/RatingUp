import { of } from 'rxjs';
import { BookService } from './book.service';

describe('BookService', () => {
	it('uses the book endpoints and forwards their arguments', () => {
		const http = { get: jest.fn().mockReturnValue(of({})), post: jest.fn().mockReturnValue(of({})) };
		const service = new BookService(http as any);

		service.getSheets().subscribe();
		service.getSubtopicContent(5).subscribe();
		service.getSubtopicType(5).subscribe();

		expect(http.get).toHaveBeenNthCalledWith(1, expect.stringContaining('/livro/paginas'));
		expect(http.get).toHaveBeenNthCalledWith(2, expect.stringContaining('/subtopics/5/content'));
		expect(http.get).toHaveBeenNthCalledWith(3, expect.stringContaining('/subtopics/5/type'));
	});
});
