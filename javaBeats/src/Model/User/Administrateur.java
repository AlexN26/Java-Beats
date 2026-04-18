package Model.User;

import Model.Morceau;
import Model.Playlist;

import java.util.ArrayList;
import java.util.List;

public class Administrateur extends Utilisateur {
    private List<Playlist> playlists;

    public Administrateur(String login, String motDePasse, String email) {
        super(login, motDePasse, email);
        this.playlists = new ArrayList<>();
    }

    public void ajouterPlaylist(Playlist playlist) { playlists.add(playlist); }
    public void supprimerPlaylist(Playlist playlist) { playlists.remove(playlist); }
    public List<Playlist> getPlaylists() { return playlists; }

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
