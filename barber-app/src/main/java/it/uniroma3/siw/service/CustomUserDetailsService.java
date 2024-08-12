package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.CustomUserDetails;
import it.uniroma3.siw.model.Utente;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // Iniezione di dipendenza per il repository delle credenziali
    @Autowired
    private CredentialsRepository credentialsRepository;

    // Iniezione di dipendenza per il repository degli utenti
    @Autowired
    private UtenteRepository utenteRepository;

    // Sovrascrittura del metodo loadUserByUsername per caricare i dettagli dell'utente
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cerca le credenziali dell'utente nel repository utilizzando lo username
        Credentials credentials = credentialsRepository.findByUsername(username);

        // Se le credenziali non vengono trovate, lancia un'eccezione
        if (credentials == null) {
            throw new UsernameNotFoundException("User not found");
        }

        Utente utente = credentials.getUtente();
        if (utente == null) {
            throw new UsernameNotFoundException("User not found");
        }
        // Crea un'autorità (ruolo) per l'utente utilizzando direttamente il ruolo memorizzato
        GrantedAuthority authority = new SimpleGrantedAuthority(credentials.getRole());

        // Crea un oggetto CustomUserDetails che include sia le credenziali che i dettagli dell'utente
        return new CustomUserDetails(credentials, utente, Collections.singletonList(authority));
    }
}
