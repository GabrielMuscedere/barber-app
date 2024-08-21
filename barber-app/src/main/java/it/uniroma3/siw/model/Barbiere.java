package it.uniroma3.siw.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Entity
public class Barbiere {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 1, message = "Nome non può essere vuoto")
    private String nome;

    @NotNull
    @Size(min = 1, message = "Cognome non può essere vuoto")
    private String cognome;

    @NotNull
    private Date dataNascita;

    private String imageUrl;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<GiornoLavorativo> giorniLavorativi;

    public Barbiere() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<GiornoLavorativo> getGiorniLavorativi() {
        return giorniLavorativi;
    }

    public void setGiorniLavorativi(List<GiornoLavorativo> giorniLavorativi) {
        this.giorniLavorativi = giorniLavorativi;
    }
}
