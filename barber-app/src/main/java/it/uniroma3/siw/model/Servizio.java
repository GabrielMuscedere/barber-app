package it.uniroma3.siw.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "servizi")
public abstract class Servizio {

    @Id
    private String nome;

    private double prezzo;
    private String descrizione;
    private int duarta;

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

    public int getDuarta() {
        return duarta;
    }

    public void setDuarta(int duarta) {
        this.duarta = duarta;
    }

}

