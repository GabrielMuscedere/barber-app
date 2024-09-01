package it.uniroma3.siw.controller;


import it.uniroma3.siw.model.*;
import it.uniroma3.siw.service.*;
import it.uniroma3.siw.validator.ServizioPrenotatoValidator;
import it.uniroma3.siw.validator.ServizioValidator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Transient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    @Value("${file.uploadServizio-dir}")
    private String uploadDir;


    @GetMapping("/user/selectedBarber/{idBarbiere}/selezionaServizio")
    public String selezionaServizio(@PathVariable Long idBarbiere, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        model.addAttribute("barbiere", this.barbiereService.findById(idBarbiere));
        model.addAttribute("servizi", this.servizioService.findAll());
        model.addAttribute("authentication", userDetails);
        return "user/selezionaServizio";
    }

    @GetMapping("/user/selectedBarber/{idBarbiere}/selectedService/{idServizio}")
        public String selezionaGiorno(@PathVariable Long idBarbiere,
                                      @PathVariable Long idServizio,
                                      @AuthenticationPrincipal CustomUserDetails userDetails,
                                      Model model) {
            Barbiere barbiere = this.barbiereService.findById(idBarbiere);
            Servizio servizio = this.servizioService.findById(idServizio);
            LocalDate today = LocalDate.now();

            // Filtra i giorni che sono dopo oggi
            List<GiornoLavorativo> giorniDopoOggi = barbiere.getGiorniLavorativi().stream()
                .filter(g -> g.getData().isAfter(today))
                .collect(Collectors.toList());

            model.addAttribute("giorniDopoOggi", giorniDopoOggi);
            model.addAttribute("barbiere", barbiere);
            model.addAttribute("servizio", servizio);
            model.addAttribute("authentication", userDetails);
            model.addAttribute("servizioPrenotato", new ServizioPrenotato());

            return "user/selezionaData";
    }

    @GetMapping("/user/{idGiorno}/{idServizio}")
    public String selezionaOrario(@PathVariable Long idGiorno,
                                  @PathVariable Long idServizio,
                                  Model model,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes){

        GiornoLavorativo giorno = giornoLavorativoService.findById(idGiorno);
        Servizio servizio = this.servizioService.findById(idServizio);

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
        model.addAttribute("servizio", servizio);
        model.addAttribute("authentication", userDetails);

        return "user/selezionaOrario";
    }

    @GetMapping("/user/prenotaServizio/{idGiorno}/{orario}/{idServizio}")
    public String prenotaServizio(@PathVariable Long idGiorno,
                                  @PathVariable String orario,
                                  @PathVariable Long idServizio,
                                  @AuthenticationPrincipal CustomUserDetails userDetails,
                                  RedirectAttributes redirectAttributes){

        Servizio servizio = servizioService.findById(idServizio);
        GiornoLavorativo giornoLavorativo = giornoLavorativoService.findById(idGiorno);
        LocalTime orarioInizio = LocalTime.parse(orario);

        ServizioPrenotato servizioPrenotato = new ServizioPrenotato();

        servizioPrenotato.setOrarioInizio(orarioInizio);
        servizioPrenotato.setOrarioFine(orarioInizio.plusMinutes(servizio.getDurata()));
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
        Servizio servizio = servizioPrenotato.getTipoServizio();

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
            return "redirect:/profile";
            //session.invalidate();

        } else {
            redirectAttributes.addFlashAttribute("errors", "Prenotazione non trovata.");
            return "redirect:/profile";
        }

    }

    @GetMapping("/admin/gestisciServizi")
    public String gestisciServizi(@AuthenticationPrincipal CustomUserDetails userDetails,
                                  Model model) {
        model.addAttribute("authentication", userDetails);
        model.addAttribute("servizi", servizioService.findAll());

        return "admin/gestisciServizi";
    }

    @GetMapping("/admin/aggiungiServizio")
    public String aggiungiServizio(@AuthenticationPrincipal CustomUserDetails userDetails,
                                   Model model) {

        model.addAttribute("authentication", userDetails);
        model.addAttribute("servizio", new Servizio());

        return "admin/aggiungiServizio";
    }

    @Autowired
    private ServizioValidator servizioValidator;

    @PostMapping("/admin/salvaServizio")
    public String salvaServizio(@ModelAttribute("servizio") Servizio servizio,
                                BindingResult bindingResult,
                                Model model,
                                @RequestParam("file") MultipartFile file){

        this.servizioValidator.validate(servizio, bindingResult);

        if (!bindingResult.hasErrors()) {
            try {
                //gestione del file
                if(!file.isEmpty()){
                    Path dirPath = Paths.get(uploadDir);
                    if(!Files.exists(dirPath)){
                        Files.createDirectories(dirPath);
                    }

                    String filename = file.getOriginalFilename();
                    Path filePath = dirPath.resolve(filename);
                    Files.write(filePath, file.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

                    // Imposta il percorso dell'immagine come URL accessibile
                    String fileUrl = filename;
                    servizio.setImageName(fileUrl);
                }
                servizio.setEliminato(Boolean.FALSE);
                this.servizioService.save(servizio);
                return "redirect:/admin/gestisciServizi";

            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("file", "upload.failed", "Failed to upload file: " + e.getMessage());
            }

        }
        else {
            bindingResult.rejectValue("nome", "servizio.duplicate");
        }

        return "redirect:/admin/gestisciServizi";


    }


}
