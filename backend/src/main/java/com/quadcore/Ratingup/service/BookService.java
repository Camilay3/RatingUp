package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.book.PageContentDTO;
import com.quadcore.Ratingup.dto.book.BookDTO;
import com.quadcore.Ratingup.dto.book.PageDTO;
import com.quadcore.Ratingup.model.book.Chapters;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.repository.ChaptersRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final ChaptersRepository chaptersRepository;

    public BookService(ChaptersRepository chapterRepository) {
        this.chaptersRepository = chapterRepository;
    }

    public BookDTO buildBook() {
        List<Chapters> capitulos = chaptersRepository.findAllByOrderbyOrderAsc();

        List<PageContentDTO> conteudos = new ArrayList<>();

        for (Chapters cap : capitulos) {
            conteudos.add(new PageContentDTO("capitulo", cap.getId(), null, cap.getTitle()));

            for (Subtopics sub : cap.getSubtopics()) {
                conteudos.add(new PageContentDTO("subtópico", sub.getId(), cap.getId(), sub.getTitle()));
            }
        }

        List<PageDTO> paginas = new ArrayList<>();
        for (int i = 0; i < conteudos.size() - 1; i += 2) {
            PageContentDTO verso = (i + 1 < conteudos.size()) ? conteudos.get(i+1) : null;
            paginas.add(new PageDTO(conteudos.get(i), verso));
        }

        return new BookDTO(paginas, paginas.size());
    }
}
