package com.quadcore.Ratingup.model.board;

import com.quadcore.Ratingup.model.book.Subtopics;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class MultipleChoiceQuestion {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "subtopic_id")
    private Subtopics subtopic;

    @Column(columnDefinition = "TEXT")
    private String questionText;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<MultipleChoiceOption> options;
}
