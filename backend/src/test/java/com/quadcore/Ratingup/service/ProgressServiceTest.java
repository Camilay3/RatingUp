package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.progresso.ProgressResponseDTO;
import com.quadcore.Ratingup.dto.progresso.ProgressUpdateDTO;
import com.quadcore.Ratingup.model.book.Chapters;
import com.quadcore.Ratingup.model.book.Subtopics;
import com.quadcore.Ratingup.model.profile.Progress;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ChaptersRepository;
import com.quadcore.Ratingup.repository.ProgressRepository;
import com.quadcore.Ratingup.repository.SubtopicsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private ProgressRepository progressRepository;
    
    @Mock
    private SubtopicsRepository subtopicsRepository;
    
    @Mock
    private ChaptersRepository chaptersRepository;

    @InjectMocks
    private ProgressService progressService;

    private Progress progress;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        progress = new Progress();
        progress.setId(1L);
        progress.setUser(user);
        progress.setChapters(1);
        progress.setSubtopics(1);
    }

    @Test
    void allowedPhases_ShouldReturnResponseDTO_WhenFound() {
        when(progressRepository.findByUserEmail(anyString())).thenReturn(Optional.of(progress));

        ProgressResponseDTO response = progressService.allowedPhases("test@test.com");

        assertThat(response).isNotNull();
        assertThat(response.userId()).isEqualTo(1L);
        assertThat(response.chapter()).isEqualTo(1);
        assertThat(response.subtopic()).isEqualTo(1);
    }

    @Test
    void allowedPhases_ShouldThrowException_WhenNotFound() {
        when(progressRepository.findByUserEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> progressService.allowedPhases("notfound@test.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Progresso não encontrado");
    }

    @Test
    void updateCurrentPhase_ShouldUpdateSubtopic_WhenNotLastSubtopic() {
        ProgressUpdateDTO dto = new ProgressUpdateDTO(1, 1);
        
        when(progressRepository.findByUserEmail("test@test.com")).thenReturn(Optional.of(progress));
        
        Chapters lastChapter = new Chapters();
        lastChapter.setDisplayOrder(2);
        when(chaptersRepository.findTopByOrderByDisplayOrderDesc()).thenReturn(Optional.of(lastChapter));
        
        Subtopics lastSubtopic = new Subtopics();
        lastSubtopic.setDisplayOrder(2);
        when(subtopicsRepository.findTopByChapterIdOrderByDisplayOrderDesc(1)).thenReturn(Optional.of(lastSubtopic));

        when(progressRepository.save(any(Progress.class))).thenAnswer(i -> i.getArgument(0));

        ProgressResponseDTO response = progressService.updateCurrentPhase("test@test.com", dto);

        assertThat(response.chapter()).isEqualTo(1);
        assertThat(response.subtopic()).isEqualTo(2);
        verify(progressRepository, times(1)).save(any(Progress.class));
    }

    @Test
    void updateCurrentPhase_ShouldUpdateChapter_WhenLastSubtopic() {
        ProgressUpdateDTO dto = new ProgressUpdateDTO(1, 1);
        
        when(progressRepository.findByUserEmail("test@test.com")).thenReturn(Optional.of(progress));
        
        Chapters lastChapter = new Chapters();
        lastChapter.setDisplayOrder(2);
        when(chaptersRepository.findTopByOrderByDisplayOrderDesc()).thenReturn(Optional.of(lastChapter));
        
        Subtopics lastSubtopic = new Subtopics();
        lastSubtopic.setDisplayOrder(1);
        when(subtopicsRepository.findTopByChapterIdOrderByDisplayOrderDesc(1)).thenReturn(Optional.of(lastSubtopic));

        when(progressRepository.save(any(Progress.class))).thenAnswer(i -> i.getArgument(0));

        ProgressResponseDTO response = progressService.updateCurrentPhase("test@test.com", dto);

        assertThat(response.chapter()).isEqualTo(2);
        assertThat(response.subtopic()).isEqualTo(1);
        verify(progressRepository, times(1)).save(any(Progress.class));
    }

    @Test
    void updateCurrentPhase_ShouldThrowException_WhenMaxProgressReached() {
        progress.setChapters(2);
        progress.setSubtopics(2);
        ProgressUpdateDTO dto = new ProgressUpdateDTO(2, 2);
        
        when(progressRepository.findByUserEmail("test@test.com")).thenReturn(Optional.of(progress));
        
        Chapters lastChapter = new Chapters();
        lastChapter.setDisplayOrder(2);
        when(chaptersRepository.findTopByOrderByDisplayOrderDesc()).thenReturn(Optional.of(lastChapter));
        
        Subtopics lastSubtopic = new Subtopics();
        lastSubtopic.setDisplayOrder(2);
        when(subtopicsRepository.findTopByChapterIdOrderByDisplayOrderDesc(2)).thenReturn(Optional.of(lastSubtopic));

        assertThatThrownBy(() -> progressService.updateCurrentPhase("test@test.com", dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Progresso máximo alcançado!");
    }

    @Test
    void updateCurrentPhase_ShouldThrowException_WhenPhaseAlreadyCompleted() {
        progress.setChapters(2);
        progress.setSubtopics(2);
        ProgressUpdateDTO dto = new ProgressUpdateDTO(1, 1);
        
        when(progressRepository.findByUserEmail("test@test.com")).thenReturn(Optional.of(progress));
        
        Chapters lastChapter = new Chapters();
        lastChapter.setDisplayOrder(2);
        when(chaptersRepository.findTopByOrderByDisplayOrderDesc()).thenReturn(Optional.of(lastChapter));
        
        Subtopics lastSubtopic = new Subtopics();
        lastSubtopic.setDisplayOrder(2);
        when(subtopicsRepository.findTopByChapterIdOrderByDisplayOrderDesc(1)).thenReturn(Optional.of(lastSubtopic));

        assertThatThrownBy(() -> progressService.updateCurrentPhase("test@test.com", dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Fase já concluída.");
    }
    
    @Test
    void updateCurrentPhase_ShouldThrowException_WhenInvalidProgress() {

        progress.setChapters(1);
        progress.setSubtopics(1);
        ProgressUpdateDTO dto = new ProgressUpdateDTO(2, 1);
        
        when(progressRepository.findByUserEmail("test@test.com")).thenReturn(Optional.of(progress));
        
        Chapters lastChapter = new Chapters();
        lastChapter.setDisplayOrder(2);
        when(chaptersRepository.findTopByOrderByDisplayOrderDesc()).thenReturn(Optional.of(lastChapter));
        
        Subtopics lastSubtopic = new Subtopics();
        lastSubtopic.setDisplayOrder(2);
        when(subtopicsRepository.findTopByChapterIdOrderByDisplayOrderDesc(2)).thenReturn(Optional.of(lastSubtopic));

        assertThatThrownBy(() -> progressService.updateCurrentPhase("test@test.com", dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Progresso inválido");
    }
}
