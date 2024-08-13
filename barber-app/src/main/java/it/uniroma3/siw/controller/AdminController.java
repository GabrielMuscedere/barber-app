package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Barbiere;
import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.repository.BarbiereRepository;
import jdk.jfr.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private BarbiereRepository barbiereRepository;

    @GetMapping("/admin/indexAdmin")
    public String indexAdmin(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("authentication", customUserDetails);
        return "admin/indexAdmin";
    }

    @GetMapping("/admin/profile")
    public String adminProfile(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("authentication", customUserDetails);
        return "admin/profile";
    }

    /*
    @GetMapping("/admin/addBarber")
    public String addBarber(Model model, @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        model.addAttribute("authentication", customUserDetails);
        Iterable<Barbiere> barbers = barbiereRepository.findAll();
        model.addAttribute("barbers", barbers);
        return "admin/addBarber";
    }
     */

}
