package com.quadcore.Ratingup.model.images;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "images")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Images {

    public Images(String objectId, String imageName, String bucketName) {
        this.objectId = objectId;
        this.imageName = imageName;
        this.bucketName = bucketName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String objectId;
    private String imageName;
    private String bucketName;
}
