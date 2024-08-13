package it.uniroma3.siw.validator;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.repository.BarbiereRepository;
import org.hibernate.annotations.Comment;
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

        if (barbiereRepository.existsByNomeAndCognome(barbiere.getNome(), barbiere.getCognome())) {
            errors.reject("barbiere.duplicate");
        }

    }
}
