package Model;

import java.io.Serializable;

public class Avis implements Serializable {
    private int note;
    private String commentaire;
    private String auteurLogin;
    private Morceau morceau;

    public Avis(int note, String commentaire, String auteurLogin, Morceau morceau) {
        if (note < 1 || note > 5) throw new IllegalArgumentException("Note doit être entre 1 et 5");
        this.note = note;
        this.commentaire = commentaire;
        this.auteurLogin = auteurLogin;
        this.morceau = morceau;
    }

    public int getNote() { return note; }
    public String getCommentaire() { return commentaire; }
    public String getAuteurLogin() { return auteurLogin; }
    public Morceau getMorceau() { return morceau; }
    public void setNote(int note) { this.note = note; }
    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    @Override
    public String toString() {
        return auteurLogin + " → " + note + "/5 : " + commentaire;
    }
}