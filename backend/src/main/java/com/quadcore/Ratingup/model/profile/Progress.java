package com.quadcore.Ratingup.model.profile;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

@Entity(name = "progress")
@Table(name = "progress")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Max(9)
    @Column(nullable = false)
    private Integer chapters = 1;

    @Column(nullable = false)
    private Integer subtopics = 1;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Progress)) return false;
        Progress other = (Progress) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}