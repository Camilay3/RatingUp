package com.quadcore.Ratingup.model.book;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Subtopico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer ordem;

    @ManyToOne
    @JoinColumn(name = "capitulo_id")
    private Capitulo capitulo;


}
