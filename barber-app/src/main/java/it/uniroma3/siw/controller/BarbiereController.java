package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.model.GiornoLavorativo;
import it.uniroma3.siw.repository.BarbiereRepository;
import it.uniroma3.siw.service.BarbiereService;
import it.uniroma3.siw.validator.BarbiereValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@Controller
public class BarbiereController {

    @Autowired
    private BarbiereService barbiereService;

    @Autowired
    private BarbiereValidator barbiereValidator;

    @Autowired
    private BarbiereRepository barbiereRepository;

    @Value("${file.uploadBarbiere-dir}")
    private String uploadDir;

    @RequestMapping(value = { "/admin/manageBarbers" }, method = RequestMethod.GET)
    public String showRegistrationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("authentication", customUserDetails);
        Iterable<Barbiere> barbers = barbiereRepository.findAll();
        model.addAttribute("barbers", barbers);
        model.addAttribute("barbiere", new Barbiere());
        return "/admin/manageBarbers";
    }

    @RequestMapping(value = { "/admin/manageBarbers" }, method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("barbiere") Barbiere barbiere,
                               BindingResult bindingResult,
                               Model model,
                               @RequestParam("file") MultipartFile file) {

        this.barbiereValidator.validate(barbiere, bindingResult);

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
                    barbiere.setImageUrl(fileUrl);
                }

                this.barbiereService.save(barbiere);
                return "redirect:/admin/manageBarbers";

            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("file", "upload.failed", "Failed to upload file: " + e.getMessage());
            }

        }
        else {
            bindingResult.rejectValue("nome", "pizza.duplicate");
        }

	    return "redirect:/admin/manageBarbers";

    }

    @GetMapping("/admin/barber/{id}")
    public String showBarbiere(@PathVariable Long id, Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("barber", this.barbiereRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid barber Id:" + id)));
        //model.addAttribute("updatedBarber", new Barbiere());
        model.addAttribute("authentication", customUserDetails);
        return "/admin/barber";
    }

    @PostMapping("/admin/editBarber/{id}")
    public String editBarberSubmit(@PathVariable("id") Long id,
                                   @ModelAttribute("barber") Barbiere updatedBarbiere,
                                   BindingResult bindingResult,
                                   RedirectAttributes redirectAttributes,
                                   @RequestParam MultipartFile file) {

        Barbiere existingBarbiere = barbiereRepository.findById(id).get();

        existingBarbiere.setNome(updatedBarbiere.getNome());
        existingBarbiere.setCognome(updatedBarbiere.getCognome());
        existingBarbiere.setDataNascita(updatedBarbiere.getDataNascita());

        barbiereValidator.validate(existingBarbiere, bindingResult);

        if (true) { //vedere perche con !bindingResult.hasError() da errore
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
                    existingBarbiere.setImageUrl(fileUrl);
                }

                this.barbiereService.save(existingBarbiere);
                redirectAttributes.addFlashAttribute("successMessage", "Barbiere e immagine aggiornati con successo.");
                return "redirect:/admin/manageBarbers";

            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("file", "upload.failed", "Failed to upload file: " + e.getMessage());
            }
        }

        this.barbiereService.save(existingBarbiere);
        redirectAttributes.addFlashAttribute("successMessage", "Barbiere aggiornato con successo.");
        return "redirect:/admin/profile";
        //this.barbiereRepository.save(existingBarbiere);

    }

    @PostMapping("/admin/deleteBarber/{id}")
    public String deleteBarber(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Barbiere barbiere = barbiereService.findById(id);
        if (barbiere != null) {
            barbiereService.delete(barbiere);
            redirectAttributes.addFlashAttribute("successMessage", "Barbiere:  " + barbiere.getNome() + " " + barbiere.getCognome() + " eliminata con successo.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Barbiere non trovato.");
        }
        return "redirect:/admin/manageBarbers";
    }

    @GetMapping("/admin/selezionaBarbiere")
    public String selezionaBarbiere(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("barbers", this.barbiereService.findAll());
        model.addAttribute("authentication", customUserDetails);
        return "/admin/selezionaBarbiere";
    }

    @GetMapping("/admin/giorniLavorativi/{id}")
    public String giorniLavorativi(@PathVariable("id") Long id,@AuthenticationPrincipal CustomUserDetails userDetails ,Model model) {
        model.addAttribute("barber", this.barbiereService.findById(id));
        model.addAttribute("authentication", userDetails);
        model.addAttribute("giornoLavorativo", new GiornoLavorativo());

        return "/admin/giorniLavorativi";
    }

}
