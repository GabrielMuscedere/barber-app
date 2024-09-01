package it.uniroma3.siw.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class ServizioPrenotato {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private LocalTime orarioInizio;

    private LocalTime orarioFine;

    @ManyToOne
    private Servizio tipoServizio;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private GiornoLavorativo giornoLavorativo;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Utente utente;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public GiornoLavorativo getGiornoLavorativo() {
        return giornoLavorativo;
    }

    public void setGiornoLavorativo(GiornoLavorativo giornoLavorativo) {
        this.giornoLavorativo = giornoLavorativo;
    }

    public LocalTime getOrarioFine() {
        return orarioFine;
    }

    public void setOrarioFine(LocalTime orarioFine) {
        this.orarioFine = orarioFine;
    }

    public LocalTime getOrarioInizio() {
        return orarioInizio;
    }

    public void setOrarioInizio(LocalTime orarioInizio) {
        this.orarioInizio = orarioInizio;
    }

    public Servizio getTipoServizio() {
        return tipoServizio;
    }

    public void setTipoServizio(Servizio tipoServizio) {
        this.tipoServizio = tipoServizio;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
}
