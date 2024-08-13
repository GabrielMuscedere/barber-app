package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.BarbiereRepository;
import it.uniroma3.siw.service.BarbiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class BarbiereController {

    @Autowired
    private BarbiereService barbiereService;

    @Autowired
    private BarbiereRepository barbiereRepository;

    @RequestMapping(value = { "/admin/addBarber" }, method = RequestMethod.GET)
    public String showRegistrationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("authentication", customUserDetails);
        Iterable<Barbiere> barbers = barbiereRepository.findAll();
        model.addAttribute("barbers", barbers);
        model.addAttribute("barbiere", new Barbiere());
        return "/admin/addBarber";
    }

    @RequestMapping(value = { "/admin/addBarber" }, method = RequestMethod.POST)
    public String registerUser(@ModelAttribute("barbiere") Barbiere barbiere, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/addBarber";
        }
        barbiereService.save(barbiere);
        return "redirect:/admin/addBarber";
    }
}
