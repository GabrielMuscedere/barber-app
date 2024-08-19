package it.uniroma3.siw.repository;

import it.uniroma3.siw.model.ServizioPrenotato;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;

@Repository
public interface ServizioPrenotatoRepository extends CrudRepository<ServizioPrenotato, Long> {
   // boolean existsByBarbiereIdAndDataAndOrarioInizioLessThanAndOrarioFineGreaterThan(Long idBarbiere, LocalDate data, LocalTime orarioFine, LocalTime orarioInizio);
}
