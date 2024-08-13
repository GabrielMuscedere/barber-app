package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.repository.BarbiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BarbiereService {

    @Autowired
    private BarbiereRepository barbiereRepository;

    @Transactional
    public Barbiere save(Barbiere barbiere) {
        barbiere.setNome(barbiere.getNome());
        barbiere.setCognome(barbiere.getCognome());
        barbiere.setDataNascita(barbiere.getDataNascita());

        return barbiereRepository.save(barbiere);
    }

    public Barbiere findById(Long id) {
        return barbiereRepository.findById(id).get();
    }
}
