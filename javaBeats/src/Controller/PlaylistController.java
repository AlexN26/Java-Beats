package Controller;

import Model.Morceau;
import Model.Playlist;
import Model.User.Abonne;

import java.util.List;

public class PlaylistController {

	public List<Playlist> listerPlaylists(Abonne abonne) {
		return abonne.getPlaylists();
	}

	public Playlist creerPlaylist(Abonne abonne, String nom) {
		if (nom == null || nom.isBlank()) {
			return null;
		}
		Playlist p = new Playlist(nom);
		abonne.ajouterPlaylist(p);
		return p;
	}

	public boolean supprimerPlaylist(Abonne abonne, int indexZeroBased) {
		List<Playlist> playlists = abonne.getPlaylists();
		if (indexZeroBased < 0 || indexZeroBased >= playlists.size()) {
			return false;
		}
		abonne.supprimerPlaylist(playlists.get(indexZeroBased));
		return true;
	}

	public boolean renommerPlaylist(Abonne abonne, int indexZeroBased, String nouveauNom) {
		if (nouveauNom == null || nouveauNom.isBlank()) {
			return false;
		}
		List<Playlist> playlists = abonne.getPlaylists();
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
}
