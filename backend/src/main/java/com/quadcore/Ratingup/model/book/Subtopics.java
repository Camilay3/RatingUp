package com.quadcore.Ratingup.model.book;

import com.quadcore.Ratingup.enums.SubtopicType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Subtopics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Column(name = "display_order")
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapters chapter;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 100)
    private String initialFen;

    @Column
    private String solutionMoves;

    @Enumerated(EnumType.STRING)
    private SubtopicType type;
}
