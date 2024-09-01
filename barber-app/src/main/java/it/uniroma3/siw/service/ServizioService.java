package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Servizio;
import it.uniroma3.siw.repository.ServizioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServizioService {

    @Autowired
    private ServizioRepository servizioRepository;

    public Iterable<Servizio> findAll() {
        return servizioRepository.findAll();
    }

    public Servizio findById(Long id) {
        return servizioRepository.findById(id).get();
    }

    public void save(Servizio servizio) {
        servizioRepository.save(servizio);
    }
}
