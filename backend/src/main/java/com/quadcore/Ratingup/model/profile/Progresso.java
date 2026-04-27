package com.quadcore.Ratingup.model.profile;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.*;

@Entity(name = "progresso")
@Table(name = "progresso")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Progresso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Max(9)
    @Column(nullable = false)
    private Integer capitulo = 1;

    @Column(nullable = false)
    private Integer subtopico = 1;

}
