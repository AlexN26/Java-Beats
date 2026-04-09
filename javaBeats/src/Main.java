import Controller.CatalogueController;
import Controller.PlaylistController;
import Controller.UtilisateurController;
import Model.Catalogue;
import Persistance.PersistanceManager;
import View.VueConsole;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        Catalogue catalogue = chargerCatalogue();
        if (catalogue.getMorceaux().isEmpty() && catalogue.getAlbums().isEmpty() && catalogue.getArtistes().isEmpty()) {
            CatalogueController.seedDemoData(catalogue);
        }

        UtilisateurController utilisateurController = new UtilisateurController();
        CatalogueController catalogueController = new CatalogueController(catalogue);
        PlaylistController playlistController = new PlaylistController();

        VueConsole vueConsole = new VueConsole(utilisateurController, catalogueController, playlistController);
        vueConsole.run();

        try {
            PersistanceManager.sauvegarder(catalogue);
        } catch (IOException e) {
            System.err.println("[WARN] Impossible de sauvegarder le catalogue: " + e.getMessage());
        }
    }

    private static Catalogue chargerCatalogue() {
        if (!PersistanceManager.fichierExiste()) {
            return new Catalogue();
        }
        try {
            return PersistanceManager.charger();
        } catch (Exception e) {
            System.err.println("[WARN] Chargement catalogue.dat impossible, utilisation d'un catalogue vide: " + e.getMessage());
            return new Catalogue();
        }
    }
}
