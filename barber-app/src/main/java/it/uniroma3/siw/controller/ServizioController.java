package it.uniroma3.siw.controller;


import it.uniroma3.siw.model.Servizio;
import it.uniroma3.siw.service.ServizioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ServizioController {

    @Autowired
    private ServizioService servizioService;

    /*
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("servizi", this.servizioService.findAll());
        return "index";
    }
    */
}
