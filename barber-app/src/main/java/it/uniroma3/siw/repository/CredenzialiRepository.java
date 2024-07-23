package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Credential;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredenzialiRepository extends CrudRepository<Credential, Long> {

}
