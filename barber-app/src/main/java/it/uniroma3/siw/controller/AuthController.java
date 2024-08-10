package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @Autowired
    private CredentialsService credentialsService;

    @Autowired
    private UtenteService utenteService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("utente", new Utente());
        model.addAttribute("credentials", new Credentials());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("utente") Utente utente,
                               @ModelAttribute("credentials") Credentials credentials,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/register";
        }
        utenteService.save(utente);
        credentials.setUtente(utente);
        credentialsService.save(credentials);
        return "redirect:/login";
    }
}
