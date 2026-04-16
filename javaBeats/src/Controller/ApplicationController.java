package Controller;

import Model.Catalogue;
import Model.User.Utilisateur;
import Persistance.PersistanceManager;
import View.VueGraphique;

import java.io.IOException;
import java.util.Map;

/**
 * Point d'entrée "métier" de l'application.
 * - Chargement au démarrage
 * - Sauvegarde automatique à la fermeture (shutdown hook)
 * - Instanciation des contrôleurs et de la vue
 *
 * Main reste minimal et ne contient pas de logique applicative.
 */
public class ApplicationController {

    public void run() {
        Catalogue catalogue = PersistanceManager.chargerOuNouveau();
        if (catalogue.getMorceaux().isEmpty() && catalogue.getAlbums().isEmpty() && catalogue.getArtistes().isEmpty()) {
            CatalogueController.seedDemoData(catalogue);
        }

        Map<String, Utilisateur> utilisateursCharges = PersistanceManager.chargerUtilisateursOuDefaut();

        UtilisateurController utilisateurController = new UtilisateurController(utilisateursCharges);
        CatalogueController catalogueController = new CatalogueController(catalogue);
        PlaylistController playlistController = new PlaylistController();

        // Sauvegarde automatique à la fermeture (exigence sujet)
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                PersistanceManager.sauvegarder(catalogue);
            } catch (IOException e) {
                System.err.println("[WARN] Impossible de sauvegarder le catalogue: " + e.getMessage());
            }
            try {
                PersistanceManager.sauvegarderUtilisateurs(utilisateurController.snapshotUtilisateursParLogin());
            } catch (IOException e) {
                System.err.println("[WARN] Impossible de sauvegarder les utilisateurs: " + e.getMessage());
            }
        }));

        VueGraphique vueGraphique = new VueGraphique(utilisateurController, catalogueController, playlistController);
        vueGraphique.run();
    }
}

