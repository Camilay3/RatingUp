package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.book.*;
import com.quadcore.Ratingup.mapper.SubtopicsMapper;
import com.quadcore.Ratingup.model.book.Chapters;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.repository.ChaptersRepository;
import com.quadcore.Ratingup.repository.SubtopicsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BookService {

    private final ChaptersRepository chaptersRepository;
    private final SubtopicsRepository subtopicsRepository;

    public BookService(ChaptersRepository chaptersRepository, SubtopicsRepository subtopicsRepository) {
        this.chaptersRepository = chaptersRepository;
        this.subtopicsRepository = subtopicsRepository;
    }

    public BookDTO buildBook() {
        List<Chapters> chapters = chaptersRepository.findAllByOrderbyOrderAsc();

        List<PageContentDTO> contents = new ArrayList<>();

        for (Chapters cap : chapters) {
            contents.add(new PageContentDTO("capitulo", cap.getId(), null, cap.getTitle(), cap.getDisplayOrder()));

            for (Subtopics sub : cap.getSubtopics()) {
                contents.add(new PageContentDTO("subtópico", sub.getId(), cap.getId(), sub.getTitle(), sub.getDisplayOrder()));
            }
        }

        List<PageDTO> paginas = new ArrayList<>();
        for (int i = 0; i < contents.size() - 1; i += 2) {
            PageContentDTO verso = (i + 1 < contents.size()) ? contents.get(i+1) : null;
            paginas.add(new PageDTO(contents.get(i), verso));
        }

        return new BookDTO(paginas, paginas.size());
    }

    public List<SubtopicResponseDTO> addSubtopic(SubtopicRequestDTO dto) {
        Chapters capitulo = chaptersRepository.findById(dto.chapterId())
                .orElseThrow(() -> new EntityNotFoundException("Capítulo não encontrado"));
        List<Subtopics> subtopicos = subtopicsRepository.findByChapter_IdOrderByDisplayOrderAsc(dto.chapterId());

        for (Subtopics subs : subtopicos) {
            if (subs.getDisplayOrder() >= dto.displayOrder()) {
                subs.setDisplayOrder(subs.getDisplayOrder() + 1);
            }
        }
        subtopicsRepository.saveAll(subtopicos);

        Subtopics salvo = subtopicsRepository.save(new Subtopics(null, dto.title(), dto.displayOrder(), capitulo,null,null,null));
        subtopicos.add(salvo);
        subtopicos.sort(Comparator.comparingInt(Subtopics::getDisplayOrder));

        return subtopicos
                .stream()
                .map(SubtopicsMapper::toResponseDTO)
                .toList();
    }

    public SubtopicResponseDTO getSubtopicContent(Long id) {
        Subtopics sub = subtopicsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Subtópico não encontrado"));
        return SubtopicsMapper.toResponseDTO(sub);
    }
}
