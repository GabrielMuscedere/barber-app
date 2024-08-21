package it.uniroma3.siw.controller;


import it.uniroma3.siw.model.*;
import it.uniroma3.siw.service.*;
import it.uniroma3.siw.validator.ServizioPrenotatoValidator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Transient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class ServizioController {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ServizioService servizioService;

    @Autowired
    private BarbiereService barbiereService;

    @Autowired
    private ServizioPrenotatoService servizioPrenotatoService;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ServizioPrenotatoValidator servizioPrenotatoValidator;

    @Autowired
    private GiornoLavorativoService giornoLavorativoService;


    @GetMapping("/user/selectedBarber/{idBarbiere}/selezionaServizio")
    public String selezionaServizio(@PathVariable Long idBarbiere, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("barbiere", this.barbiereService.findById(idBarbiere));
        model.addAttribute("servizi", this.servizioService.findAll());
        model.addAttribute("authentication", userDetails);
        return "user/selezionaServizio";
    }

    @GetMapping("/user/selectedBarber/{idBarbiere}/selectedService/{nomeServizio}")
        public String selezionaGiorno(@PathVariable Long idBarbiere,
                                      @PathVariable String nomeServizio,
                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                      Model model) {
            Barbiere barbiere = this.barbiereService.findById(idBarbiere);

            LocalDate today = LocalDate.now();

            // Filtra i giorni che sono dopo oggi
            List<GiornoLavorativo> giorniDopoOggi = barbiere.getGiorniLavorativi().stream()
                .filter(g -> g.getData().isAfter(today))
                .collect(Collectors.toList());

            model.addAttribute("giorniDopoOggi", giorniDopoOggi);
            model.addAttribute("barbiere", barbiere);
            model.addAttribute("servizio", this.servizioService.findById(nomeServizio));
            model.addAttribute("authentication", userDetails);
            model.addAttribute("servizioPrenotato", new ServizioPrenotato());

            return "user/selezionaData";
    }

    @GetMapping("/user/{idGiorno}/{nomeServizio}")
    public String selezionaOrario(@PathVariable Long idGiorno,
                                  @PathVariable String nomeServizio,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes){

        GiornoLavorativo giorno = giornoLavorativoService.findById(idGiorno);

        Map<LocalTime, LocalDate> orari = new TreeMap<>();

        LocalTime orario = giorno.getInizioTurno();

        int currentIterations = 0;
        int maxIterations = 15;

        if (giorno.getInizioTurno().isBefore(giorno.getFineTurno())) {
            while (orario.isBefore(giorno.getFineTurno().minusMinutes(30)) && currentIterations < maxIterations) {
                orari.put(orario, giorno.getData());
                orario = orario.plusMinutes(30);
                currentIterations++;
            }
        }

        if(!giorno.getPrestazioni().isEmpty()){
            for(ServizioPrenotato prestazione : giorno.getPrestazioni()){
                orari.remove(prestazione.getOrarioInizio());
            }
        }

        model.addAttribute("orariDisponibili", orari);
        model.addAttribute("giorno", giorno);
        model.addAttribute("servizio", nomeServizio);
        model.addAttribute("authentication", userDetails);

        return "user/selezionaOrario";
    }

    @GetMapping("/user/prenotaServizio/{idGiorno}/{orario}/{servizio}")
    public String prenotaServizio(@PathVariable Long idGiorno,
                                  @PathVariable String orario,
                                  @PathVariable String servizio,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes){

        GiornoLavorativo giornoLavorativo = giornoLavorativoService.findById(idGiorno);
        LocalTime orarioInizio = LocalTime.parse(orario);

        ServizioPrenotato servizioPrenotato = new ServizioPrenotato();

        servizioPrenotato.setOrarioInizio(orarioInizio);
        servizioPrenotato.setOrarioFine(orarioInizio.plusMinutes(servizioService.findById(servizio).getDurata()));
        servizioPrenotato.setTipoServizio(servizio);
        servizioPrenotato.setGiornoLavorativo(giornoLavorativoService.findById(idGiorno));
        servizioPrenotato.setUtente(userDetails.getUtente());

        servizioPrenotatoService.save(servizioPrenotato);

        giornoLavorativo.getPrestazioni().add(servizioPrenotato);
        giornoLavorativoService.save(giornoLavorativo);

        userDetails.getUtente().getPrenotazioni().add(servizioPrenotato);
        utenteService.save(userDetails.getUtente());

        return "redirect:/profile";
    }

    @PostMapping("/user/delete/{id}")
    public String deletePrenotazione(@PathVariable Long id,
                                     RedirectAttributes redirectAttributes) {

        ServizioPrenotato servizioPrenotato = servizioPrenotatoService.findById(id);
        Long idGiorno = servizioPrenotato.getGiornoLavorativo().getId();
        String nomeServizio = servizioPrenotato.getTipoServizio();

        if (servizioPrenotato != null) {
            GiornoLavorativo giornoLavorativo = servizioPrenotato.getGiornoLavorativo();
            Utente utente = servizioPrenotato.getUtente();

            if (giornoLavorativo != null) {
                giornoLavorativo.getPrestazioni().remove(servizioPrenotato); // Rimuove l'associazione
                giornoLavorativoService.save(giornoLavorativo);
            }

            if (utente != null) {
                utente.getPrenotazioni().remove(servizioPrenotato);
                utenteService.save(utente);
            }

            servizioPrenotatoService.delete(servizioPrenotato); // Elimina il servizio prenotato
            redirectAttributes.addFlashAttribute("successMessage", "Prenotazione eliminata con successo.");

            //return "redirect:/user/idGiorno /{nomeServizio}";
            return "redirect:/";
            //session.invalidate();

        } else {
            redirectAttributes.addFlashAttribute("errors", "Prenotazione non trovata.");
            return "redirect:/profile";
        }

    }


}
