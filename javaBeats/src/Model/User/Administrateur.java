package Model.User;

import Model.Morceau;

import java.util.List;

public class Administrateur extends Utilisateur {
    public Administrateur(String login, String motDePasse, String email) {
        super(login, motDePasse, email);
    }

    public void ajouterMusique(Morceau morceau) {
        throw new UnsupportedOperationException("Fonctionnalite non implementee.");
    }

    public void supprimerMusique(Morceau morceau) {
        throw new UnsupportedOperationException("Fonctionnalite non implementee.");
    }

    public void ajouterInscrit(Utilisateur utilisateur) {
        throw new UnsupportedOperationException("Fonctionnalite non implementee.");
    }

    public void supprimerInscrit(Utilisateur utilisateur) {
        throw new UnsupportedOperationException("Fonctionnalite non implementee.");
    }

    public List<Utilisateur> consulterListeUtilisateurs() {
        throw new UnsupportedOperationException("Fonctionnalite non implementee.");
    }

    public List<Morceau> consulterListeMusiques() {
        throw new UnsupportedOperationException("Fonctionnalite non implementee.");
    }
}
