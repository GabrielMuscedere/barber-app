package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Utente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UtenteRepository extends CrudRepository<Utente, Long> {

}
