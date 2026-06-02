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

    @Column(name = "display_order")
    private Integer displayOrder;

    @ManyToOne
    @JoinColumn(name = "chapter_id")
    private Chapters chapter;

    @Column(length = 100)
    private String initialFen;//LEMBRAR DE GERAR ESSE FEN NA MIGRATION(mateus)
}
