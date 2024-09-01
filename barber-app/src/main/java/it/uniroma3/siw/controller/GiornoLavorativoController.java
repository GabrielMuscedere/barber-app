package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.model.GiornoLavorativo;
import it.uniroma3.siw.service.BarbiereService;
import it.uniroma3.siw.service.GiornoLavorativoService;
import it.uniroma3.siw.validator.GiornoLavorativoValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalTime;



@Controller
public class GiornoLavorativoController {

    @Autowired
    private BarbiereService barbiereService;

    @Autowired
    private GiornoLavorativoService giornoLavorativoService;

    @Autowired
    private GiornoLavorativoValidator giornoLavorativoValidator;

    @PostMapping("/admin/aggiungiGiorno/{idBarber}")
    public String aggiungiGiorno(@PathVariable("idBarber") Long idBarber,
                                 @ModelAttribute("giornoLavorativo") GiornoLavorativo giornoLavorativo,
                                 Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes,
                                 BindingResult result) {

        // Recupera il barbiere
        Barbiere barbiere = barbiereService.findById(idBarber);

        // Imposta il barbiere nel giorno lavorativo
        giornoLavorativo.setBarbiere(barbiere);

        // Valida il giorno lavorativo
        giornoLavorativoValidator.validate(giornoLavorativo, result);

        // Se ci sono errori di validazione, ritorna alla vista originale con gli errori
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Errore: " + result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/admin/giorniLavorativi/" + idBarber;
        }

        // Salva il giorno lavorativo e aggiorna il barbiere
        giornoLavorativoService.save(giornoLavorativo);
        barbiere.getGiorniLavorativi().add(giornoLavorativo);
        barbiereService.save(barbiere);

        // Reindirizza alla pagina di gestione del barbiere
        return "redirect:/admin/giorniLavorativi/" + idBarber;
    }

    @GetMapping("/admin/visualizzaPrenotazioniBarbiere/{idGiorno}")
    public String visualizzaPrenotazioni(@PathVariable Long idGiorno,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         RedirectAttributes redirectAttributes,
                                         Model model){

        model.addAttribute("giorno", giornoLavorativoService.findById(idGiorno));
        model.addAttribute("authentication", userDetails);
        return "admin/visualizzaPrenotazioniBarbiere";
    }


}
