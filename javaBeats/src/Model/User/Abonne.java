package Model.User;

import Model.Historique;
import Model.Playlist;
import java.util.ArrayList;
import java.util.List;

public class Abonne extends Utilisateur {
    private List<Playlist> playlists;
    private Historique historique;
    private boolean abonnementActif;

    public Abonne(String login, String motDePasse, String email) {
        super(login, motDePasse, email);
        this.playlists = new ArrayList<>();
        this.historique = new Historique();
        this.abonnementActif = true;
    }

    public void ajouterPlaylist(Playlist playlist) { playlists.add(playlist); }
    public void supprimerPlaylist(Playlist playlist) { playlists.remove(playlist); }

    public List<Playlist> getPlaylists() { return playlists; }
    public Historique getHistorique() { return historique; }
    public boolean isAbonnementActif() { return abonnementActif; }
    public void setAbonnementActif(boolean actif) { this.abonnementActif = actif; }
}