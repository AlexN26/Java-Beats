package Persistance;

import Model.Catalogue;
import java.io.*;

public class PersistanceManager {
    private static final String FICHIER = "catalogue.dat";

    public static void sauvegarder(Catalogue catalogue) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER))) {
            oos.writeObject(catalogue);
        }
    }

    public static Catalogue charger() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER))) {
            return (Catalogue) ois.readObject();
        }
    }

    /**
     * Charge le catalogue depuis le fichier de persistance.
     * Si le fichier n'existe pas ou si le chargement échoue, retourne un catalogue vide.
     */
    public static Catalogue chargerOuNouveau() {
        if (!fichierExiste()) {
            return new Catalogue();
        }
        try {
            return charger();
        } catch (Exception e) {
            System.err.println("[WARN] Chargement " + FICHIER + " impossible, utilisation d'un catalogue vide: " + e.getMessage());
            return new Catalogue();
        }
    }

    public static boolean fichierExiste() {
        return new File(FICHIER).exists();
    }
}