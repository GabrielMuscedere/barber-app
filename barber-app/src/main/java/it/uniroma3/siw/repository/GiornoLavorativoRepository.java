package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.GiornoLavorativo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface GiornoLavorativoRepository extends CrudRepository<GiornoLavorativo, Long> {

    boolean existsByBarbiereAndData(Barbiere barbiere, LocalDate data);
}
