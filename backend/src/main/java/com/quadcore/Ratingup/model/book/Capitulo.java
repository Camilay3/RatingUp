package com.quadcore.Ratingup.model.book;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Capitulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private Integer ordem;

    @OneToMany(mappedBy = "capitulo", fetch = FetchType.LAZY)
    @OrderBy("ordem ASC")
    private List<Subtopico> subtopicos;

}
