package com.quadcore.Ratingup.model.board;

import com.quadcore.Ratingup.enums.BoardStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubtopicPracticeSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long subtopicId;

    @Column(length = 100)
    private String currentFen;

    @Enumerated(EnumType.STRING)
    private BoardStatus status;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(length = 500)
    private String movesPlayed;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubtopicPracticeSession)) return false;
        SubtopicPracticeSession other = (SubtopicPracticeSession) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}