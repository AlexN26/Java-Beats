package Model;

import java.io.Serializable;

public class Morceau implements Serializable {
    private String titre;
    private int dureeSecondes;
    private int nbEcoutes;
    private Artiste artiste;

    public Morceau(String titre, int dureeSecondes, Artiste artiste) {
        this.titre = titre;
        this.dureeSecondes = dureeSecondes;
        this.artiste = artiste;
        this.nbEcoutes = 0;
    }

    public void incrementerEcoutes() { this.nbEcoutes++; }

    public String getTitre() { return titre; }
    public int getDureeSecondes() { return dureeSecondes; }
    public int getNbEcoutes() { return nbEcoutes; }
    public Artiste getArtiste() { return artiste; }

    public void setTitre(String titre) { this.titre = titre; }
    public void setDureeSecondes(int dureeSecondes) { this.dureeSecondes = dureeSecondes; }
    public void setArtiste(Artiste artiste) { this.artiste = artiste; }

    @Override
    public String toString() {
        return titre + " — " + artiste.getNom() + " (" + dureeSecondes / 60 + ":" + String.format("%02d", dureeSecondes % 60) + ")";
    }
}