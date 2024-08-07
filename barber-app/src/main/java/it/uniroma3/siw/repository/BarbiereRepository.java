package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Barbiere;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BarbiereRepository extends CrudRepository<Barbiere, String> {

}
