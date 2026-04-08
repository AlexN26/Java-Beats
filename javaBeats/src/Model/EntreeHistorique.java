package Model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EntreeHistorique implements Serializable {
    private Morceau morceau;
    private LocalDateTime dateEcoute;

    public EntreeHistorique(Morceau morceau) {
        this.morceau = morceau;
        this.dateEcoute = LocalDateTime.now();
    }

    public Morceau getMorceau() { return morceau; }
    public LocalDateTime getDateEcoute() { return dateEcoute; }

    @Override
    public String toString() {
        return dateEcoute.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                + " — " + morceau.getTitre();
    }
}