package Model;

import java.util.ArrayList;
import java.util.List;

public class Groupe extends Artiste {
    private List<String> membres;

    public Groupe(String nom, String biographie) {
        super(nom, biographie);
        this.membres = new ArrayList<>();
    }

    public void ajouterMembre(String membre) { membres.add(membre); }
    public List<String> getMembres() { return membres; }
}