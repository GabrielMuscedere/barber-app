package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutoController {

    @GetMapping("/about")
    public String about(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("authentication", userDetails);
        return "about";
    }

    @GetMapping("/contact")
    public String contact(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("authentication", userDetails);
        return "contact";
    }



}
