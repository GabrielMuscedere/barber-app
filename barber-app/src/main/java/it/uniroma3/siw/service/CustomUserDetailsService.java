package it.uniroma3.siw.service;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.repository.CredentialsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final CredentialsRepository credentialsRepository;

    @Autowired
    public CustomUserDetailsService(CredentialsRepository credentialsRepository) {
        this.credentialsRepository = credentialsRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Credentials credentials = credentialsRepository.findByUsername(username);
        if (credentials == null) {
            throw new UsernameNotFoundException("Username not found");
        }
        GrantedAuthority authority = new SimpleGrantedAuthority(credentials.getRole());
        return new org.springframework.security.core.userdetails.User(credentials.getUsername(), credentials.getPassword(), Collections.singletonList(authority));
    }
}
