package it.uniroma3.siw.model;

import it.uniroma3.siw.model.Utente;
import jakarta.persistence.*;
import jdk.jfr.Unsigned;

@Entity
public class Credential {
    //public static Roles DEFAULT_ROLE = Roles.REGISTRATO;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Unsigned
    private int id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", nullable = false)
    private String role;

    @OneToOne
    private Utente utente;

    public Credential() {
    }

    public Credential(String username, String password) {
        this.username = username;
        this.password = password;
        //this.role = Credential.DEFAULT_ROLE.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /*
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
