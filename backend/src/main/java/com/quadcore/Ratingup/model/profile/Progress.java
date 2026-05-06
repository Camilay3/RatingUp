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
@EqualsAndHashCode(of = "id")
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

}
