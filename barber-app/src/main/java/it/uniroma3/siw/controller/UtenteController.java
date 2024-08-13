package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UtenteController {

    @Autowired
    private UtenteService utenteService;

    /*
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("utente", new Utente());
        return "register";
    }

    @PostMapping("/register")
    public String newUtente(@ModelAttribute("utente") Utente utente) {
        this.utenteService.saveUtente(utente);
        return "redirect:profile/" + utente.getId();
    }
    */

}
