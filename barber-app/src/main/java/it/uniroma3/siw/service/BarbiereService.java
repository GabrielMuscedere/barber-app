package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.repository.BarbiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BarbiereService {

    @Autowired
    private BarbiereRepository barbiereRepository;

    public Barbiere findById(String id) {
        return barbiereRepository.findById(id).get();
    }
}
