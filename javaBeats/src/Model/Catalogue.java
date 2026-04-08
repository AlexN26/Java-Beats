package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Catalogue implements Serializable {
    private List<Album> albums;
    private List<Artiste> artistes;
    private List<Morceau> morceaux;

    public Catalogue() {
        this.albums = new ArrayList<>();
        this.artistes = new ArrayList<>();
        this.morceaux = new ArrayList<>();
    }

    public void ajouterAlbum(Album album) { albums.add(album); }
    public void ajouterArtiste(Artiste artiste) { artistes.add(artiste); }
    public void ajouterMorceau(Morceau morceau) { morceaux.add(morceau); }

    public List<Morceau> rechercherMorceau(String titre) {
        return morceaux.stream()
                .filter(m -> m.getTitre().toLowerCase().contains(titre.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Artiste> rechercherArtiste(String nom) {
        return artistes.stream()
                .filter(a -> a.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Album> rechercherAlbum(String nom) {
        return albums.stream()
                .filter(a -> a.getNom().toLowerCase().contains(nom.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Album> getAlbums() { return albums; }
    public List<Artiste> getArtistes() { return artistes; }
    public List<Morceau> getMorceaux() { return morceaux; }
}