package it.uniroma3.siw.service;

import it.uniroma3.siw.model.ServizioPrenotato;
import it.uniroma3.siw.repository.ServizioPrenotatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServizioPrenotatoService {

    @Autowired private ServizioPrenotatoRepository servizioPrenotatoRepository;

    public ServizioPrenotato save(ServizioPrenotato servizioPrenotato) {
        return servizioPrenotatoRepository.save(servizioPrenotato);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(ServizioPrenotato servizioPrenotato) {
        servizioPrenotatoRepository.delete(servizioPrenotato);
    }

    public ServizioPrenotato findById(Long id) {
        return servizioPrenotatoRepository.findById(id).get();
    }
}
