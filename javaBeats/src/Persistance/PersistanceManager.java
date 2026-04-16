package Persistance;

import Model.Catalogue;
import Model.User.Utilisateur;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PersistanceManager {
    private static final String FICHIER_CATALOGUE = "catalogue.dat";
    private static final String FICHIER_UTILISATEURS = "utilisateurs.dat";

    // =========================
    // Catalogue
    // =========================

    public static void sauvegarder(Catalogue catalogue) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_CATALOGUE))) {
            oos.writeObject(catalogue);
        }
    }

    public static Catalogue charger() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_CATALOGUE))) {
            return (Catalogue) ois.readObject();
        }
    }

    /**
     * Charge le catalogue depuis le fichier de persistance.
     * Si le fichier n'existe pas ou si le chargement échoue, retourne un catalogue vide.
     */
    public static Catalogue chargerOuNouveau() {
        if (!fichierCatalogueExiste()) {
            return new Catalogue();
        }
        try {
            return charger();
        } catch (Exception e) {
            System.err.println("[WARN] Chargement " + FICHIER_CATALOGUE + " impossible, utilisation d'un catalogue vide: " + e.getMessage());
            return new Catalogue();
        }
    }

    public static boolean fichierCatalogueExiste() {
        return new File(FICHIER_CATALOGUE).exists();
    }

    // =========================
    // Utilisateurs
    // =========================

    public static void sauvegarderUtilisateurs(Map<String, Utilisateur> utilisateursParLogin) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FICHIER_UTILISATEURS))) {
            oos.writeObject(utilisateursParLogin);
        }
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Utilisateur> chargerUtilisateurs() throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FICHIER_UTILISATEURS))) {
            Object obj = ois.readObject();
            if (obj instanceof Map<?, ?> map) {
                return (Map<String, Utilisateur>) map;
            }
            throw new IOException("Format invalide pour " + FICHIER_UTILISATEURS);
        }
    }

    /**
     * Charge les utilisateurs depuis le fichier de persistance.
     * Si le fichier n'existe pas ou si le chargement échoue, retourne une map vide.
     */
    public static Map<String, Utilisateur> chargerUtilisateursOuDefaut() {
        if (!fichierUtilisateursExiste()) {
            return new HashMap<>();
        }
        try {
            return chargerUtilisateurs();
        } catch (Exception e) {
            System.err.println("[WARN] Chargement " + FICHIER_UTILISATEURS + " impossible, utilisation d'une liste vide: " + e.getMessage());
            return new HashMap<>();
        }
    }

    public static boolean fichierUtilisateursExiste() {
        return new File(FICHIER_UTILISATEURS).exists();
    }
}