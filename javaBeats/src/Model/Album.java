package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    private String nom;
    private String dateDeSortie;
    private TypeAlbum type;
    private Artiste artiste;
    private List<Morceau> morceaux;
    private List<Avis> avis;

    public Album(String nom, String dateDeSortie, TypeAlbum type, Artiste artiste) {
        this.nom = nom;
        this.dateDeSortie = dateDeSortie;
        this.type = type;
        this.artiste = artiste;
        this.morceaux = new ArrayList<>();
        this.avis = new ArrayList<>();
    }

    public void ajouterMorceau(Morceau morceau) { morceaux.add(morceau); }
    public void ajouterAvis(Avis avis) { getAvis().add(avis); }

    public int getTotalEcoutes() {
        return morceaux.stream().mapToInt(Morceau::getNbEcoutes).sum();
    }

    public double getNoteMoyenne() {
        return getAvis().stream().mapToInt(Avis::getNote).average().orElse(0);
    }

    public String getNom() { return nom; }
    public String getDateDeSortie() { return dateDeSortie; }
    public TypeAlbum getType() { return type; }
    public Artiste getArtiste() { return artiste; }
    public List<Morceau> getMorceaux() { return morceaux; }
    public List<Avis> getAvis() {
        if (avis == null) {
            avis = new ArrayList<>();
        }
        return avis;
    }

    public void setNom(String nom) { this.nom = nom; }
    public void setDateDeSortie(String dateDeSortie) { this.dateDeSortie = dateDeSortie; }
    public void setType(TypeAlbum type) { this.type = type; }

    public void information(){
        System.out.println("nom :"+this.nom);
        System.out.println("date de sortie :"+this.dateDeSortie);
        System.out.println("Artiste :"+this.artiste);
    }

    @Override
    public String toString() {
        return nom + " (" + type + ", " + dateDeSortie + ")";
    }
}
