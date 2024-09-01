package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.GiornoLavorativo;
import it.uniroma3.siw.repository.GiornoLavorativoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalTime;

@Component
public class GiornoLavorativoValidator implements Validator {

    @Autowired
    private GiornoLavorativoRepository giornoLavorativoRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return GiornoLavorativo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        GiornoLavorativo giornoLavorativo = (GiornoLavorativo) target;

        // Controllo se esiste già un turno per questo barbiere nello stesso giorno
        boolean esisteGia = giornoLavorativoRepository.existsByBarbiereAndData(giornoLavorativo.getBarbiere(), giornoLavorativo.getData());

        if (esisteGia) {
            errors.rejectValue("data", "giornoLavorativo.data.duplicato", "Il barbiere ha già un turno assegnato in questa data.");
        }

        // Controllo se l'orario di fine è successivo all'orario di inizio
        LocalTime inizioTurno = giornoLavorativo.getInizioTurno();
        LocalTime fineTurno = giornoLavorativo.getFineTurno();

        if (fineTurno.isBefore(inizioTurno)) {
            errors.rejectValue("fineTurno", "giornoLavorativo.fineTurno.invalido", "L'orario di fine turno deve essere successivo all'orario di inizio turno.");
        }

        // Controllo se la differenza tra inizio e fine turno è al massimo di 8 ore
        long durataOre = java.time.Duration.between(inizioTurno, fineTurno).toHours();
        if (durataOre > 8) {
            errors.rejectValue("fineTurno", "giornoLavorativo.fineTurno.durataEccessiva", "La durata del turno non può essere superiore a 8 ore.");
        }
    }
}
