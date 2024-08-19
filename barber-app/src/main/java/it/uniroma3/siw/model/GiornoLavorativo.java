package it.uniroma3.siw.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class GiornoLavorativo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @NotNull
    private LocalDate data;

    @NotNull
    private LocalTime inizioTurno;

    @NotNull
    private LocalTime fineTurno;

    @Transient
    private List<LocalTime> orariDisponibili;

    @Transient
    private List<LocalTime> orariOccupati;

    @ManyToOne
    private Barbiere barbiere;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private List<ServizioPrenotato> prestazioni;

    public GiornoLavorativo() {
        this.orariDisponibili = new ArrayList<>();
        this.orariOccupati = new ArrayList<>();
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public LocalTime getFineTurno() {
        return fineTurno;
    }

    public void setFineTurno(LocalTime fineTurno) {
        this.fineTurno = fineTurno;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public LocalTime getInizioTurno() {
        return inizioTurno;
    }

    public void setInizioTurno(LocalTime inizioTurno) {
        this.inizioTurno = inizioTurno;
    }

    public List<LocalTime> getOrariDisponibili() {
        return orariDisponibili;
    }

    public void setOrariDisponibili(List<LocalTime> orariDisponibili) {
        this.orariDisponibili = orariDisponibili;
    }

    public List<LocalTime> getOrariOccupati() {
        return orariOccupati;
    }

    public void setOrariOccupati(List<LocalTime> orariOccupati) {
        this.orariOccupati = orariOccupati;
    }

    public Barbiere getBarbiere() {
        return barbiere;
    }

    public void setBarbiere(Barbiere barbiere) {
        this.barbiere = barbiere;
    }

    public List<ServizioPrenotato> getPrestazioni() {
        return prestazioni;
    }

    public void setPrestazioni(List<ServizioPrenotato> prestazioni) {
        this.prestazioni = prestazioni;
    }
}
