package it.uniroma3.siw.controller;

import it.uniroma3.siw.repository.ServizioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ServizioController {

    @Autowired
    private ServizioRepository servizioRepository;

}
