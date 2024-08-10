package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente findById(Long id) {
        return utenteRepository.findById(id).get();
    }

    public Utente save(Utente utente) {
        return utenteRepository.save(utente);
    }


}
