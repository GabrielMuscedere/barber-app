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

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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


    /*
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("servizi", this.servizioService.findAll());
        return "index";
    }
    */

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

            model.addAttribute("barbiere", barbiere);
            model.addAttribute("servizio", this.servizioService.findById(nomeServizio));
            model.addAttribute("authentication", userDetails);
            model.addAttribute("servizioPrenotato", new ServizioPrenotato());
            //model.addAttribute("prestazioni", barbiere.getPrestazioni());

            return "user/selezionaData";
    }

    @GetMapping("/user/{idGiorno}/{nomeServizio}")
    public String selezionaOrario(@PathVariable Long idGiorno,
                                  @PathVariable String nomeServizio,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes){

        GiornoLavorativo giorno = giornoLavorativoService.findById(idGiorno);
        List<LocalTime> orari = new ArrayList<>();

        LocalTime orario = giorno.getInizioTurno();

        int currentIterations = 0;
        int maxIterations = 16;

        if (giorno.getInizioTurno().isBefore(giorno.getFineTurno())) {
            while (orario.isBefore(giorno.getFineTurno()) && currentIterations < maxIterations) {
                orari.add(orario);
                orario = orario.plusMinutes(30);
                currentIterations++;
            }
        }

        List<LocalTime> orariDisponibili = new ArrayList<>(orari);

        if(!giorno.getPrestazioni().isEmpty()){
            for(ServizioPrenotato prestazione : giorno.getPrestazioni()){
                orariDisponibili.remove(prestazione.getOrarioInizio());

            }
        }

        model.addAttribute("orariDisponibili", orariDisponibili);
        model.addAttribute("giorno", giorno);
        model.addAttribute("servizio", nomeServizio);
        model.addAttribute("authentication", userDetails);

        return "user/selezionaOrario";
    }

    /*
    @PostMapping("/user/selectedBarber/{idBarbiere}/selectedService/{nomeServizio}/prenota")
    public String prenotaServizio(@PathVariable Long idBarbiere,
                                  @PathVariable String nomeServizio,
                                  @ModelAttribute("servizioPrenotato") ServizioPrenotato servizioPrenotato,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  BindingResult bindingResult,
                                  Model model,
                                  RedirectAttributes redirectAttributes){

        Barbiere barbiere = this.barbiereService.findById(idBarbiere);
        //Servizio servizio = this.servizioService.findById(nomeServizio);
        Utente utente = userDetails.getUtente();

        servizioPrenotato.setUtente(utente);
        servizioPrenotato.setBarbiere(barbiere);
        servizioPrenotato.setTipoServizio(nomeServizio);
        LocalTime orarioFine = servizioPrenotato.getOrarioInizio().plusMinutes(servizioService.findById(nomeServizio).getDurata());
        servizioPrenotato.setOrarioFine(orarioFine);

        // Chiamata al validator manuale
        servizioPrenotatoValidator.validate(servizioPrenotato, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/user/selectedBarber/{idBarbiere}/selectedService/{nomeServizio}";
        }

        this.servizioPrenotatoService.save(servizioPrenotato);

        barbiere.getPrestazioni().add(servizioPrenotato);
        utente.getPrenotazioni().add(servizioPrenotato);

        barbiereService.save(barbiere);
        utenteService.save(utente);

        return "redirect:/profile";
    }

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private HttpSession session;

     */

    /*
    @Transactional
    @PostMapping("/user/deletePrenotazione/{id}")
    public String deletePrenotazione(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        ServizioPrenotato servizioPrenotato = servizioPrenotatoService.findById(id);
        if (servizioPrenotato != null) {
            Barbiere barbiere = servizioPrenotato.getBarbiere();
            Utente utente = servizioPrenotato.getUtente();

            if (barbiere != null) {
                barbiere.getPrestazioni().remove(servizioPrenotato); // Rimuove l'associazione
                barbiereService.save(barbiere); // Salva lo stato aggiornato di Barbiere
                entityManager.flush(); // Forza il flush al database
                entityManager.clear(); // Pulisce il contesto di persistenza
            }

            if (utente != null) {
                utente.getPrenotazioni().remove(servizioPrenotato);
                utenteService.save(utente);
                entityManager.flush(); // Forza il flush al database
                entityManager.clear(); // Pulisce il contesto di persistenza
            }

            servizioPrenotatoService.delete(servizioPrenotato); // Elimina il servizio prenotato
            //entityManager.remove(servizioPrenotato); // Rimuovi dal database
            entityManager.flush(); // Sincronizza il contesto di persistenza con il database
            redirectAttributes.addFlashAttribute("successMessage", "Prenotazione eliminata con successo.");

            entityManager.flush(); // Forza il flush al database
            entityManager.clear(); // Pulisce il contesto di persistenza

            // Debug: Stampa il contenuto della sessione
            session.getAttributeNames().asIterator().forEachRemaining(attr -> {
                System.out.println("Session Attribute: " + attr + " = " + session.getAttribute(attr));
            });


            // Pulisci manualmente la cache di Thymeleaf
            templateEngine.clearTemplateCache();

            //session.invalidate();
            // Rimuovi l'entità dalla cache di primo livello (sessione di Hibernate)
            entityManager.detach(servizioPrenotato);

        } else {
            redirectAttributes.addFlashAttribute("errors", "Prenotazione non trovata.");
            return "redirect:/profile";
        }

        return "redirect:/";
    }

     */

}
