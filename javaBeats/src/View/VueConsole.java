package View;

import Controller.CatalogueController;
import Controller.PlaylistController;
import Controller.UtilisateurController;
import Model.EntreeHistorique;
import Model.Morceau;
import Model.Playlist;
import Model.User.Abonne;
import Model.User.Utilisateur;

import java.util.List;
import java.util.Scanner;

public class VueConsole {

	private final UtilisateurController utilisateurController;
	private final CatalogueController catalogueController;
	private final PlaylistController playlistController;
	private final Scanner scanner;

	public VueConsole(UtilisateurController utilisateurController,
					  CatalogueController catalogueController,
					  PlaylistController playlistController) {
		this.utilisateurController = utilisateurController;
		this.catalogueController = catalogueController;
		this.playlistController = playlistController;
		this.scanner = new Scanner(System.in);
	}

	public void run() {
		boolean quitter = false;
		while (!quitter) {
			if (!utilisateurController.estConnecte()) {
				quitter = ecranAccueil();
				continue;
			}

			Utilisateur u = utilisateurController.getUtilisateurCourant();
			if (utilisateurController.estAdmin()) {
				quitter = menuPrincipalAdmin(u);
			} else if (utilisateurController.estAbonneActif()) {
				quitter = menuPrincipalAbonne((Abonne) u);
			} else {
				quitter = menuPrincipalVisiteur(u);
			}
		}
		System.out.println("\nAu revoir.");
	}

	// =========================
	// Accueil / Session
	// =========================

	private boolean ecranAccueil() {
		while (true) {
			System.out.println("\n=== JavaBeats ===");
			System.out.println("1) Continuer en visiteur");
			System.out.println("2) Connexion");
			System.out.println("3) Inscription (abonné)");
			System.out.println("0) Quitter");

			int choix = lireInt("Choix: ", 0, 3);
			switch (choix) {
				case 1 -> {
					utilisateurController.continuerEnVisiteur();
					return false;
				}
				case 2 -> {
					String login = lireString("Login: ");
					String mdp = lireString("Mot de passe: ");
					if (!utilisateurController.connexion(login, mdp)) {
						System.out.println("Connexion impossible (login/mot de passe incorrect). ");
					} else {
						System.out.println("Connecté en tant que " + utilisateurController.getUtilisateurCourant());
						return false;
					}
				}
				case 3 -> {
					String login = lireString("Choisir un login: ");
					String mdp = lireString("Choisir un mot de passe: ");
					String email = lireString("Email (optionnel): ");
					if (!utilisateurController.inscriptionAbonne(login, mdp, email)) {
						System.out.println("Inscription impossible (login déjà utilisé ou informations invalides). ");
					} else {
						System.out.println("Inscription OK. Connecté en tant que " + utilisateurController.getUtilisateurCourant());
						return false;
					}
				}
				case 0 -> {
					return true;
				}
			}
		}
	}

	private boolean menuPrincipalVisiteur(Utilisateur u) {
		while (true) {
			System.out.println("\n=== Menu principal (Visiteur) ===");
			System.out.println("Connecté: " + u);
			System.out.println("1) Catalogue");
			System.out.println("2) Rechercher" );
			System.out.println("0) Déconnexion");

			int choix = lireInt("Choix: ", 0, 2);
			switch (choix) {
				case 1 -> menuCatalogue(u);
				case 2 -> menuRecherche(u);
				case 0 -> {
					utilisateurController.deconnexion();
					return false;
				}
			}
		}
	}

	private boolean menuPrincipalAbonne(Abonne abonne) {
		while (true) {
			System.out.println("\n=== Menu principal (Abonné) ===");
			System.out.println("Connecté: " + abonne);
			System.out.println("1) Catalogue");
			System.out.println("2) Rechercher");
			System.out.println("3) Playlists");
			System.out.println("4) Historique");
			System.out.println("0) Déconnexion");

			int choix = lireInt("Choix: ", 0, 4);
			switch (choix) {
				case 1 -> menuCatalogue(abonne);
				case 2 -> menuRecherche(abonne);
				case 3 -> menuPlaylists(abonne);
				case 4 -> menuHistorique(abonne);
				case 0 -> {
					utilisateurController.deconnexion();
					return false;
				}
			}
		}
	}

	private boolean menuPrincipalAdmin(Utilisateur admin) {
		while (true) {
			System.out.println("\n=== Menu principal (Administrateur) ===");
			System.out.println("Connecté: " + admin);
			System.out.println("1) Catalogue");
			System.out.println("2) Rechercher");
			System.out.println("3) Administration (démo: seed)" );
			System.out.println("0) Déconnexion");

			int choix = lireInt("Choix: ", 0, 3);
			switch (choix) {
				case 1 -> menuCatalogue(admin);
				case 2 -> menuRecherche(admin);
				case 3 -> menuAdmin();
				case 0 -> {
					utilisateurController.deconnexion();
					return false;
				}
			}
		}
	}

	// =========================
	// Sous-menus
	// =========================

	private void menuCatalogue(Utilisateur u) {
		while (true) {
			System.out.println("\n--- Catalogue ---");
			System.out.println("1) Lister les morceaux");
			System.out.println("2) Écouter un morceau (par n°)" );
			System.out.println("0) Retour" );

			int choix = lireInt("Choix: ", 0, 2);
			switch (choix) {
				case 1 -> afficherListeMorceaux(catalogueController.listerMorceaux());
				case 2 -> {
					List<Morceau> morceaux = catalogueController.listerMorceaux();
					if (morceaux.isEmpty()) {
						System.out.println("Catalogue vide.");
						break;
					}
					afficherListeMorceaux(morceaux);
					int n = lireInt("Numéro du morceau: ", 1, morceaux.size());
					Morceau m = morceaux.get(n - 1);
					catalogueController.ecouter(u, m);
					System.out.println("Lecture: " + m);
				}
				case 0 -> { return; }
			}
		}
	}

	private void menuRecherche(Utilisateur u) {
		while (true) {
			System.out.println("\n--- Recherche ---");
			System.out.println("1) Rechercher un morceau" );
			System.out.println("0) Retour" );

			int choix = lireInt("Choix: ", 0, 1);
			switch (choix) {
				case 1 -> {
					String q = lireString("Titre contient: ");
					List<Morceau> res = catalogueController.rechercherMorceau(q);
					if (res.isEmpty()) {
						System.out.println("Aucun résultat.");
						break;
					}
					afficherListeMorceaux(res);
					if (confirmer("Écouter un morceau de cette liste ?")) {
						int n = lireInt("Numéro du morceau: ", 1, res.size());
						Morceau m = res.get(n - 1);
						catalogueController.ecouter(u, m);
						System.out.println("Lecture: " + m);
					}
				}
				case 0 -> { return; }
			}
		}
	}

	private void menuPlaylists(Abonne abonne) {
		while (true) {
			System.out.println("\n--- Playlists ---");
			System.out.println("1) Lister mes playlists");
			System.out.println("2) Créer une playlist");
			System.out.println("3) Renommer une playlist");
			System.out.println("4) Supprimer une playlist");
			System.out.println("5) Ouvrir une playlist" );
			System.out.println("0) Retour" );

			int choix = lireInt("Choix: ", 0, 5);
			switch (choix) {
				case 1 -> afficherPlaylists(abonne.getPlaylists());
				case 2 -> {
					String nom = lireString("Nom de la playlist: ");
					Playlist p = playlistController.creerPlaylist(abonne, nom);
					System.out.println(p == null ? "Création impossible." : ("Créée: " + p));
				}
				case 3 -> {
					if (abonne.getPlaylists().isEmpty()) {
						System.out.println("Aucune playlist.");
						break;
					}
					afficherPlaylists(abonne.getPlaylists());
					int n = lireInt("Numéro: ", 1, abonne.getPlaylists().size());
					String nom = lireString("Nouveau nom: ");
					System.out.println(playlistController.renommerPlaylist(abonne, n - 1, nom) ? "OK" : "Impossible");
				}
				case 4 -> {
					if (abonne.getPlaylists().isEmpty()) {
						System.out.println("Aucune playlist.");
						break;
					}
					afficherPlaylists(abonne.getPlaylists());
					int n = lireInt("Numéro: ", 1, abonne.getPlaylists().size());
					System.out.println(playlistController.supprimerPlaylist(abonne, n - 1) ? "Supprimée." : "Impossible");
				}
				case 5 -> {
					if (abonne.getPlaylists().isEmpty()) {
						System.out.println("Aucune playlist.");
						break;
					}
					afficherPlaylists(abonne.getPlaylists());
					int n = lireInt("Numéro: ", 1, abonne.getPlaylists().size());
					menuPlaylist(abonne, abonne.getPlaylists().get(n - 1));
				}
				case 0 -> { return; }
			}
		}
	}

	private void menuPlaylist(Abonne abonne, Playlist playlist) {
		while (true) {
			System.out.println("\n--- Playlist: " + playlist.getNom() + " ---");
			System.out.println("Durée totale: " + formaterDuree(playlist.getDureeTotale()));
			System.out.println("1) Afficher les morceaux" );
			System.out.println("2) Ajouter un morceau du catalogue" );
			System.out.println("3) Retirer un morceau" );
			System.out.println("0) Retour" );

			int choix = lireInt("Choix: ", 0, 3);
			switch (choix) {
				case 1 -> afficherListeMorceaux(playlist.getMorceaux());
				case 2 -> {
					List<Morceau> morceaux = catalogueController.listerMorceaux();
					if (morceaux.isEmpty()) {
						System.out.println("Catalogue vide.");
						break;
					}
					afficherListeMorceaux(morceaux);
					int n = lireInt("Numéro du morceau: ", 1, morceaux.size());
					Morceau m = morceaux.get(n - 1);
					System.out.println(playlistController.ajouterMorceau(playlist, m) ? "Ajouté." : "Impossible");
				}
				case 3 -> {
					if (playlist.getMorceaux().isEmpty()) {
						System.out.println("Playlist vide.");
						break;
					}
					afficherListeMorceaux(playlist.getMorceaux());
					int n = lireInt("Numéro du morceau à retirer: ", 1, playlist.getMorceaux().size());
					System.out.println(playlistController.retirerMorceau(playlist, n - 1) ? "Retiré." : "Impossible");
				}
				case 0 -> { return; }
			}
		}
	}

	private void menuHistorique(Abonne abonne) {
		while (true) {
			System.out.println("\n--- Historique ---");
			System.out.println("Nombre d'écoutes: " + abonne.getHistorique().getNbEcoutes());
			System.out.println("1) Afficher les entrées" );
			System.out.println("0) Retour" );
			int choix = lireInt("Choix: ", 0, 1);
			switch (choix) {
				case 1 -> {
					List<EntreeHistorique> entrees = abonne.getHistorique().getEntrees();
					if (entrees.isEmpty()) {
						System.out.println("Historique vide.");
					} else {
						for (int i = 0; i < entrees.size(); i++) {
							System.out.println((i + 1) + ") " + entrees.get(i));
						}
					}
				}
				case 0 -> { return; }
			}
		}
	}

	private void menuAdmin() {
		while (true) {
			System.out.println("\n--- Administration (démo) ---");
			System.out.println("1) Re-seed demo data (ajoute des entrées si catalogue vide)" );
			System.out.println("0) Retour" );
			int choix = lireInt("Choix: ", 0, 1);
			switch (choix) {
				case 1 -> {
					if (catalogueController.getCatalogue().getMorceaux().isEmpty()
							&& catalogueController.getCatalogue().getAlbums().isEmpty()
							&& catalogueController.getCatalogue().getArtistes().isEmpty()) {
						CatalogueController.seedDemoData(catalogueController.getCatalogue());
						System.out.println("OK");
					} else {
						System.out.println("Catalogue non vide: action ignorée pour éviter les doublons.");
					}
				}
				case 0 -> { return; }
			}
		}
	}

	// =========================
	// Helpers affichage / input
	// =========================

	private void afficherListeMorceaux(List<Morceau> morceaux) {
		if (morceaux == null || morceaux.isEmpty()) {
			System.out.println("(aucun morceau)");
			return;
		}
		for (int i = 0; i < morceaux.size(); i++) {
			Morceau m = morceaux.get(i);
			System.out.println((i + 1) + ") " + m + " | écoutes=" + m.getNbEcoutes());
		}
	}

	private void afficherPlaylists(List<Playlist> playlists) {
		if (playlists == null || playlists.isEmpty()) {
			System.out.println("(aucune playlist)");
			return;
		}
		for (int i = 0; i < playlists.size(); i++) {
			Playlist p = playlists.get(i);
			System.out.println((i + 1) + ") " + p + " | durée=" + formaterDuree(p.getDureeTotale()));
		}
	}

	private int lireInt(String prompt, int min, int max) {
		while (true) {
			System.out.print(prompt);
			String s = scanner.nextLine();
			try {
				int v = Integer.parseInt(s.trim());
				if (v < min || v > max) {
					System.out.println("Veuillez entrer un nombre entre " + min + " et " + max + ".");
					continue;
				}
				return v;
			} catch (NumberFormatException e) {
				System.out.println("Veuillez entrer un nombre valide.");
			}
		}
	}

	private String lireString(String prompt) {
		System.out.print(prompt);
		return scanner.nextLine().trim();
	}

	private boolean confirmer(String question) {
		while (true) {
			String rep = lireString(question + " (o/n): ").toLowerCase();
			if (rep.equals("o") || rep.equals("oui")) return true;
			if (rep.equals("n") || rep.equals("non")) return false;
			System.out.println("Réponse attendue: o/n");
		}
	}

	private String formaterDuree(int secondes) {
		int m = secondes / 60;
		int s = secondes % 60;
		return m + ":" + String.format("%02d", s);
	}
}
