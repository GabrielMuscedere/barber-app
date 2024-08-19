package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.repository.BarbiereRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BarbiereValidator implements Validator {

    @Autowired
    private BarbiereRepository barbiereRepository;

    @Override
    public boolean supports(Class<?> aClass) {
        return Barbiere.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Barbiere barbiere = (Barbiere) o;

        // Verifica che il nome non sia nullo o vuoto
        if (barbiere.getNome() == null || barbiere.getNome().trim().isEmpty()) {
            errors.rejectValue("nome", "barbiere.nome.empty", "Il nome non può essere vuoto");
        }

        // Verifica che il cognome non sia nullo o vuoto
        if (barbiere.getCognome() == null || barbiere.getCognome().trim().isEmpty()) {
            errors.rejectValue("cognome", "barbiere.cognome.empty", "Il cognome non può essere vuoto");
        }

        // Verifica che la data di nascita non sia nulla
        if (barbiere.getDataNascita() == null) {
            errors.rejectValue("dataNascita", "barbiere.dataNascita.empty", "La data di nascita non può essere vuota");
        }

        // Verifica che non esista già un barbiere con lo stesso nome e cognome
        if (barbiereRepository.existsByNomeAndCognome(barbiere.getNome(), barbiere.getCognome())) {
            errors.reject("barbiere.duplicate", "Esiste già un barbiere con questo nome e cognome");
        }
    }
}
