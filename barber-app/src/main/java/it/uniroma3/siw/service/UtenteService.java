package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    public Utente save(Utente utente) {
        return utenteRepository.save(utente);
    }

    public Utente findById(Long id) {
        Optional<Utente> utente = utenteRepository.findById(id);
        return utente.orElse(null); // Oppure gestisci il caso in cui l'utente non è trovato
    }
}
