package it.uniroma3.siw.controller;

import it.uniroma3.siw.service.BarbiereService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class BarbiereController {

    @Autowired
    private BarbiereService barbiereService;
}
