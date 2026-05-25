package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.profile.AvatarResponseDTO;
import com.quadcore.Ratingup.model.images.Images;
import com.quadcore.Ratingup.model.profile.User;
import com.quadcore.Ratingup.repository.ImagesRepository;
import com.quadcore.Ratingup.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AvatarService {
    private final ImagesRepository imagesRepository;
    private final UserRepository userRepository;

    public AvatarService(ImagesRepository imagesRepository, UserRepository userRepository){
        this.imagesRepository = imagesRepository;
        this.userRepository = userRepository;
    }

    public List<AvatarResponseDTO> listAvaliableImages(){
        return imagesRepository.findAll()
                .stream()
                .map(AvatarResponseDTO::new)
                .toList();
    }

    public void selectAvatar(String imageName){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Images image = imagesRepository.findByImageName(imageName)
                .orElseThrow(() -> new RuntimeException("Imagem não encontrada"));

        user.setAvatarurl(imageName);
        userRepository.save(user);
    }

    //Service para gerenciar a lógica das rotas do avatar (Mostrar lista de imagens e escolher uma das imagens como avatar)
}
