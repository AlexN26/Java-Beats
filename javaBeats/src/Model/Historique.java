package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Historique implements Serializable {
    private List<EntreeHistorique> entrees;

    public Historique() {
        this.entrees = new ArrayList<>();
    }

    public void ajouterEntree(Morceau morceau) {
        entrees.add(new EntreeHistorique(morceau));
        morceau.incrementerEcoutes();
    }

    public List<EntreeHistorique> getEntrees() { return entrees; }
    public int getNbEcoutes() { return entrees.size(); }
}