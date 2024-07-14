package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Servizio;
import org.springframework.data.repository.CrudRepository;

public interface ServizioRepository extends CrudRepository<Servizio, Integer> {
    public Servizio findByName(String name);

    //public List<Servizio> getAllBy(String description);
}
