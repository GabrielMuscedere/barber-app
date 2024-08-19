package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.model.GiornoLavorativo;
import it.uniroma3.siw.service.BarbiereService;
import it.uniroma3.siw.service.GiornoLavorativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    @PostMapping("/admin/aggiungiGiorno/{idBarber}")
    public String aggiungiGiorno(@PathVariable("idBarber") Long idBarber,
                                 @ModelAttribute("giornoLavorativo") GiornoLavorativo giornoLavorativo,
                                 Model model,
                                 @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {

        Barbiere barbiere = barbiereService.findById(idBarber);
        LocalTime orario = giornoLavorativo.getInizioTurno();

        int maxIterations = 16; // 8 ore con intervalli di 30 minuti
        int currentIterations = 0;

        if (giornoLavorativo.getInizioTurno().isBefore(giornoLavorativo.getFineTurno())) {
            System.out.println("sono nell'if");
            while (orario.isBefore(giornoLavorativo.getFineTurno()) && currentIterations < maxIterations) {
                System.out.println("sono nel while");
                System.out.println(giornoLavorativo.getOrariDisponibili());
                giornoLavorativo.getOrariDisponibili().add(orario);
                orario = orario.plusMinutes(30);
                currentIterations++;
            }
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "l'orario di inizo turno e fine turno non sono congruenti");
            return "redirect:/admin/giorniLavorativi/{idBarber}";
        }

        giornoLavorativo.setBarbiere(barbiere);
        giornoLavorativoService.save(giornoLavorativo);

        barbiere.getGiorniLavorativi().add(giornoLavorativo);
        barbiereService.save(barbiere);

        return "redirect:/admin/giorniLavorativi/{idBarber}";
    }

    @GetMapping("/admin/visualizzaPrenotazioniBarbiere/{idGiorno}")
    public String visualizzaPrenotazioni(@PathVariable Long idGiorno,
                                         @AuthenticationPrincipal CustomUserDetails userDetails,
                                         RedirectAttributes redirectAttributes,
                                         Model model){

        model.addAttribute("giorno", giornoLavorativoService.findById(idGiorno));
        return "admin/visualizzaPrenotazioniBarbiere";
    }


}
