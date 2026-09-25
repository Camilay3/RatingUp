package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.book.BookDTO;
import com.quadcore.Ratingup.dto.book.PageDTO;
import com.quadcore.Ratingup.dto.book.SubtopicRequestDTO;
import com.quadcore.Ratingup.dto.book.SubtopicResponseDTO;
import com.quadcore.Ratingup.model.book.Chapters;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.model.images.Images;
import com.quadcore.Ratingup.repository.ChaptersRepository;
import com.quadcore.Ratingup.repository.ImagesRepository;
import com.quadcore.Ratingup.repository.SubtopicsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private ChaptersRepository chaptersRepository;
    
    @Mock
    private SubtopicsRepository subtopicsRepository;
    
    @Mock
    private ImagesRepository imagesRepository;

    @InjectMocks
    private BookService bookService;

    private Chapters chapter;
    private Subtopics subtopic;
    private Images image;

    @BeforeEach
    void setUp() {
        chapter = new Chapters();
        chapter.setId(1L);
        chapter.setTitle("Capitulo 1");
        chapter.setDisplayOrder(1);
        chapter.setSubtopics(new ArrayList<>());

        subtopic = new Subtopics();
        subtopic.setId(1L);
        subtopic.setTitle("Subtopico 1");
        subtopic.setDisplayOrder(1);
        subtopic.setChapter(chapter);

        chapter.getSubtopics().add(subtopic);

        image = new Images();
        image.setId(1L);
        image.setImageName("Subtopico-1.png");
        image.setObjectId("obj-id-123");
    }

    @Test
    void buildBook_ShouldReturnBookDTO_Successfully() throws Exception {
        when(chaptersRepository.findAllByOrderbyOrderAsc()).thenReturn(List.of(chapter));
        when(imagesRepository.findByImageName(anyString())).thenReturn(Optional.of(image));

        BookDTO book = bookService.buildBook();

        assertThat(book).isNotNull();
        assertThat(book.totalPages()).isEqualTo(1);
        List<PageDTO> pages = book.pages();
        assertThat(pages).hasSize(1);
        assertThat(pages.get(0).front().type()).isEqualTo("capitulo");
        assertThat(pages.get(0).verse().type()).isEqualTo("subtópico");
    }

    @Test
    void addSubtopic_ShouldAddAndReorderSuccessfully() {
        SubtopicRequestDTO dto = new SubtopicRequestDTO(1L, "Novo Subtopico", 1);
        
        when(chaptersRepository.findById(1L)).thenReturn(Optional.of(chapter));
        
        Subtopics savedSub = new Subtopics();
        savedSub.setId(3L);
        savedSub.setTitle("Novo Subtopico");
        savedSub.setDisplayOrder(1);
        savedSub.setChapter(chapter);
        when(subtopicsRepository.save(any(Subtopics.class))).thenReturn(savedSub);

        Subtopics existingSub = new Subtopics();
        existingSub.setId(2L);
        existingSub.setDisplayOrder(2);
        existingSub.setChapter(chapter);
        
        List<Subtopics> existingSubs = new ArrayList<>();
        existingSubs.add(savedSub);
        existingSubs.add(existingSub);
        
        when(subtopicsRepository.findByChapter_IdOrderByDisplayOrderAsc(1L)).thenReturn(existingSubs);

        List<SubtopicResponseDTO> response = bookService.addSubtopic(dto);

        assertThat(response).hasSize(2);
        verify(subtopicsRepository, times(1)).incrementDisplayOrderFrom(1L, 1);
    }

    @Test
    void addSubtopic_ShouldThrowEntityNotFound_WhenChapterNotFound() {
        SubtopicRequestDTO dto = new SubtopicRequestDTO(99L, "Novo", 1);
        when(chaptersRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.addSubtopic(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Capítulo não encontrado");
    }

    @Test
    void getSubtopicContent_ShouldReturnDTO_WhenFound() {
        when(subtopicsRepository.findById(1L)).thenReturn(Optional.of(subtopic));

        SubtopicResponseDTO response = bookService.getSubtopicContent(1L);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.title()).isEqualTo("Subtopico 1");
    }

    @Test
    void getSubtopicContent_ShouldThrowEntityNotFound_WhenNotFound() {
        when(subtopicsRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> bookService.getSubtopicContent(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Subtópico não encontrado");
    }
}
