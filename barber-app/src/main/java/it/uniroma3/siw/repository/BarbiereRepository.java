package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Barbiere;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarbiereRepository extends CrudRepository<Barbiere, Long> {

}
