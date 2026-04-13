package Model;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

public class Statistique implements Serializable {
    private Catalogue catalogue;
    private int nbEcoutesTotal;
    public Statistique(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    public Morceau getMorceauLePlusEcoute() {
        return catalogue.getMorceaux().stream()
                .max(Comparator.comparingInt(Morceau::getNbEcoutes))
                .orElse(null);
    }

    public double getDureeMoyenneMorceaux() {
        return catalogue.getMorceaux().stream()
                .mapToInt(Morceau::getDureeSecondes)
                .average()
                .orElse(0);
    }

    public List<Artiste> getTop3Artistes() {
        return catalogue.getArtistes().stream()
                .sorted(Comparator.comparingInt(
                        a -> -a.getAlbums().stream().mapToInt(Album::getTotalEcoutes).sum()))
                .limit(3)
                .toList();
    }
}