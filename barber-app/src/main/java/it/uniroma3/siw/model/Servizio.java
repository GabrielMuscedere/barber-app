package it.uniroma3.siw.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Servizio {

    @Id
    private String nome;

    private double prezzo;
    private String descrizione;
    private int durata;

    public Servizio() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(double prezzo) {
        this.prezzo = prezzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public int getDurata() {
        return durata;
    }

    public void setDuarta(int duarta) {
        this.durata = duarta;
    }

}

