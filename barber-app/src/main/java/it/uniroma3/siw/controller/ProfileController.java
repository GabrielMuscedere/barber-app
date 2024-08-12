package it.uniroma3.siw.controller;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.service.ServizioService;
import it.uniroma3.siw.service.UtenteService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ProfileController {

    @Autowired private CredentialsRepository credentialsRepository;
    @Autowired private ServizioService servizioService;

    @GetMapping("/redirectByRole")
    public String redirectByRole(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        if (customUserDetails != null ) {
            Credentials credentials = customUserDetails.getCredentials();
            String role = credentials.getRole();

            if ("ADMIN".equals(role)) {
                return "/admin/indexAdmin";
            } else {
                return "redirect:/user/profile";
            }
        }

        // Se l'utente non è autenticato, reindirizza alla pagina di login
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            if (userDetails.getCredentials().getRole().equals("ROLE_DEFAULT")) {
                model.addAttribute("authentication", userDetails);
                return "/user/profile";
            } else if (userDetails.getCredentials().getRole().equals("ROLE_ADMIN")) {
                model.addAttribute("barbiere", userDetails);
                return "/admin/indexAdmin";
            }
        }
        // Se l'utente non è autenticato, reindirizza alla pagina di login
        return "redirect:/login";
    }

    @GetMapping("/")
    public String getIndex(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {
        model.addAttribute("authentication", userDetails);
        model.addAttribute("servizi", this.servizioService.findAll());
        return "index"; // Il nome del template Thymeleaf
    }


    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }


}
