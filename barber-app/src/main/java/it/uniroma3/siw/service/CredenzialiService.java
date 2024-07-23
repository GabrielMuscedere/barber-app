package it.uniroma3.siw.service;

import it.uniroma3.siw.repository.CredenzialiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredenzialiService {

    @Autowired
    private CredenzialiRepository credenzialiRepository;

}
