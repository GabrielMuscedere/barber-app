package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Servizio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository
public interface ServizioRepository extends CrudRepository<Servizio, Long> {

    boolean existsByNome(String nome);
}
