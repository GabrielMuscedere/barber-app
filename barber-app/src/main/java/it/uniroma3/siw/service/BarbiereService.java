package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.Servizio;
import it.uniroma3.siw.repository.BarbiereRepository;
import it.uniroma3.siw.repository.ServizioPrenotatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
public class BarbiereService {

    @Autowired
    private ServizioPrenotatoRepository servizioPrenotatoRepository;

    @Autowired
    private BarbiereRepository barbiereRepository;

    @Transactional
    public Barbiere save(Barbiere barbiere) {
        return barbiereRepository.save(barbiere);
    }

    public Barbiere findById(Long id) {
        return barbiereRepository.findById(id).get();
    }

    public void delete(Barbiere barbiere) {
        barbiereRepository.delete(barbiere);
    }

    public Iterable<Barbiere> findAll() {
        return barbiereRepository.findAll();
    }

    /*
    public boolean isBarbiereAvailable(Long idBarbiere, LocalDate data, LocalTime orarioInizio, LocalTime orarioFine) {
        // Logica per verificare se il barbiere è disponibile
        return !servizioPrenotatoRepository.existsByBarbiereIdAndDataAndOrarioInizioLessThanAndOrarioFineGreaterThan(
                idBarbiere, data, orarioFine, orarioInizio
        );
    }

     */
}
