package Model.User;

import java.io.Serializable;

public abstract class Utilisateur implements Serializable {
    protected String login;
    protected String motDePasse;
    protected String email;

    public Utilisateur(String login, String motDePasse, String email) {
        this.login = login;
        this.motDePasse = motDePasse;
        this.email = email;
    }

    public boolean verifierMotDePasse(String mdp) {
        return this.motDePasse.equals(mdp);
    }

    public String getLogin() { return login; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    @Override
    public String toString() { return login + " (" + getClass().getSimpleName() + ")"; }
}