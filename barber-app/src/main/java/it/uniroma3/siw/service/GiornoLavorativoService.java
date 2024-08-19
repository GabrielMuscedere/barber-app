package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.GiornoLavorativo;
import it.uniroma3.siw.repository.GiornoLavorativoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GiornoLavorativoService {

    @Autowired
    private GiornoLavorativoRepository giornoLavorativoRepository;

    @Transactional
    public GiornoLavorativo save(GiornoLavorativo giornoLavorativo) {
        return giornoLavorativoRepository.save(giornoLavorativo);
    }

    public GiornoLavorativo findById(Long id) {
        return giornoLavorativoRepository.findById(id).get();
    }
}
