package Model;

import java.io.Serializable;

public class Avis implements Serializable {
    private int note;
    private String commentaire;
    private String auteurLogin;
    private Morceau morceau;
    private Album album;

    public Avis(int note, String commentaire, String auteurLogin, Morceau morceau) {
        validerNote(note);
        this.note = note;
        this.commentaire = commentaire;
        this.auteurLogin = auteurLogin;
        this.morceau = morceau;
        this.album = null;
    }

    public Avis(int note, String commentaire, String auteurLogin, Album album) {
        validerNote(note);
        this.note = note;
        this.commentaire = commentaire;
        this.auteurLogin = auteurLogin;
        this.album = album;
        this.morceau = null;
    }

    public int getNote() { return note; }
    public String getCommentaire() { return commentaire; }
    public String getAuteurLogin() { return auteurLogin; }
    public Morceau getMorceau() { return morceau; }
    public Album getAlbum() { return album; }

    public void setNote(int note) {
        validerNote(note);
        this.note = note;
    }

    public void setCommentaire(String commentaire) { this.commentaire = commentaire; }

    private void validerNote(int note) {
        if (note < 1 || note > 5) {
            throw new IllegalArgumentException("Note doit etre entre 1 et 5");
        }
    }

    @Override
    public String toString() {
        return auteurLogin + " -> " + note + "/5 : " + commentaire;
    }
}
