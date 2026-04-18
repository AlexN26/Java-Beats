package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Morceau implements Serializable {
    private String titre;
    private int dureeSecondes;
    private int nbEcoutes;
    private Artiste artiste;
    private List<Avis> avis;

    public Morceau(String titre, int dureeSecondes, Artiste artiste) {
        this.titre = titre;
        this.dureeSecondes = dureeSecondes;
        this.artiste = artiste;
        this.nbEcoutes = 0;
        this.avis = new ArrayList<>();
    }

    public void incrementerEcoutes() { this.nbEcoutes++; }
    public void ajouterAvis(Avis avis) {
        getAvis().add(avis);
    }
    public double getNoteMoyenne() {
        return getAvis().stream().mapToInt(Avis::getNote).average().orElse(0);
    }

    public String getTitre() { return titre; }
    public int getDureeSecondes() { return dureeSecondes; }
    public int getNbEcoutes() { return nbEcoutes; }
    public Artiste getArtiste() { return artiste; }
    public List<Avis> getAvis() {
        if (avis == null) {
            avis = new ArrayList<>();
        }
        return avis;
    }

    public void setTitre(String titre) { this.titre = titre; }
    public void setDureeSecondes(int dureeSecondes) { this.dureeSecondes = dureeSecondes; }
    public void setArtiste(Artiste artiste) { this.artiste = artiste; }


    public void information(){
        System.out.println("titre :"+this.titre);
        System.out.println("nombre d'ecoute :"+this.nbEcoutes);
        System.out.println("artiste :"+this.artiste);

    }
    @Override
    public String toString() {
        return titre + " — " + artiste.getNom() + " (" + dureeSecondes / 60 + ":" + String.format("%02d", dureeSecondes % 60) + ")";
    }
}
