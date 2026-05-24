package com.quadcore.Ratingup.dto.profile;

import com.quadcore.Ratingup.model.images.Images;

public record AvatarResponseDTO(
        String imageName,
        String avatarurl
) {
    public AvatarResponseDTO(Images images){
        this(
                images.getImageName(),
                "/avatar/view/" + images.getImageName()
        );
    }
    //Aqui estará o DTO para transportar a lista de imagens em forma de URL
}
