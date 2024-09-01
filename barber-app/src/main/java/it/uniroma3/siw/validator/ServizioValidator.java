package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Servizio;
import it.uniroma3.siw.repository.ServizioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ServizioValidator implements Validator {


    @Autowired
    private ServizioRepository servizioRepository; // Assuming you have a repository to interact with the database

    @Override
    public boolean supports(Class<?> clazz) {
        return Servizio.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Servizio servizio = (Servizio) target;

        // Check if a service with the same name already exists
        if (servizioRepository.existsByNome(servizio.getNome())) {
            errors.rejectValue("nome", "servizio.nome.duplicato", "Un servizio con questo nome esiste già.");
        }
    }
}

