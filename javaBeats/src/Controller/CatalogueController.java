package Controller;

import Model.*;
import Model.User.Abonne;
import Model.User.Utilisateur;
import Model.User.Visiteur;
import java.util.List;

public class CatalogueController {

	private final Catalogue catalogue;

	public CatalogueController(Catalogue catalogue) {
		this.catalogue = catalogue;
	}

	public Catalogue getCatalogue() {
		return catalogue;
	}

	public List<Morceau> listerMorceaux() {
		return catalogue.getMorceaux();
	}

	public List<Album> listerAlbums() {
		return catalogue.getAlbums();
	}

	public List<Artiste> listerArtistes() {
		return catalogue.getArtistes();
	}

	public void ajouterMusique(Morceau morceau) {
		throw new UnsupportedOperationException("Fonctionnalite administrateur non implementee.");
	}

	public void supprimerMusique(Morceau morceau) {
		throw new UnsupportedOperationException("Fonctionnalite administrateur non implementee.");
	}

	public List<Morceau> rechercherMorceau(String titre) {
		return catalogue.rechercherMorceau(titre);
	}

	public List<Album> rechercherAlbum(String nom) {
		return catalogue.rechercherAlbum(nom);
	}

	public List<Artiste> rechercherArtiste(String nom) {
		return catalogue.rechercherArtiste(nom);
	}

	public void ecouter(Utilisateur utilisateur, Morceau morceau) {
		if (morceau == null) return;
		if (utilisateur instanceof Visiteur visiteur) {
			if (visiteur.getNbEcoutesSession() >= 5) {
				throw new RuntimeException("Limite de 5 écoutes atteinte pour cette session.");
			}
			visiteur.incrementerEcoutesSession();
			morceau.incrementerEcoutes();
		} else if (utilisateur instanceof Abonne abonne && abonne.isAbonnementActif()) {
			abonne.getHistorique().ajouterEntree(morceau);
		} else {
			morceau.incrementerEcoutes();
		}
	}

	public Morceau getMorceauParIndex(int indexZeroBased) {
		List<Morceau> morceaux = catalogue.getMorceaux();
		if (indexZeroBased < 0 || indexZeroBased >= morceaux.size()) {
			return null;
		}
		return morceaux.get(indexZeroBased);
	}

	public static void seedDemoData(Catalogue catalogue) {
		// Données minimales pour pouvoir naviguer dans le menu.
		Artiste daftPunk = new Artiste("Daft Punk", "Duo français de musique électronique.");
		Artiste stromae = new Artiste("Stromae", "Auteur-compositeur-interprète belge.");
		catalogue.ajouterArtiste(daftPunk);
		catalogue.ajouterArtiste(stromae);

		Album randomAccess = new Album("Random Access Memories", "2013", TypeAlbum.ALBUM, daftPunk);
		Album racineCarree = new Album("Racine carrée", "2013", TypeAlbum.ALBUM, stromae);
		catalogue.ajouterAlbum(randomAccess);
		catalogue.ajouterAlbum(racineCarree);
		daftPunk.ajouterAlbum(randomAccess);
		stromae.ajouterAlbum(racineCarree);

		Morceau getLucky = new Morceau("Get Lucky", 369, daftPunk);
		Morceau instantCrush = new Morceau("Instant Crush", 337, daftPunk);
		Morceau tousLesMemes = new Morceau("Tous les mêmes", 197, stromae);
		catalogue.ajouterMorceau(getLucky);
		catalogue.ajouterMorceau(instantCrush);
		catalogue.ajouterMorceau(tousLesMemes);

		randomAccess.ajouterMorceau(getLucky);
		randomAccess.ajouterMorceau(instantCrush);
		racineCarree.ajouterMorceau(tousLesMemes);
	}
}
