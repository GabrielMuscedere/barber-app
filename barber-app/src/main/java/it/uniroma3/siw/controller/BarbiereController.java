package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.model.Utente;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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

    @Value("${file.upload-dir}")
    private String uploadDir;

    @RequestMapping(value = { "/admin/addBarber" }, method = RequestMethod.GET)
    public String showRegistrationForm(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("authentication", customUserDetails);
        Iterable<Barbiere> barbers = barbiereRepository.findAll();
        model.addAttribute("barbers", barbers);
        model.addAttribute("barbiere", new Barbiere());
        return "/admin/addBarber";
    }

    @RequestMapping(value = { "/admin/addBarber" }, method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("barbiere") Barbiere barbiere, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile file) {

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
                return "redirect:/admin/addBarber";

            } catch (IOException e) {
                e.printStackTrace();
                bindingResult.rejectValue("file", "upload.failed", "Failed to upload file: " + e.getMessage());
            }

        }
        else {
            bindingResult.rejectValue("nome", "pizza.duplicate");
        }

	    return "redirect:/admin/addBarber";

    }


}
