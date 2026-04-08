package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Playlist implements Serializable {
    private String nom;
    private List<Morceau> morceaux;

    public Playlist(String nom) {
        this.nom = nom;
        this.morceaux = new ArrayList<>();
    }

    public void ajouterMorceau(Morceau morceau) { morceaux.add(morceau); }
    public void supprimerMorceau(Morceau morceau) { morceaux.remove(morceau); }

    public int getDureeTotale() {
        return morceaux.stream().mapToInt(Morceau::getDureeSecondes).sum();
    }

    public String getNom() { return nom; }
    public List<Morceau> getMorceaux() { return morceaux; }
    public void setNom(String nom) { this.nom = nom; }

    @Override
    public String toString() { return nom + " (" + morceaux.size() + " morceaux)"; }
}