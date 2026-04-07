package Model;


public class Album {
    public String nom;
    public String date_de_sortie;
    public float Ecoutes;

    public Album(String date_de_sortie, String nom, float ecoutes) {
        this.date_de_sortie = date_de_sortie;
        this.nom = nom;
        Ecoutes = ecoutes;
    }

    public String getDate_de_sortie() {
        return date_de_sortie;
    }

    public float getEcoutes() {
        return Ecoutes;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate_de_sortie(String date_de_sortie) {
        this.date_de_sortie = date_de_sortie;
    }

    public void setEcoutes(float ecoutes) {
        Ecoutes = ecoutes;
    }

    public void info(){
        System.out.println("nom :"+this.nom);
        System.out.println("date :"+this.date_de_sortie);
        System.out.println("ecoutes :"+this.Ecoutes);
    }
}
