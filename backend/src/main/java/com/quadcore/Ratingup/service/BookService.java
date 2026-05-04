package com.quadcore.Ratingup.service;

import com.quadcore.Ratingup.dto.book.ConteudoPaginaDTO;
import com.quadcore.Ratingup.dto.book.LivroDTO;
import com.quadcore.Ratingup.dto.book.PaginaDTO;
import com.quadcore.Ratingup.model.book.Capitulo;
import com.quadcore.Ratingup.model.book.Subtopico;
import com.quadcore.Ratingup.repository.CapituloRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    private final CapituloRepository capituloRepository;

    public BookService(CapituloRepository capituloRepository) {
        this.capituloRepository = capituloRepository;
    }

    public LivroDTO buildBook() {
        List<Capitulo> capitulos = capituloRepository.findAllByOrderbyOrdemAsc();

        List<ConteudoPaginaDTO> conteudos = new ArrayList<>();

        for (Capitulo cap : capitulos) {
            conteudos.add(new ConteudoPaginaDTO("capitulo", cap.getId(), null, cap.getTitulo()));

            for (Subtopico sub : cap.getSubtopicos()) {
                conteudos.add(new ConteudoPaginaDTO("subtópico", sub.getId(), cap.getId(), sub.getTitulo()));
            }
        }

        List<PaginaDTO> paginas = new ArrayList<>();
        for (int i = 0; i < conteudos.size() - 1; i += 2) {
            ConteudoPaginaDTO verso = (i + 1 < conteudos.size()) ? conteudos.get(i+1) : null;
            paginas.add(new PaginaDTO(conteudos.get(i), verso));
        }

        return new LivroDTO(paginas, paginas.size());
    }
}
