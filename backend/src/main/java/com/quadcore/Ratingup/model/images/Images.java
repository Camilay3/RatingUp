package com.quadcore.Ratingup.model.images;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity(name = "images")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Images {

    public Images(String objectId, String imageName) {
        this.objectId = objectId;
        this.imageName = imageName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String objectId;
    private String imageName;
}
