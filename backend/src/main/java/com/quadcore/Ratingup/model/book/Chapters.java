package com.quadcore.Ratingup.model.book;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Chapters {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Integer displayOrder;

    @OneToMany(mappedBy = "chapter", fetch = FetchType.LAZY)
    @OrderBy("display_order ASC")
    private List<Subtopics> subtopics;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Chapters)) return false;
        Chapters other = (Chapters) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}