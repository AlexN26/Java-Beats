package Controller;

import Model.Morceau;
import Model.Playlist;
import Model.User.Abonne;
import Model.User.Administrateur;
import Model.User.Utilisateur;

import java.util.List;

public class PlaylistController {

	public List<Playlist> listerPlaylists(Utilisateur utilisateur) {
		return getPlaylists(utilisateur);
	}

	public Playlist creerPlaylist(Utilisateur utilisateur, String nom) {
		if (nom == null || nom.isBlank()) {
			return null;
		}
		Playlist p = new Playlist(nom);
		ajouterPlaylist(utilisateur, p);
		return p;
	}

	public boolean supprimerPlaylist(Utilisateur utilisateur, int indexZeroBased) {
		List<Playlist> playlists = getPlaylists(utilisateur);
		if (indexZeroBased < 0 || indexZeroBased >= playlists.size()) {
			return false;
		}
		supprimerPlaylist(utilisateur, playlists.get(indexZeroBased));
		return true;
	}

	public boolean renommerPlaylist(Utilisateur utilisateur, int indexZeroBased, String nouveauNom) {
		if (nouveauNom == null || nouveauNom.isBlank()) {
			return false;
		}
		List<Playlist> playlists = getPlaylists(utilisateur);
		if (indexZeroBased < 0 || indexZeroBased >= playlists.size()) {
			return false;
		}
		playlists.get(indexZeroBased).setNom(nouveauNom);
		return true;
	}

	public boolean ajouterMorceau(Playlist playlist, Morceau morceau) {
		if (playlist == null || morceau == null) {
			return false;
		}
		playlist.ajouterMorceau(morceau);
		return true;
	}

	public boolean retirerMorceau(Playlist playlist, int indexZeroBased) {
		if (playlist == null) {
			return false;
		}
		List<Morceau> morceaux = playlist.getMorceaux();
		if (indexZeroBased < 0 || indexZeroBased >= morceaux.size()) {
			return false;
		}
		playlist.supprimerMorceau(morceaux.get(indexZeroBased));
		return true;
	}

	private List<Playlist> getPlaylists(Utilisateur utilisateur) {
		if (utilisateur instanceof Abonne abonne) {
			return abonne.getPlaylists();
		}
		if (utilisateur instanceof Administrateur administrateur) {
			return administrateur.getPlaylists();
		}
		throw new IllegalArgumentException("Type d'utilisateur incompatible avec les playlists.");
	}

	private void ajouterPlaylist(Utilisateur utilisateur, Playlist playlist) {
		if (utilisateur instanceof Abonne abonne) {
			abonne.ajouterPlaylist(playlist);
			return;
		}
		if (utilisateur instanceof Administrateur administrateur) {
			administrateur.ajouterPlaylist(playlist);
			return;
		}
		throw new IllegalArgumentException("Type d'utilisateur incompatible avec les playlists.");
	}

	private void supprimerPlaylist(Utilisateur utilisateur, Playlist playlist) {
		if (utilisateur instanceof Abonne abonne) {
			abonne.supprimerPlaylist(playlist);
			return;
		}
		if (utilisateur instanceof Administrateur administrateur) {
			administrateur.supprimerPlaylist(playlist);
			return;
		}
		throw new IllegalArgumentException("Type d'utilisateur incompatible avec les playlists.");
	}
}
