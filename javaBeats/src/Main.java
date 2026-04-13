import Controller.CatalogueController;
import Controller.PlaylistController;
import Controller.UtilisateurController;
import Model.Catalogue;
import Persistance.PersistanceManager;

import java.io.IOException;

import View.VueGraphique;

public class Main {

    public static void main(String[] args) {
        Catalogue catalogue = PersistanceManager.chargerOuNouveau();
        if (catalogue.getMorceaux().isEmpty() && catalogue.getAlbums().isEmpty() && catalogue.getArtistes().isEmpty()) {
            CatalogueController.seedDemoData(catalogue);
        }

        UtilisateurController utilisateurController = new UtilisateurController();
        CatalogueController catalogueController = new CatalogueController(catalogue);
        PlaylistController playlistController = new PlaylistController();

        // 🔥 Vue Graphique au lieu de console
        VueGraphique vueGraphique = new VueGraphique(utilisateurController, catalogueController, playlistController);
        vueGraphique.run();

        try {
            PersistanceManager.sauvegarder(catalogue);
        } catch (IOException e) {
            System.err.println("[WARN] Impossible de sauvegarder le catalogue: " + e.getMessage());
        }
    }
}
