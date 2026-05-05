package com.quadcore.Ratingup.model.book;

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
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapters chapter;
}
