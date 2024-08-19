package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.ServizioPrenotato;
import it.uniroma3.siw.service.BarbiereService;
import it.uniroma3.siw.service.ServizioPrenotatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ServizioPrenotatoValidator implements Validator {

    @Autowired
    private ServizioPrenotatoService servizioPrenotatoService;

    @Autowired
    private BarbiereService barbiereService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ServizioPrenotato.class.equals(clazz);
    }



    @Override
    public void validate(Object target, Errors errors) {
        /*
        ServizioPrenotato servizioPrenotato = (ServizioPrenotato) target;

        boolean disponibile = barbiereService.isBarbiereAvailable(
                servizioPrenotato.getId(),
                servizioPrenotato.getData(),
                servizioPrenotato.getOrarioInizio(),
                servizioPrenotato.getOrarioFine()
        );

        if (!disponibile) {
            errors.rejectValue("orarioInizio", "barbiere.nonDisponibile", "Il barbiere non è disponibile per l'orario selezionato.");
        }*/

    }


}
