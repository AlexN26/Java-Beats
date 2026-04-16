package Controller;

import Model.User.Abonne;
import Model.User.Administrateur;
import Model.User.Utilisateur;
import Model.User.Visiteur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilisateurController {

	private final Map<String, Utilisateur> utilisateursParLogin;
	private Utilisateur utilisateurCourant;

	public UtilisateurController() {
		this(null);
	}

	public UtilisateurController(Map<String, Utilisateur> utilisateursInitial) {
		this.utilisateursParLogin = new HashMap<>();
		if (utilisateursInitial != null) {
			this.utilisateursParLogin.putAll(utilisateursInitial);
		}
		// Comptes de démo uniquement si aucune persistance (ou fichier vide/corrompu)
		if (this.utilisateursParLogin.isEmpty()) {
			seedDemoComptes();
		}
	}

	private void seedDemoComptes() {
		utilisateursParLogin.put("admin", new Administrateur("admin", "admin", "admin@javabeats.local"));
		utilisateursParLogin.put("abonne", new Abonne("abonne", "abonne", "abonne@javabeats.local"));
		// Pas de "visiteur" stocké: le visiteur est un mode d'usage.
	}

	/**
	 * Retourne une copie de la map sérialisable (pour PersistanceManager).
	 */
	public Map<String, Utilisateur> snapshotUtilisateursParLogin() {
		return new HashMap<>(utilisateursParLogin);
	}

	public Utilisateur getUtilisateurCourant() {
		return utilisateurCourant;
	}

	public void continuerEnVisiteur() {
		// Visiteur "éphémère" pour la session
		this.utilisateurCourant = new Visiteur("visiteur", "", "");
	}

	public boolean estConnecte() {
		return utilisateurCourant != null;
	}

	public boolean estAdmin() {
		return utilisateurCourant instanceof Administrateur;
	}

	public boolean estAbonneActif() {
		return (utilisateurCourant instanceof Abonne a) && a.isAbonnementActif();
	}

	public boolean connexion(String login, String motDePasse) {
		if (login == null || login.isBlank()) {
			return false;
		}
		Utilisateur u = utilisateursParLogin.get(login);
		if (u == null) {
			return false;
		}
		if (!u.verifierMotDePasse(motDePasse)) {
			return false;
		}
		utilisateurCourant = u;
		return true;
	}

	public boolean inscriptionAbonne(String login, String motDePasse, String email) {
		if (login == null || login.isBlank() || motDePasse == null || motDePasse.isBlank()) {
			return false;
		}
		if (utilisateursParLogin.containsKey(login)) {
			return false;
		}
		Utilisateur nouvel = new Abonne(login, motDePasse, email == null ? "" : email);
		utilisateursParLogin.put(login, nouvel);
		utilisateurCourant = nouvel;
		return true;
	}

	public void ajouterInscrit(String login, String motDePasse, String email) {
		throw new UnsupportedOperationException("Fonctionnalite administrateur non implementee.");
	}

	public void supprimerInscrit(String login) {
		throw new UnsupportedOperationException("Fonctionnalite administrateur non implementee.");
	}

	public List<Utilisateur> listerUtilisateurs() {
		return new ArrayList<>(utilisateursParLogin.values());
	}

	public void deconnexion() {
		utilisateurCourant = null;
	}
}
