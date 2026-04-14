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

    // =========================
    // Helpers
    // =========================

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

    private String formaterDuree(int secondes) {
        return secondes / 60 + ":" + String.format("%02d", secondes % 60);
    }

    private JButton creerBoutonDeconnexion() {

        JButton bouton = creerBoutonRouge("Déconnexion");
        bouton.addActionListener(e -> { utilisateurController.deconnexion(); afficherAccueil(); });
        return bouton;
    }

    // =========================
    // Accueil
    // =========================

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

    // =========================
    // Connexion / Inscription
    // =========================

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

    // =========================
    // Routage
    // =========================

    private void afficherMenuSelonRole() {
        if (utilisateurController.estAdmin()) {
            menuAdmin();
        } else if (utilisateurController.estAbonneActif()) {
            menuAbonne((Abonne) utilisateurController.getUtilisateurCourant());
        } else {
            menuVisiteur();
        }
    }

    // =========================
    // Menus principaux
    // =========================

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
        JButton btnRecherche  = creerBouton("Rechercher");
        JButton btnPlaylists  = creerBouton("Mes playlists");
        JButton btnHistorique = creerBouton("Historique");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche, btnPlaylists, btnHistorique}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b);
            centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
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
        JButton btnGererCat  = creerBouton("Gérer le catalogue");
        JButton btnGererAbo  = creerBouton("Gérer les abonnés");
        JButton btnStats     = creerBouton("Statistiques");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche, btnGererCat, btnGererAbo, btnStats}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b);
            centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnRecherche.addActionListener(e -> menuRecherche());
        btnGererCat.addActionListener(e -> menuGererCatalogue());
        btnGererAbo.addActionListener(e -> menuGererAbonnes());
        btnStats.addActionListener(e -> menuStatistiques());

        changerContenu(new JLabel("Menu Admin", SwingConstants.CENTER), centre, creerPanelBoutons(creerBoutonDeconnexion()));
    }

    // =========================
    // Catalogue
    // =========================

    private void menuCatalogue() {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        DefaultListModel<String> modele = new DefaultListModel<>();
        morceaux.forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste() + " | " + m.getNbEcoutes() + " écoutes"));
        if (morceaux.isEmpty()) modele.addElement("(catalogue vide)");

        JList<String> liste = creerListe(modele);

        JButton btnEcouter = creerBouton("Écouter");
        JButton btnRetour  = creerBouton("Retour");

        btnEcouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceaux.isEmpty()) { afficherInfo("Sélectionnez un morceau."); return; }
            Morceau m = morceaux.get(idx);
            try {
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                afficherBarreProgression(m);
                modele.set(idx, m.getTitre() + " - " + m.getArtiste() + " | " + m.getNbEcoutes() + " écoutes");
            } catch (RuntimeException ex) {
                afficherInfo(ex.getMessage());
            }
        });
        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        changerContenu(new JLabel("Catalogue", SwingConstants.CENTER), creerScroll(liste), creerPanelBoutons(btnEcouter, btnRetour));
    }

    // =========================
    // Recherche
    // =========================

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
            if (idx >= 0 && !resultat[0].isEmpty()) {
                Morceau m = resultat[0].get(idx);
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                afficherInfo("Lecture de : " + m.getTitre());
            } else afficherInfo("Sélectionnez un morceau.");
        });

        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JPanel nord = new JPanel(new BorderLayout(10, 10));
        nord.add(new JLabel("Recherche", SwingConstants.CENTER), BorderLayout.NORTH);
        nord.add(champRecherche, BorderLayout.CENTER);
        nord.add(btnChercher, BorderLayout.EAST);

        changerContenu(nord, creerScroll(liste), creerPanelBoutons(btnEcouter, btnRetour));
    }

    private void afficherBarreProgression(Morceau morceau) {
        JDialog dialog = new JDialog(this, "Lecture en cours", true);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(this);

        JLabel label = new JLabel("▶ " + morceau.getTitre() + " - " + morceau.getArtiste(), SwingConstants.CENTER);
        JProgressBar barre = new JProgressBar(0, 100);
        barre.setStringPainted(true);

        dialog.add(label, BorderLayout.NORTH);
        dialog.add(barre, BorderLayout.CENTER);

        // Timer qui avance la barre sur 3 secondes
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
    // =========================
    // Playlists
    // =========================

    private void menuPlaylists(Abonne abonne) {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);

        Runnable rafraichir = () -> {
            modele.clear();
            List<Playlist> playlists = abonne.getPlaylists();
            if (playlists.isEmpty()) modele.addElement("(aucune playlist)");
            else playlists.forEach(p -> modele.addElement(p.getNom() + " | " + p.getMorceaux().size() + " morceaux - " + formaterDuree(p.getDureeTotale())));
        };
        rafraichir.run();

        JTextField champNom  = new JTextField(12);
        JButton btnCreer     = creerBouton("Créer");
        JButton btnRenommer  = creerBouton("Renommer");
        JButton btnSupprimer = creerBouton("Supprimer");
        JButton btnOuvrir    = creerBouton("Ouvrir");
        JButton btnRetour    = creerBouton("Retour");

        btnCreer.addActionListener(e -> {
            String nom = champNom.getText().trim();
            if (nom.isEmpty()) { afficherInfo("Entrez un nom."); return; }
            Playlist p = playlistController.creerPlaylist(abonne, nom);
            if (p == null) afficherInfo("Création impossible.");
            else { champNom.setText(""); rafraichir.run(); }
        });

        btnRenommer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            String nom = champNom.getText().trim();
            if (idx < 0 || abonne.getPlaylists().isEmpty()) { afficherInfo("Sélectionnez une playlist."); return; }
            if (nom.isEmpty()) { afficherInfo("Entrez un nouveau nom."); return; }
            if (playlistController.renommerPlaylist(abonne, idx, nom)) { champNom.setText(""); rafraichir.run(); }
            else afficherInfo("Impossible.");
        });

        btnSupprimer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || abonne.getPlaylists().isEmpty()) { afficherInfo("Sélectionnez une playlist."); return; }
            if (demanderConfirmation("Supprimer cette playlist ?"))
                if (playlistController.supprimerPlaylist(abonne, idx)) rafraichir.run();
        });

        btnOuvrir.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || abonne.getPlaylists().isEmpty()) { afficherInfo("Sélectionnez une playlist."); return; }
            menuPlaylist(abonne, abonne.getPlaylists().get(idx));
        });

        btnRetour.addActionListener(e -> menuAbonne(abonne));

        JPanel nord = new JPanel(new BorderLayout(10, 5));
        nord.add(new JLabel("Mes Playlists", SwingConstants.CENTER), BorderLayout.NORTH);
        JPanel champPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        champPanel.add(new JLabel("Nom :"));
        champPanel.add(champNom);
        nord.add(champPanel, BorderLayout.CENTER);

        changerContenu(nord, creerScroll(liste), creerPanelBoutons(btnCreer, btnRenommer, btnSupprimer, btnOuvrir, btnRetour));
    }

    private void menuPlaylist(Abonne abonne, Playlist playlist) {
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

        btnAjouter.addActionListener(e -> menuAjoutMorceau(abonne, playlist, rafraichir));

        btnRetirer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || playlist.getMorceaux().isEmpty()) { afficherInfo("Sélectionnez un morceau."); return; }
            if (playlistController.retirerMorceau(playlist, idx)) rafraichir.run();
            else afficherInfo("Impossible.");
        });

        btnRetour.addActionListener(e -> menuPlaylists(abonne));

        JPanel nord = new JPanel(new GridLayout(2, 1));
        nord.add(new JLabel(playlist.getNom(), SwingConstants.CENTER));
        nord.add(labelDuree);

        changerContenu(nord, creerScroll(liste), creerPanelBoutons(btnAjouter, btnRetirer, btnRetour));
    }

    private void menuAjoutMorceau(Abonne abonne, Playlist playlist, Runnable rafraichir) {
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
                menuPlaylist(abonne, playlist);
            } else afficherInfo("Impossible (déjà présent ?).");
        });

        btnRetour.addActionListener(e -> menuPlaylist(abonne, playlist));

        changerContenu(new JLabel("Ajouter un morceau", SwingConstants.CENTER), creerScroll(liste), creerPanelBoutons(btnAjouter, btnRetour));
    }

    // =========================
    // Historique
    // =========================

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

    // =========================
    // Administration
    // =========================

    private void menuGererCatalogue() {
        DefaultListModel<String> modele = new DefaultListModel<>();

        Runnable rafraichir = () -> {
            modele.clear();
            List<Morceau> morceaux = catalogueController.listerMorceaux();
            if (morceaux.isEmpty()) modele.addElement("(aucun morceau)");
            else morceaux.forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));
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
                catalogueController.getCatalogue().ajouterMorceau(
                        new Morceau(champTitre.getText().trim(), duree, artiste));
                rafraichir.run();
                afficherInfo("Morceau ajouté !");
            } catch (NumberFormatException ex) { afficherInfo("Durée invalide."); }
        });

        btnRetour.addActionListener(e -> menuAdmin());

        changerContenu(new JLabel("Gestion du catalogue", SwingConstants.CENTER), creerScroll(liste), creerPanelBoutons(btnAjouter, btnSupprimer, btnRetour));
    }

    private void menuGererAbonnes() {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);

        Runnable rafraichir = () -> {
            modele.clear();
            List<Utilisateur> utilisateurs = utilisateurController.listerUtilisateurs();
            if (utilisateurs.isEmpty()) modele.addElement("(aucun utilisateur)");
            else utilisateurs.forEach(u -> {
                String statut = (u instanceof Abonne a) ? (a.isAbonnementActif() ? " actif" : " suspendu") : "";
                modele.addElement(u.getLogin() + " (" + u.getClass().getSimpleName() + ")" + statut);
            });
        };
        rafraichir.run();

        JButton btnSuspendre = creerBouton("Suspendre");
        JButton btnSupprimer = creerBouton("Supprimer");
        JButton btnRetour    = creerBouton("Retour");

        btnSuspendre.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            List<Utilisateur> utilisateurs = utilisateurController.listerUtilisateurs();
            if (idx < 0) { afficherInfo("Sélectionnez un utilisateur."); return; }
            Utilisateur u = utilisateurs.get(idx);
            if (u instanceof Abonne abonne) {
                abonne.setAbonnementActif(false);
                rafraichir.run();
                afficherInfo("Compte suspendu.");
            } else afficherInfo("Impossible (pas un abonné).");
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
                        "Durée moyenne   : " + String.format("%.0f sec", stats.getDureeMoyenneMorceaux()) + "\n" +
                        "Top morceau     : " + (morceauTop != null ? morceauTop.getTitre() + " (" + morceauTop.getNbEcoutes() + " écoutes)" : "aucun");

        JTextArea zoneTexte = new JTextArea(contenu);
        zoneTexte.setEditable(false);

        JButton btnRetour = creerBouton("Retour");
        btnRetour.addActionListener(e -> menuAdmin());

        changerContenu(new JLabel("Statistiques", SwingConstants.CENTER), zoneTexte, creerPanelBoutons(btnRetour));
    }
}