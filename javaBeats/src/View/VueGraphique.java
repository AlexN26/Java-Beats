package View;

import Controller.CatalogueController;
import Controller.PlaylistController;
import Controller.UtilisateurController;
import Model.*;
import Model.User.Abonne;
import Model.User.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VueGraphique extends JFrame {

    private final UtilisateurController utilisateurController;
    private final CatalogueController catalogueController;
    private final PlaylistController playlistController;

    public VueGraphique(UtilisateurController u, CatalogueController c, PlaylistController p) {
        this.utilisateurController = u;
        this.catalogueController = c;
        this.playlistController = p;
        setTitle("JavaBeats");
        setSize(750, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        afficherAccueil();
        setVisible(true);
    }

    public void run() { setVisible(true); }

    
    // Helpers
    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setPreferredSize(new Dimension(220, 38));
        return bouton;
    }

    private JButton creerBoutonRouge(String texte) {
        JButton bouton = creerBouton(texte);
        bouton.setBackground(new Color(220, 40, 60));
        bouton.setForeground(Color.WHITE);
        return bouton;
    }

    private JList<String> creerListe(DefaultListModel<String> modele) {
        JList<String> liste = new JList<>(modele);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.setFixedCellHeight(28);
        return liste;
    }

    private JScrollPane creerScroll(JList<String> liste) {
        return new JScrollPane(liste);
    }

    private JPanel creerPanelBoutons(JButton... boutons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        for (JButton b : boutons) panel.add(b);
        return panel;
    }

    private JPanel creerPanelBoutonsDeuxLignes(JButton... boutons) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        for (int i = 0; i < boutons.length; i++) {
            if (i < 3) ligne1.add(boutons[i]);
            else ligne2.add(boutons[i]);
        }
        panel.add(ligne1);
        if (boutons.length > 3) panel.add(ligne2);
        return panel;
    }

    private void changerContenu(JComponent nord, JComponent centre, JComponent sud) {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(0, 5));
        if (nord   != null) add(nord,   BorderLayout.NORTH);
        if (centre != null) add(centre, BorderLayout.CENTER);
        if (sud    != null) add(sud,    BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void afficherInfo(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    private boolean demanderConfirmation(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirmation",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private boolean utilisateurPeutDonnerAvis() {
        return utilisateurController.estAbonneActif();
    }

    private Integer demanderNoteAvis() {
        String saisie = JOptionPane.showInputDialog(this, "Note sur 5 :", "Donner un avis",
                JOptionPane.PLAIN_MESSAGE);
        if (saisie == null) return null;
        try {
            int note = Integer.parseInt(saisie.trim());
            if (note < 1 || note > 5) {
                afficherInfo("La note doit etre comprise entre 1 et 5.");
                return null;
            }
            return note;
        } catch (NumberFormatException ex) {
            afficherInfo("Note invalide.");
            return null;
        }
    }

    private void afficherAvis(String titre, List<Avis> avis) {
        JTextArea zoneTexte = new JTextArea();
        zoneTexte.setEditable(false);
        if (avis.isEmpty()) {
            zoneTexte.setText("Aucun avis pour le moment.");
        } else {
            StringBuilder contenu = new StringBuilder();
            for (Avis a : avis) {
                String commentaire = a.getCommentaire() == null ? "" : a.getCommentaire();
                contenu.append(a.getAuteurLogin())
                        .append(" - ")
                        .append(a.getNote())
                        .append("/5");
                if (!commentaire.isBlank()) {
                    contenu.append(" : ").append(commentaire);
                }
                contenu.append("\n");
            }
            zoneTexte.setText(contenu.toString());
        }
        JOptionPane.showMessageDialog(this, new JScrollPane(zoneTexte), titre, JOptionPane.INFORMATION_MESSAGE);
    }

    private void saisirAvisMorceau(Morceau morceau) {
        Integer note = demanderNoteAvis();
        if (note == null) return;
        String commentaire = JOptionPane.showInputDialog(this, "Commentaire :", "Donner un avis",
                JOptionPane.PLAIN_MESSAGE);
        if (commentaire == null) return;
        try {
            catalogueController.ajouterAvisMorceau(utilisateurController.getUtilisateurCourant(), morceau, note, commentaire);
            afficherInfo("Avis ajoute.");
        } catch (IllegalArgumentException ex) {
            afficherInfo(ex.getMessage());
        }
    }

    private void saisirAvisAlbum(Album album) {
        Integer note = demanderNoteAvis();
        if (note == null) return;
        String commentaire = JOptionPane.showInputDialog(this, "Commentaire :", "Donner un avis",
                JOptionPane.PLAIN_MESSAGE);
        if (commentaire == null) return;
        try {
            catalogueController.ajouterAvisAlbum(utilisateurController.getUtilisateurCourant(), album, note, commentaire);
            afficherInfo("Avis ajoute.");
        } catch (IllegalArgumentException ex) {
            afficherInfo(ex.getMessage());
        }
    }

    private String formaterDuree(int secondes) {
        return secondes / 60 + ":" + String.format("%02d", secondes % 60);
    }

    private JButton creerBoutonDeconnexion() {

        JButton bouton = creerBoutonRouge("Déconnexion");
        bouton.addActionListener(e -> { utilisateurController.deconnexion(); afficherAccueil(); });
        return bouton;
    }

    
    // Accueil
    private void afficherAccueil() {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        JButton btnVisiteur    = creerBouton("Continuer en visiteur");
        JButton btnConnexion   = creerBouton("Se connecter");
        JButton btnInscription = creerBouton("S'inscrire");
        JButton btnQuitter     = creerBouton("Quitter");

        for (JButton b : new JButton[]{btnVisiteur, btnConnexion, btnInscription, btnQuitter}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b);
            centre.add(Box.createVerticalStrut(10));
        }

        btnVisiteur.addActionListener(e -> {
            utilisateurController.continuerEnVisiteur();
            afficherMenuSelonRole();
        });
        btnConnexion.addActionListener(e -> ecranConnexion());
        btnInscription.addActionListener(e -> ecranInscription());
        btnQuitter.addActionListener(e -> System.exit(0));

        changerContenu(new JLabel("JavaBeats", SwingConstants.CENTER), centre, null);
    }

    
    // connexion et inscription
    private void ecranConnexion() {
        JTextField champNom = new JTextField();
        JPasswordField champMotDePasse = new JPasswordField();

        Object[] champs = {"Nom d'utilisateur :", champNom, "Mot de passe :", champMotDePasse};
        int res = JOptionPane.showConfirmDialog(this, champs, "Connexion", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        if (!utilisateurController.connexion(champNom.getText(), new String(champMotDePasse.getPassword()))) {
            afficherInfo("Nom d'utilisateur ou mot de passe incorrect.");
        } else {
            afficherMenuSelonRole();
        }
    }

    private void ecranInscription() {
        JTextField champNom = new JTextField();
        JPasswordField champMotDePasse = new JPasswordField();
        JTextField champEmail = new JTextField();

        Object[] champs = {"Nom d'utilisateur :", champNom, "Mot de passe :", champMotDePasse, "Email (optionnel) :", champEmail};
        int res = JOptionPane.showConfirmDialog(this, champs, "Inscription", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        if (!utilisateurController.inscriptionAbonne(champNom.getText(),
                new String(champMotDePasse.getPassword()), champEmail.getText())) {
            afficherInfo("Nom d'utilisateur déjà utilisé ou informations invalides.");
        } else {
            afficherMenuSelonRole();
        }
    }

    
    // Routage
    private void afficherMenuSelonRole() {
        if (utilisateurController.estAdmin()) {
            menuAdmin();
        } else if (utilisateurController.estAbonneActif()) {
            menuAbonne((Abonne) utilisateurController.getUtilisateurCourant());
        } else {
            menuVisiteur();
        }
    }

    
    // menu
    private void menuVisiteur() {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        JButton btnCatalogue = creerBouton("Catalogue");
        JButton btnRecherche = creerBouton("Rechercher");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b);
            centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnRecherche.addActionListener(e -> menuRecherche());

        changerContenu(new JLabel("Menu Visiteur", SwingConstants.CENTER), centre, creerPanelBoutons(creerBoutonDeconnexion()));
    }

    private void menuAbonne(Abonne abonne) {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        JButton btnCatalogue  = creerBouton("Catalogue");
        JButton btnAlbums     = creerBouton("Albums");
        JButton btnRecherche  = creerBouton("Rechercher");
        JButton btnPlaylists  = creerBouton("Mes playlists");
        JButton btnHistorique = creerBouton("Historique");

        for (JButton b : new JButton[]{btnCatalogue, btnAlbums, btnRecherche, btnPlaylists, btnHistorique}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b);
            centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnAlbums.addActionListener(e -> menuAlbums());
        btnRecherche.addActionListener(e -> menuRecherche());
        btnPlaylists.addActionListener(e -> menuPlaylists(abonne));
        btnHistorique.addActionListener(e -> menuHistorique(abonne));

        changerContenu(new JLabel("Menu Abonné", SwingConstants.CENTER), centre, creerPanelBoutons(creerBoutonDeconnexion()));
    }

    private void menuAdmin() {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));

        JButton btnCatalogue = creerBouton("Catalogue");
        JButton btnRecherche = creerBouton("Rechercher");
        JButton btnPlaylists = creerBouton("Mes playlists");
        JButton btnGererCat  = creerBouton("Gérer le catalogue");
        JButton btnGererAbo  = creerBouton("Gérer les abonnés");
        JButton btnStats     = creerBouton("Statistiques");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche, btnPlaylists, btnGererCat, btnGererAbo, btnStats}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b);
            centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnRecherche.addActionListener(e -> menuRecherche());
        btnPlaylists.addActionListener(e -> menuPlaylists(utilisateurController.getUtilisateurCourant()));
        btnGererCat.addActionListener(e -> menuGererCatalogue());
        btnGererAbo.addActionListener(e -> menuGererAbonnes());
        btnStats.addActionListener(e -> menuStatistiques());

        changerContenu(new JLabel("Menu Admin", SwingConstants.CENTER), centre, creerPanelBoutons(creerBoutonDeconnexion()));
    }

    
    // catalogue
    private void menuCatalogue() {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        DefaultListModel<String> modele = new DefaultListModel<>();
        morceaux.forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste() + " | " + m.getNbEcoutes() + " écoutes | " +
                String.format("%.1f/5", m.getNoteMoyenne())));
        if (morceaux.isEmpty()) modele.addElement("(catalogue vide)");

        JList<String> liste = creerListe(modele);

        JButton btnEcouter = creerBouton("Écouter");
        JButton btnAvis    = creerBouton("Donner un avis");
        JButton btnVoirAvis = creerBouton("Voir les avis");
        JButton btnRetour  = creerBouton("Retour");

        btnEcouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceaux.isEmpty()) { afficherInfo("Sélectionnez un morceau."); return; }
            Morceau m = morceaux.get(idx);
            try {
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                afficherBarreProgression(m);
                modele.set(idx, m.getTitre() + " - " + m.getArtiste() + " | " + m.getNbEcoutes() + " écoutes | " +
                        String.format("%.1f/5", m.getNoteMoyenne()));
            } catch (RuntimeException ex) {
                afficherInfo(ex.getMessage());
            }
        });
        btnAvis.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceaux.isEmpty()) { afficherInfo("Sélectionnez un morceau."); return; }
            saisirAvisMorceau(morceaux.get(idx));
            Morceau m = morceaux.get(idx);
            modele.set(idx, m.getTitre() + " - " + m.getArtiste() + " | " + m.getNbEcoutes() + " écoutes | " +
                    String.format("%.1f/5", m.getNoteMoyenne()));
        });
        btnVoirAvis.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceaux.isEmpty()) { afficherInfo("Sélectionnez un morceau."); return; }
            Morceau m = morceaux.get(idx);
            afficherAvis("Avis - " + m.getTitre(), m.getAvis());
        });
        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JPanel sud = utilisateurPeutDonnerAvis()
                ? creerPanelBoutonsDeuxLignes(btnEcouter, btnAvis, btnVoirAvis, btnRetour)
                : creerPanelBoutonsDeuxLignes(btnEcouter, btnVoirAvis, btnRetour);
        changerContenu(new JLabel("Catalogue", SwingConstants.CENTER), creerScroll(liste), sud);
    }

    private void menuAlbums() {
        List<Album> albums = catalogueController.listerAlbums();
        DefaultListModel<String> modele = new DefaultListModel<>();
        albums.forEach(a -> modele.addElement(a.getNom() + " - " + a.getArtiste() + " | " + a.getMorceaux().size() +
                " morceaux | " + String.format("%.1f/5", a.getNoteMoyenne())));
        if (albums.isEmpty()) modele.addElement("(aucun album)");

        JList<String> liste = creerListe(modele);
        JButton btnAvis = creerBouton("Donner un avis");
        JButton btnVoirAvis = creerBouton("Voir les avis");
        JButton btnRetour = creerBouton("Retour");

        btnAvis.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || albums.isEmpty()) { afficherInfo("Sélectionnez un album."); return; }
            saisirAvisAlbum(albums.get(idx));
            Album a = albums.get(idx);
            modele.set(idx, a.getNom() + " - " + a.getArtiste() + " | " + a.getMorceaux().size() +
                    " morceaux | " + String.format("%.1f/5", a.getNoteMoyenne()));
        });
        btnVoirAvis.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || albums.isEmpty()) { afficherInfo("Sélectionnez un album."); return; }
            Album a = albums.get(idx);
            afficherAvis("Avis - " + a.getNom(), a.getAvis());
        });
        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        changerContenu(new JLabel("Albums", SwingConstants.CENTER), creerScroll(liste),
                creerPanelBoutons(btnAvis, btnVoirAvis, btnRetour));
    }

    
    // Recherche
    private void menuRecherche() {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);
        List<Morceau>[] resultat = new List[]{List.of()};

        JTextField champRecherche = new JTextField();
        JButton btnChercher = creerBouton("Chercher");
        JButton btnEcouter  = creerBouton("Écouter");
        JButton btnRetour   = creerBouton("Retour");

        btnChercher.addActionListener(e -> {
            resultat[0] = catalogueController.rechercherMorceau(champRecherche.getText());
            modele.clear();
            if (resultat[0].isEmpty()) modele.addElement("(aucun résultat)");
            else resultat[0].forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));
        });

        btnEcouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || resultat[0].isEmpty()) {
                afficherInfo("Sélectionnez un morceau.");
                return;
            }
            Morceau m = resultat[0].get(idx);
            try {
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                afficherBarreProgression(m);
                modele.set(idx, m.getTitre() + " - " + m.getArtiste());
            } catch (RuntimeException ex) {
                afficherInfo(ex.getMessage());
            }
        });

        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JPanel nord = new JPanel(new BorderLayout(10, 10));
        nord.add(new JLabel("Recherche", SwingConstants.CENTER), BorderLayout.NORTH);
        nord.add(champRecherche, BorderLayout.CENTER);
        nord.add(btnChercher, BorderLayout.EAST);

        changerContenu(nord, creerScroll(liste), creerPanelBoutons(btnEcouter, btnRetour));
    }

    //barre d'ecoute
    private void afficherBarreProgression(Morceau morceau) {
        JDialog dialog = new JDialog(this, "Lecture en cours", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JLabel label = new JLabel("▶ " + morceau.getTitre() + " - " + morceau.getArtiste(), SwingConstants.CENTER);
        JProgressBar barre = new JProgressBar(0, 100);
        barre.setStringPainted(true);

        dialog.add(label, BorderLayout.NORTH);
        dialog.add(barre, BorderLayout.CENTER);

        // timer de la barre pour que ca dure 5 secondes
        Timer timer = new Timer(500, null);
        timer.addActionListener(evt -> {
            int val = barre.getValue() + 10;
            if (val >= 100) {
                timer.stop();
                dialog.dispose();
            } else {
                barre.setValue(val);
                barre.setString(val + "%");
            }
        });
        timer.start();
        dialog.setVisible(true);
    }
    
    // Playlists
        private void menuPlaylists(Utilisateur utilisateur) {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);

        Runnable rafraichir = () -> {
            modele.clear();
            List<Playlist> playlists = playlistController.listerPlaylists(utilisateur);
            if (playlists.isEmpty()) modele.addElement("(aucune playlist)");
            else playlists.forEach(p -> modele.addElement(p.getNom() + " | " + p.getMorceaux().size() + " morceaux - " + formaterDuree(p.getDureeTotale())));
        };
        rafraichir.run();

        JButton btnCreer     = creerBouton("Créer");
        JButton btnRenommer  = creerBouton("Renommer");
        JButton btnSupprimer = creerBouton("Supprimer");
        JButton btnOuvrir    = creerBouton("Ouvrir");
        JButton btnRetour    = creerBouton("Retour");

        btnCreer.addActionListener(e -> {
            String nom = JOptionPane.showInputDialog(this, "Nom de la playlist :", "Créer une playlist",
                    JOptionPane.PLAIN_MESSAGE);
            if (nom == null) return;
            nom = nom.trim();
            if (nom.isEmpty()) { afficherInfo("Entrez un nom."); return; }
            Playlist p = playlistController.creerPlaylist(utilisateur, nom);
            if (p == null) afficherInfo("Création impossible.");
            else rafraichir.run();
        });

        btnRenommer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            List<Playlist> playlists = playlistController.listerPlaylists(utilisateur);
            if (idx < 0 || playlists.isEmpty()) { afficherInfo("Sélectionnez une playlist."); return; }
            String nom = JOptionPane.showInputDialog(this, "Nouveau nom de la playlist :",
                    playlists.get(idx).getNom());
            if (nom == null) return;
            nom = nom.trim();
            if (nom.isEmpty()) { afficherInfo("Entrez un nouveau nom."); return; }
            if (playlistController.renommerPlaylist(utilisateur, idx, nom)) rafraichir.run();
            else afficherInfo("Impossible.");
        });

        btnSupprimer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || playlistController.listerPlaylists(utilisateur).isEmpty()) { afficherInfo("Sélectionnez une playlist."); return; }
            if (demanderConfirmation("Supprimer cette playlist ?"))
                if (playlistController.supprimerPlaylist(utilisateur, idx)) rafraichir.run();
        });

        btnOuvrir.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            List<Playlist> playlists = playlistController.listerPlaylists(utilisateur);
            if (idx < 0 || playlists.isEmpty()) { afficherInfo("Sélectionnez une playlist."); return; }
            menuPlaylist(utilisateur, playlists.get(idx));
        });

        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JPanel nord = new JPanel(new BorderLayout(10, 5));
        nord.add(new JLabel("Mes Playlists", SwingConstants.CENTER), BorderLayout.CENTER);

    //bouton retour
        JPanel sud = new JPanel();
        sud.setLayout(new BoxLayout(sud, BoxLayout.Y_AXIS));
        sud.add(creerPanelBoutons(btnCreer, btnRenommer, btnSupprimer));
        sud.add(creerPanelBoutons(btnOuvrir, btnRetour));

        changerContenu(nord, creerScroll(liste), sud);
    }

    //menu de la playlist
    private void menuPlaylist(Utilisateur utilisateur, Playlist playlist) {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);
        JLabel labelDuree = new JLabel("Durée : " + formaterDuree(playlist.getDureeTotale()), SwingConstants.CENTER);

        Runnable rafraichir = () -> {
            modele.clear();
            List<Morceau> morceaux = playlist.getMorceaux();
            if (morceaux.isEmpty()) modele.addElement("(playlist vide)");
            else morceaux.forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));
            labelDuree.setText("Durée : " + formaterDuree(playlist.getDureeTotale()));
        };
        rafraichir.run();

        JButton btnAjouter = creerBouton("Ajouter");
        JButton btnRetirer = creerBouton("Retirer");
        JButton btnRetour  = creerBouton("Retour");

        btnAjouter.addActionListener(e -> menuAjoutMorceau(utilisateur, playlist, rafraichir));

        btnRetirer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || playlist.getMorceaux().isEmpty()) { afficherInfo("Sélectionnez un morceau."); return; }
            if (playlistController.retirerMorceau(playlist, idx)) rafraichir.run();
            else afficherInfo("Impossible.");
        });

        btnRetour.addActionListener(e -> menuPlaylists(utilisateur));

        JPanel nord = new JPanel(new GridLayout(2, 1));
        nord.add(new JLabel(playlist.getNom(), SwingConstants.CENTER));
        nord.add(labelDuree);

        changerContenu(nord, creerScroll(liste), creerPanelBoutons(btnAjouter, btnRetirer, btnRetour));
    }

    private void menuAjoutMorceau(Utilisateur utilisateur, Playlist playlist, Runnable rafraichir) {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        if (morceaux.isEmpty()) { afficherInfo("Catalogue vide."); return; }

        DefaultListModel<String> modele = new DefaultListModel<>();
        morceaux.forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));
        JList<String> liste = creerListe(modele);

        JButton btnAjouter = creerBouton("Ajouter");
        JButton btnRetour  = creerBouton("Retour");

        btnAjouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0) { afficherInfo("Sélectionnez un morceau."); return; }
            if (playlistController.ajouterMorceau(playlist, morceaux.get(idx))) {
                rafraichir.run();
                afficherInfo("Ajouté !");
                menuPlaylist(utilisateur, playlist);
            } else afficherInfo("Impossible (déjà présent ?).");
        });

        btnRetour.addActionListener(e -> menuPlaylist(utilisateur, playlist));

        changerContenu(new JLabel("Ajouter un morceau", SwingConstants.CENTER), creerScroll(liste), creerPanelBoutons(btnAjouter, btnRetour));
    }

    
    // Historique
    private void menuHistorique(Abonne abonne) {
        DefaultListModel<String> modele = new DefaultListModel<>();
        List<EntreeHistorique> entrees = abonne.getHistorique().getEntrees();
        if (entrees.isEmpty()) modele.addElement("(historique vide)");
        else for (int i = 0; i < entrees.size(); i++)
            modele.addElement((i + 1) + ". " + entrees.get(i));

        JList<String> liste = creerListe(modele);
        JButton btnRetour = creerBouton("Retour");
        btnRetour.addActionListener(e -> menuAbonne(abonne));

        JPanel nord = new JPanel(new GridLayout(2, 1));
        nord.add(new JLabel("Historique", SwingConstants.CENTER));
        nord.add(new JLabel(abonne.getHistorique().getNbEcoutes() + " écoutes au total", SwingConstants.CENTER));

        changerContenu(nord, creerScroll(liste), creerPanelBoutons(btnRetour));
    }

    
    // fenetre admin
    private void menuGererCatalogue() {
        DefaultListModel<String> modele = new DefaultListModel<>();
        List<Morceau>[] morceauxAffiches = new List[]{List.of()};

        Runnable rafraichir = () -> {
            modele.clear();
            morceauxAffiches[0] = catalogueController.listerMorceaux();
            if (morceauxAffiches[0].isEmpty()) modele.addElement("(aucun morceau)");
            else morceauxAffiches[0].forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));
        };
        rafraichir.run();

        JList<String> liste = creerListe(modele);
        JButton btnAjouter   = creerBouton("Ajouter");
        JButton btnSupprimer = creerBouton("Supprimer");
        JButton btnRetour    = creerBouton("Retour");

        btnAjouter.addActionListener(e -> {
            JTextField champTitre   = new JTextField();
            JTextField champArtiste = new JTextField();
            JTextField champDuree   = new JTextField();
            Object[] champs = {"Titre :", champTitre, "Artiste :", champArtiste, "Durée (secondes) :", champDuree};
            if (JOptionPane.showConfirmDialog(this, champs, "Ajouter un morceau",
                    JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
            try {
                int duree = Integer.parseInt(champDuree.getText().trim());
                Artiste artiste = new Artiste(champArtiste.getText().trim(), "");
                catalogueController.ajouterMusique(new Morceau(champTitre.getText().trim(), duree, artiste));
                rafraichir.run();
                afficherInfo("Morceau ajouté !");
            } catch (NumberFormatException ex) {
                afficherInfo("Durée invalide.");
            } catch (IllegalArgumentException ex) {
                afficherInfo(ex.getMessage());
            }
        });

        btnSupprimer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceauxAffiches[0].isEmpty()) {
                afficherInfo("Sélectionnez un morceau.");
                return;
            }
            Morceau morceau = morceauxAffiches[0].get(idx);
            if (!demanderConfirmation("Supprimer le morceau \"" + morceau.getTitre() + "\" ?")) {
                return;
            }
            if (catalogueController.supprimerMusique(morceau)) {
                rafraichir.run();
                afficherInfo("Morceau supprimé.");
            } else {
                afficherInfo("Suppression impossible.");
            }
        });

        btnRetour.addActionListener(e -> menuAdmin());

        changerContenu(new JLabel("Gestion du catalogue", SwingConstants.CENTER), creerScroll(liste), creerPanelBoutons(btnAjouter, btnSupprimer, btnRetour));
    }

    private void menuGererAbonnes() {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);
        List<Utilisateur>[] utilisateursAffiches = new List[]{List.of()};

        Runnable rafraichir = () -> {
            modele.clear();
            utilisateursAffiches[0] = utilisateurController.listerUtilisateursGerables();
            if (utilisateursAffiches[0].isEmpty()) modele.addElement("(aucun utilisateur)");
            else utilisateursAffiches[0].forEach(u -> {
                String statut = (u instanceof Abonne a) ? (a.isAbonnementActif() ? " actif" : " suspendu") : "";
                modele.addElement(u.getLogin() + " (" + u.getClass().getSimpleName() + ")" + statut);
            });
        };
        rafraichir.run();

        JButton btnSuspendre = creerBouton("Suspendre");
        JButton btnSupprimer = creerBouton("Supprimer");
        JButton btnRetour    = creerBouton("Retour");
        Runnable mettreAJourBoutonSuspension = () -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || idx >= utilisateursAffiches[0].size()) {
                btnSuspendre.setText("Suspendre");
                return;
            }
            Utilisateur utilisateur = utilisateursAffiches[0].get(idx);
            if (utilisateur instanceof Abonne abonne && !abonne.isAbonnementActif()) {
                btnSuspendre.setText("Réactiver");
            } else {
                btnSuspendre.setText("Suspendre");
            }
        };
        liste.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mettreAJourBoutonSuspension.run();
            }
        });

        btnSuspendre.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0) { afficherInfo("Sélectionnez un utilisateur."); return; }
            Utilisateur u = utilisateursAffiches[0].get(idx);
            if (u instanceof Abonne abonne) {
                boolean actif = abonne.isAbonnementActif();
                abonne.setAbonnementActif(!actif);
                rafraichir.run();
                mettreAJourBoutonSuspension.run();
                afficherInfo(actif ? "Compte suspendu." : "Suspension retirée.");
            } else afficherInfo("Impossible (pas un abonné).");
        });

        btnSupprimer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || utilisateursAffiches[0].isEmpty()) {
                afficherInfo("Sélectionnez un utilisateur.");
                return;
            }
            Utilisateur utilisateur = utilisateursAffiches[0].get(idx);
            if (!demanderConfirmation("Supprimer l'utilisateur \"" + utilisateur.getLogin() + "\" ?")) {
                return;
            }
            if (utilisateurController.supprimerInscrit(utilisateur.getLogin())) {
                rafraichir.run();
                mettreAJourBoutonSuspension.run();
                afficherInfo("Utilisateur supprimé.");
            } else {
                afficherInfo("Suppression impossible.");
            }
        });

        btnRetour.addActionListener(e -> menuAdmin());

        changerContenu(new JLabel("Gestion des abonnés", SwingConstants.CENTER), creerScroll(liste), creerPanelBoutons(btnSuspendre, btnSupprimer, btnRetour));
    }

    private void menuStatistiques() {
        Statistique stats = new Statistique(catalogueController.getCatalogue());
        Morceau morceauTop = stats.getMorceauLePlusEcoute();

        String contenu =
                "Utilisateurs    : " + utilisateurController.listerUtilisateurs().size() + "\n\n" +
                        "Morceaux        : " + catalogueController.listerMorceaux().size() + "\n" +
                        "Albums          : " + catalogueController.listerAlbums().size() + "\n" +
                        "Artistes        : " + catalogueController.listerArtistes().size() + "\n\n" +
                        "Ecoutes totales : " + stats.getNbEcoutesTotal() + "\n" +
                        "Durée moyenne   : " + String.format("%.0f sec", stats.getDureeMoyenneMorceaux()) + "\n" +
                        "Top morceau     : " + (morceauTop != null ? morceauTop.getTitre() + " (" + morceauTop.getNbEcoutes() + " écoutes)" : "aucun");

        JTextArea zoneTexte = new JTextArea(contenu);
        zoneTexte.setEditable(false);

        JButton btnRetour = creerBouton("Retour");
        btnRetour.addActionListener(e -> menuAdmin());

        changerContenu(new JLabel("Statistiques", SwingConstants.CENTER), zoneTexte, creerPanelBoutons(btnRetour));
    }
}
