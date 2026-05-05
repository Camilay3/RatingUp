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
public class Chapters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer displayOrder;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY)
    @OrderBy("ordem ASC")
    private List<Subtopics> subtopics;

}
