package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Artiste implements Serializable {
    private String nom;
    private String biographie;
    private List<Album> albums;

    public Artiste(String nom, String biographie) {
        this.nom = nom;
        this.biographie = biographie;
        this.albums = new ArrayList<>();
    }

    public void ajouterAlbum(Album album) { albums.add(album); }

    public String getNom() { return nom; }
    public String getBiographie() { return biographie; }
    public List<Album> getAlbums() { return albums; }

    public void setNom(String nom) { this.nom = nom; }
    public void setBiographie(String biographie) { this.biographie = biographie; }


    public void information(){
        System.out.println("nom :"+this.nom);
        System.out.println("Biographie :"+this.biographie);
        System.out.println("album :"+this.albums);
    }

    @Override
    public String toString() { return nom; }
}