package View;

import Controller.CatalogueController;
import Controller.PlaylistController;
import Controller.UtilisateurController;
import Model.Album;
import Model.Morceau;
import Model.Playlist;
import Model.User.Abonne;
import Model.User.Utilisateur;

import java.util.List;
import java.util.Scanner;

/**
 * Vue console simple.
 * Respect MVC: cette vue ne touche pas à la persistance (fichiers) et délègue aux contrôleurs.
 */
public class VueConsole {

    private final UtilisateurController utilisateurController;
    private final CatalogueController catalogueController;
    private final PlaylistController playlistController;

    private final Scanner sc = new Scanner(System.in);

    public VueConsole(UtilisateurController u, CatalogueController c, PlaylistController p) {
        this.utilisateurController = u;
        this.catalogueController = c;
        this.playlistController = p;
    }

    public void run() {
        while (true) {
            afficherAccueil();
        }
    }

    private void afficherAccueil() {
        System.out.println("\n=== JavaBeats ===");
        System.out.println("1) Continuer en visiteur");
        System.out.println("2) Se connecter");
        System.out.println("3) S'inscrire");
        System.out.println("0) Quitter");
        System.out.print("> ");

        int choix = lireInt();
        switch (choix) {
            case 1 -> {
                utilisateurController.continuerEnVisiteur();
                afficherMenuSelonRole();
            }
            case 2 -> {
                if (connexion()) {
                    afficherMenuSelonRole();
                }
            }
            case 3 -> {
                if (inscription()) {
                    afficherMenuSelonRole();
                }
            }
            case 0 -> System.exit(0);
            default -> System.out.println("Choix invalide.");
        }
    }

    private void afficherMenuSelonRole() {
        if (utilisateurController.estAdmin()) {
            menuAdmin();
        } else if (utilisateurController.estAbonneActif()) {
            menuAbonne((Abonne) utilisateurController.getUtilisateurCourant());
        } else {
            menuVisiteur();
        }
    }

    private boolean connexion() {
        System.out.print("Login: ");
        String login = sc.nextLine().trim();
        System.out.print("Mot de passe: ");
        String mdp = sc.nextLine();
        boolean ok = utilisateurController.connexion(login, mdp);
        if (!ok) {
            System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
        }
        return ok;
    }

    private boolean inscription() {
        System.out.print("Login: ");
        String login = sc.nextLine().trim();
        System.out.print("Mot de passe: ");
        String mdp = sc.nextLine();
        System.out.print("Email (optionnel): ");
        String email = sc.nextLine().trim();

        boolean ok = utilisateurController.inscriptionAbonne(login, mdp, email);
        if (!ok) {
            System.out.println("Nom d'utilisateur déjà utilisé ou informations invalides.");
        }
        return ok;
    }

    private void menuVisiteur() {
        while (true) {
            System.out.println("\n--- Menu Visiteur ---");
            System.out.println("1) Catalogue");
            System.out.println("2) Rechercher un morceau");
            System.out.println("9) Déconnexion");
            System.out.println("0) Retour accueil");
            System.out.print("> ");

            int choix = lireInt();
            switch (choix) {
                case 1 -> menuCatalogue();
                case 2 -> menuRecherche();
                case 9 -> {
                    utilisateurController.deconnexion();
                    return;
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void menuAbonne(Abonne abonne) {
        while (true) {
            System.out.println("\n--- Menu Abonné ---");
            System.out.println("1) Catalogue");
            System.out.println("2) Albums");
            System.out.println("3) Rechercher un morceau");
            System.out.println("4) Mes playlists");
            System.out.println("9) Déconnexion");
            System.out.println("0) Retour accueil");
            System.out.print("> ");

            int choix = lireInt();
            switch (choix) {
                case 1 -> menuCatalogue();
                case 2 -> menuAlbums();
                case 3 -> menuRecherche();
                case 4 -> menuPlaylists(abonne);
                case 9 -> {
                    utilisateurController.deconnexion();
                    return;
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void menuAdmin() {
        while (true) {
            System.out.println("\n--- Menu Admin ---");
            System.out.println("1) Catalogue");
            System.out.println("2) Rechercher un morceau");
            System.out.println("9) Déconnexion");
            System.out.println("0) Retour accueil");
            System.out.print("> ");

            int choix = lireInt();
            switch (choix) {
                case 1 -> menuCatalogue();
                case 2 -> menuRecherche();
                case 9 -> {
                    utilisateurController.deconnexion();
                    return;
                }
                case 0 -> {
                    return;
                }
                default -> System.out.println("Choix invalide.");
            }
        }
    }

    private void menuCatalogue() {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        if (morceaux.isEmpty()) {
            System.out.println("(catalogue vide)");
            return;
        }

        while (true) {
            System.out.println("\n--- Catalogue ---");
            for (int i = 0; i < morceaux.size(); i++) {
                Morceau m = morceaux.get(i);
                System.out.printf("%d) %s - %s | %d écoutes | %.1f/5%n",
                        i + 1, m.getTitre(), m.getArtiste(), m.getNbEcoutes(), m.getNoteMoyenne());
            }
            System.out.println("0) Retour");
            System.out.print("> ");

            int choix = lireInt();
            if (choix == 0) return;
            int idx = choix - 1;
            if (idx < 0 || idx >= morceaux.size()) {
                System.out.println("Sélection invalide.");
                continue;
            }
            Morceau m = morceaux.get(idx);
            try {
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                System.out.println("Lecture: " + m.getTitre());
            } catch (RuntimeException ex) {
                System.out.println("Erreur: " + ex.getMessage());
            }
        }
    }

    private void menuAlbums() {
        List<Album> albums = catalogueController.listerAlbums();
        if (albums.isEmpty()) {
            System.out.println("(aucun album)");
            return;
        }
        System.out.println("\n--- Albums ---");
        for (int i = 0; i < albums.size(); i++) {
            Album a = albums.get(i);
            System.out.printf("%d) %s - %s | %.1f/5%n", i + 1, a.getNom(), a.getArtiste(), a.getNoteMoyenne());
        }
        System.out.println("(retour auto)");
    }

    private void menuRecherche() {
        System.out.print("Recherche (titre): ");
        String q = sc.nextLine();
        List<Morceau> res = catalogueController.rechercherMorceau(q);
        if (res.isEmpty()) {
            System.out.println("(aucun résultat)");
            return;
        }
        System.out.println("Résultats:");
        for (int i = 0; i < res.size(); i++) {
            Morceau m = res.get(i);
            System.out.printf("%d) %s - %s%n", i + 1, m.getTitre(), m.getArtiste());
        }
    }

    private void menuPlaylists(Utilisateur utilisateur) {
        while (true) {
            List<Playlist> playlists = playlistController.listerPlaylists(utilisateur);
            System.out.println("\n--- Mes playlists ---");
            if (playlists.isEmpty()) {
                System.out.println("(aucune playlist)");
                System.out.println("0) Retour");
                System.out.print("> ");
                if (lireInt() == 0) return;
                continue;
            }

            for (int i = 0; i < playlists.size(); i++) {
                Playlist p = playlists.get(i);
                System.out.printf("%d) %s (%d morceaux)\n", i + 1, p.getNom(), p.getMorceaux().size());
            }
            System.out.println("0) Retour");
            System.out.print("> ");

            int choix = lireInt();
            if (choix == 0) return;
            int idx = choix - 1;
            if (idx < 0 || idx >= playlists.size()) {
                System.out.println("Sélection invalide.");
                continue;
            }
            menuPlaylist(playlists.get(idx));
        }
    }

    private void menuPlaylist(Playlist playlist) {
        while (true) {
            List<Morceau> morceaux = playlist.getMorceaux();
            System.out.println("\n--- Playlist: " + playlist.getNom() + " ---");
            if (morceaux.isEmpty()) {
                System.out.println("(playlist vide)");
                System.out.println("0) Retour");
                System.out.print("> ");
                if (lireInt() == 0) return;
                continue;
            }

            for (int i = 0; i < morceaux.size(); i++) {
                Morceau m = morceaux.get(i);
                System.out.printf("%d) %s - %s\n", i + 1, m.getTitre(), m.getArtiste());
            }
            System.out.println("0) Retour");
            System.out.print("> ");

            int choix = lireInt();
            if (choix == 0) return;
            int idx = choix - 1;
            if (idx < 0 || idx >= morceaux.size()) {
                System.out.println("Sélection invalide.");
                continue;
            }

            Morceau m = morceaux.get(idx);
            try {
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                System.out.println("Lecture: " + m.getTitre());
            } catch (RuntimeException ex) {
                System.out.println("Erreur: " + ex.getMessage());
            }
        }
    }

    private int lireInt() {
        while (true) {
            String line = sc.nextLine();
            try {
                return Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                System.out.print("Entrez un nombre: ");
            }
        }
    }
}

