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

    public static boolean fichierExiste() {
        return new File(FICHIER).exists();
    }
}