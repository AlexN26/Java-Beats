package View;

import Controller.CatalogueController;
import Controller.PlaylistController;
import Controller.UtilisateurController;
import Model.*;
import Model.User.Abonne;
import Model.User.Utilisateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class VueGraphique extends JFrame {

    private final UtilisateurController utilisateurController;
    private final CatalogueController catalogueController;
    private final PlaylistController playlistController;

    // Couleurs du thème
    private static final Color BG        = new Color(200, 200, 200);
    private static final Color ACCENT    = new Color(29, 185, 84);
    private static final Color TEXTE     = new Color(0, 0, 0);
    private static final Color DANGER    = new Color(220, 53, 69);

    public VueGraphique(UtilisateurController u, CatalogueController c, PlaylistController p) {
        this.utilisateurController = u;
        this.catalogueController = c;
        this.playlistController = p;
        setTitle("JavaBeats");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);
        afficherAccueil();
        setVisible(true);
    }

    public void run() { setVisible(true); }

    // =========================
    // Helpers UI
    // =========================

    private JButton btn(String texte, Color couleur) {
        JButton b = new JButton(texte);
        b.setBackground(couleur);
        b.setForeground(TEXTE);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(220, 38));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(couleur.brighter()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { b.setBackground(couleur); }
        });
        return b;
    }

    private JButton btnVert(String texte)   { return btn(texte, ACCENT); }
    private JButton btnGris(String texte)   { return btn(texte, new Color(60, 60, 60)); }
    private JButton btnRouge(String texte)  { return btn(texte, DANGER); }

    private JLabel titre(String texte) {
        JLabel l = new JLabel(texte, SwingConstants.CENTER);
        l.setForeground(ACCENT);
        return l;
    }

    private JLabel sousTitre(String texte) {
        JLabel l = new JLabel(texte, SwingConstants.CENTER);
        l.setForeground(TEXTE);
        return l;
    }

    private JList<String> liste(DefaultListModel<String> model) {
        JList<String> l = new JList<>(model);
        l.setBackground(Color.WHITE);
        l.setForeground(TEXTE);
        l.setSelectionBackground(ACCENT);
        l.setSelectionForeground(Color.BLACK);
        l.setFixedCellHeight(28);
        l.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return l;
    }

    private JScrollPane scroll(JList<String> l) {
        JScrollPane s = new JScrollPane(l);
        s.getViewport().setBackground(Color.WHITE);
        return s;
    }

    private JPanel panelBas(JButton... boutons) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        p.setBackground(BG);
        for (JButton b : boutons) p.add(b);
        return p;
    }

    private JPanel panelCentre() {
        JPanel p = new JPanel();
        p.setBackground(BG);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        return p;
    }

    private void changer(JPanel nord, JComponent centre, JPanel sud) {
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(0, 5));
        getContentPane().setBackground(BG);
        if (nord   != null) add(nord,   BorderLayout.NORTH);
        if (centre != null) add(centre, BorderLayout.CENTER);
        if (sud    != null) add(sud,    BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void info(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean confirmer(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "Confirmation",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private String formaterDuree(int s) {
        return s / 60 + ":" + String.format("%02d", s % 60);
    }

    // =========================
    // Accueil
    // =========================

    private void afficherAccueil() {
        JPanel nord = new JPanel(new GridLayout(3, 1));
        nord.setBackground(BG);
        nord.add(titre("🎵 JavaBeats"));
        nord.add(sousTitre("Votre musique, votre façon"));
        nord.add(sousTitre(""));

        JPanel centre = panelCentre();
        JButton btnVisiteur    = btnGris("Continuer en visiteur");
        JButton btnConnexion   = btnVert("Se connecter");
        JButton btnInscription = btnGris("S'inscrire");
        JButton btnQuitter     = btnRouge("Quitter");

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

        changer(nord, centre, null);
    }

    // =========================
    // Connexion / Inscription
    // =========================

    private void ecranConnexion() {
        JTextField loginField = new JTextField();
        JPasswordField mdpField = new JPasswordField();
        loginField.setBackground(Color.WHITE); loginField.setForeground(TEXTE);
        mdpField.setBackground(Color.WHITE);   mdpField.setForeground(TEXTE);

        Object[] msg = {"Login :", loginField, "Mot de passe :", mdpField};
        int res = JOptionPane.showConfirmDialog(this, msg, "Connexion", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        if (!utilisateurController.connexion(loginField.getText(), new String(mdpField.getPassword()))) {
            info("Login ou mot de passe incorrect.");
        } else {
            afficherMenuSelonRole();
        }
    }

    private void ecranInscription() {
        JTextField loginField = new JTextField();
        JPasswordField mdpField = new JPasswordField();
        JTextField emailField = new JTextField();

        Object[] msg = {"Login :", loginField, "Mot de passe :", mdpField, "Email (optionnel) :", emailField};
        int res = JOptionPane.showConfirmDialog(this, msg, "Inscription", JOptionPane.OK_CANCEL_OPTION);
        if (res != JOptionPane.OK_OPTION) return;

        if (!utilisateurController.inscriptionAbonne(loginField.getText(),
                new String(mdpField.getPassword()), emailField.getText())) {
            info("Login déjà utilisé ou informations invalides.");
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

    private JPanel nordMenu(String texte) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBackground(BG);
        p.add(titre(texte));
        p.add(sousTitre("Connecté: " + utilisateurController.getUtilisateurCourant()));
        return p;
    }

    private JButton btnDeconnexion() {
        JButton b = btnRouge("Déconnexion");
        b.addActionListener(e -> { utilisateurController.deconnexion(); afficherAccueil(); });
        return b;
    }

    // =========================
    // Menus principaux
    // =========================

    private void menuVisiteur() {
        JPanel centre = panelCentre();
        JButton btnCatalogue = btnVert("Catalogue");
        JButton btnRecherche = btnGris("Rechercher");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b); centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnRecherche.addActionListener(e -> menuRecherche());

        changer(nordMenu("🎵 Menu Visiteur"), centre, panelBas(btnDeconnexion()));
    }

    private void menuAbonne(Abonne abonne) {
        JPanel centre = panelCentre();
        JButton btnCatalogue  = btnVert("Catalogue");
        JButton btnRecherche  = btnGris("Rechercher");
        JButton btnPlaylists  = btnGris("Mes playlists");
        JButton btnHistorique = btnGris("Historique");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche, btnPlaylists, btnHistorique}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b); centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnRecherche.addActionListener(e -> menuRecherche());
        btnPlaylists.addActionListener(e -> menuPlaylists(abonne));
        btnHistorique.addActionListener(e -> menuHistorique(abonne));

        changer(nordMenu("🎵 Menu Abonné"), centre, panelBas(btnDeconnexion()));
    }

    private void menuAdmin() {
        JPanel centre = panelCentre();
        JButton btnCatalogue  = btnVert("Catalogue");
        JButton btnRecherche  = btnGris("Rechercher");
        JButton btnGererCat   = btnGris("Gérer le catalogue");
        JButton btnGererAbo   = btnGris("Gérer les abonnés");
        JButton btnStats      = btnGris("Statistiques");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche, btnGererCat, btnGererAbo, btnStats}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 38));
            centre.add(b); centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnRecherche.addActionListener(e -> menuRecherche());
        btnGererCat.addActionListener(e -> menuGererCatalogue());
        btnGererAbo.addActionListener(e -> menuGererAbonnes());
        btnStats.addActionListener(e -> menuStatistiques());

        changer(nordMenu("⚙️ Menu Admin"), centre, panelBas(btnDeconnexion()));
    }

    // =========================
    // Catalogue
    // =========================

    private void menuCatalogue() {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        DefaultListModel<String> model = new DefaultListModel<>();
        morceaux.forEach(m -> model.addElement("  " + m.getTitre() + "  —  " + m.getArtiste() + "  |  " + m.getNbEcoutes() + " écoutes"));
        if (morceaux.isEmpty()) model.addElement("  (catalogue vide)");

        JList<String> liste = liste(model);

        JButton btnEcouter = btnVert("▶ Écouter");
        JButton btnRetour  = btnGris("← Retour");

        btnEcouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceaux.isEmpty()) { info("Sélectionnez un morceau."); return; }
            Morceau m = morceaux.get(idx);
            catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
            info("▶ Lecture de : " + m.getTitre());
            model.set(idx, "  " + m.getTitre() + "  —  " + m.getArtiste() + "  |  " + m.getNbEcoutes() + " écoutes");
        });
        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JPanel nord = new JPanel(new GridLayout(1,1));
        nord.setBackground(BG);
        nord.add(titre("🎵 Catalogue"));

        changer(nord, scroll(liste), panelBas(btnEcouter, btnRetour));
    }

    // =========================
    // Recherche
    // =========================

    private void menuRecherche() {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> liste = liste(model);
        List<Morceau>[] resultat = new List[]{List.of()};

        JTextField champ = new JTextField();
        champ.setBackground(Color.WHITE);
        champ.setForeground(TEXTE);
        champ.setCaretColor(TEXTE);
        JButton btnChercher = btnVert("🔍 Chercher");
        JButton btnEcouter  = btnGris("▶ Écouter");
        JButton btnRetour   = btnGris("← Retour");

        btnChercher.addActionListener(e -> {
            resultat[0] = catalogueController.rechercherMorceau(champ.getText());
            model.clear();
            if (resultat[0].isEmpty()) model.addElement("  (aucun résultat)");
            else resultat[0].forEach(m -> model.addElement("  " + m.getTitre() + "  —  " + m.getArtiste()));
        });

        btnEcouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx >= 0 && !resultat[0].isEmpty()) {
                Morceau m = resultat[0].get(idx);
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                info("▶ Lecture de : " + m.getTitre());
            } else info("Sélectionnez un morceau.");
        });

        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JPanel nord = new JPanel(new BorderLayout(10, 10));
        nord.setBackground(BG);
        nord.add(titre("🔍 Recherche"), BorderLayout.NORTH);
        nord.add(champ, BorderLayout.CENTER);
        nord.add(btnChercher, BorderLayout.EAST);

        changer(nord, scroll(liste), panelBas(btnEcouter, btnRetour));
    }

    // =========================
    // Playlists
    // =========================

    private void menuPlaylists(Abonne abonne) {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> liste = liste(model);

        Runnable rafraichir = () -> {
            model.clear();
            List<Playlist> pls = abonne.getPlaylists();
            if (pls.isEmpty()) model.addElement("  (aucune playlist)");
            else pls.forEach(p -> model.addElement("  " + p.getNom() + "  |  "
                    + p.getMorceaux().size() + " morceaux  —  " + formaterDuree(p.getDureeTotale())));
        };
        rafraichir.run();

        JTextField champNom = new JTextField(12);
        champNom.setBackground(Color.WHITE); champNom.setForeground(TEXTE);
        champNom.setCaretColor(TEXTE);

        JButton btnCreer     = btnVert("+ Créer");
        JButton btnRenommer  = btnGris("✏ Renommer");
        JButton btnSupprimer = btnRouge("✕ Supprimer");
        JButton btnOuvrir    = btnVert("→ Ouvrir");
        JButton btnRetour    = btnGris("← Retour");

        btnCreer.addActionListener(e -> {
            String nom = champNom.getText().trim();
            if (nom.isEmpty()) { info("Entrez un nom."); return; }
            Playlist p = playlistController.creerPlaylist(abonne, nom);
            if (p == null) info("Création impossible.");
            else { champNom.setText(""); rafraichir.run(); }
        });

        btnRenommer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            String nom = champNom.getText().trim();
            if (idx < 0 || abonne.getPlaylists().isEmpty()) { info("Sélectionnez une playlist."); return; }
            if (nom.isEmpty()) { info("Entrez un nouveau nom."); return; }
            if (playlistController.renommerPlaylist(abonne, idx, nom)) { champNom.setText(""); rafraichir.run(); }
            else info("Impossible.");
        });

        btnSupprimer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || abonne.getPlaylists().isEmpty()) { info("Sélectionnez une playlist."); return; }
            if (confirmer("Supprimer cette playlist ?"))
                if (playlistController.supprimerPlaylist(abonne, idx)) rafraichir.run();
        });

        btnOuvrir.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || abonne.getPlaylists().isEmpty()) { info("Sélectionnez une playlist."); return; }
            menuPlaylist(abonne, abonne.getPlaylists().get(idx));
        });

        btnRetour.addActionListener(e -> menuAbonne(abonne));

        JPanel nord = new JPanel(new BorderLayout(10, 5));
        nord.setBackground(BG);
        nord.add(titre("🎶 Mes Playlists"), BorderLayout.NORTH);
        JPanel champPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        champPanel.setBackground(BG);
        champPanel.add(new JLabel("Nom : ") {{ setForeground(TEXTE); }});
        champPanel.add(champNom);
        nord.add(champPanel, BorderLayout.CENTER);

        changer(nord, scroll(liste), panelBas(btnCreer, btnRenommer, btnSupprimer, btnOuvrir, btnRetour));
    }

    private void menuPlaylist(Abonne abonne, Playlist playlist) {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> liste = liste(model);
        JLabel labelDuree = sousTitre("Durée : " + formaterDuree(playlist.getDureeTotale()));

        Runnable rafraichir = () -> {
            model.clear();
            List<Morceau> mors = playlist.getMorceaux();
            if (mors.isEmpty()) model.addElement("  (playlist vide)");
            else mors.forEach(m -> model.addElement("  " + m.getTitre() + "  —  " + m.getArtiste()));
            labelDuree.setText("Durée : " + formaterDuree(playlist.getDureeTotale()));
        };
        rafraichir.run();

        JButton btnAjouter  = btnVert("+ Ajouter");
        JButton btnRetirer  = btnRouge("✕ Retirer");
        JButton btnRetour   = btnGris("← Retour");

        btnAjouter.addActionListener(e -> menuAjoutMorceau(abonne, playlist, rafraichir));

        btnRetirer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || playlist.getMorceaux().isEmpty()) { info("Sélectionnez un morceau."); return; }
            if (playlistController.retirerMorceau(playlist, idx)) rafraichir.run();
            else info("Impossible.");
        });

        btnRetour.addActionListener(e -> menuPlaylists(abonne));

        JPanel nord = new JPanel(new GridLayout(2, 1));
        nord.setBackground(BG);
        nord.add(titre("🎶 " + playlist.getNom()));
        nord.add(labelDuree);

        changer(nord, scroll(liste), panelBas(btnAjouter, btnRetirer, btnRetour));
    }

    private void menuAjoutMorceau(Abonne abonne, Playlist playlist, Runnable callbackRafraichir) {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        if (morceaux.isEmpty()) { info("Catalogue vide."); return; }

        DefaultListModel<String> model = new DefaultListModel<>();
        morceaux.forEach(m -> model.addElement("  " + m.getTitre() + "  —  " + m.getArtiste()));
        JList<String> liste = liste(model);

        JButton btnAjouter = btnVert("+ Ajouter");
        JButton btnRetour  = btnGris("← Retour");

        btnAjouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0) { info("Sélectionnez un morceau."); return; }
            if (playlistController.ajouterMorceau(playlist, morceaux.get(idx))) {
                callbackRafraichir.run();
                info("Ajouté !");
                menuPlaylist(abonne, playlist);
            } else info("Impossible (déjà présent ?).");
        });

        btnRetour.addActionListener(e -> menuPlaylist(abonne, playlist));

        JPanel nord = new JPanel(new GridLayout(1, 1));
        nord.setBackground(BG);
        nord.add(titre("+ Ajouter un morceau"));

        changer(nord, scroll(liste), panelBas(btnAjouter, btnRetour));
    }

    // =========================
    // Historique
    // =========================

    private void menuHistorique(Abonne abonne) {
        DefaultListModel<String> model = new DefaultListModel<>();
        List<EntreeHistorique> entrees = abonne.getHistorique().getEntrees();
        if (entrees.isEmpty()) model.addElement("  (historique vide)");
        else for (int i = 0; i < entrees.size(); i++)
            model.addElement("  " + (i + 1) + ".  " + entrees.get(i));

        JList<String> liste = liste(model);
        JButton btnRetour = btnGris("← Retour");
        btnRetour.addActionListener(e -> menuAbonne(abonne));

        JPanel nord = new JPanel(new GridLayout(2, 1));
        nord.setBackground(BG);
        nord.add(titre("📜 Historique"));
        nord.add(sousTitre(abonne.getHistorique().getNbEcoutes() + " écoutes au total"));

        changer(nord, scroll(liste), panelBas(btnRetour));
    }

    // =========================
    // Administration
    // =========================

    private void menuGererCatalogue() {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        DefaultListModel<String> model = new DefaultListModel<>();

        Runnable rafraichir = () -> {
            model.clear();
            List<Morceau> liste2 = catalogueController.listerMorceaux();
            if (liste2.isEmpty()) model.addElement("  (aucun morceau)");
            else liste2.forEach(m -> model.addElement("  " + m.getTitre() + "  —  " + m.getArtiste()));
        };
        rafraichir.run();

        JList<String> liste = liste(model);
        JButton btnAjouter   = btnVert("+ Ajouter");
        JButton btnSupprimer = btnRouge("✕ Supprimer");
        JButton btnRetour    = btnGris("← Retour");

        btnAjouter.addActionListener(e -> {
            JTextField titreF   = new JTextField();
            JTextField artisteF = new JTextField();
            JTextField dureeF   = new JTextField();
            Object[] msg = {"Titre :", titreF, "Artiste :", artisteF, "Durée (secondes) :", dureeF};
            if (JOptionPane.showConfirmDialog(this, msg, "Ajouter un morceau",
                    JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
            try {
                int duree = Integer.parseInt(dureeF.getText().trim());
                Artiste artiste = new Artiste(artisteF.getText().trim(), "");
                catalogueController.getCatalogue().ajouterMorceau(
                        new Morceau(titreF.getText().trim(), duree, artiste));
                rafraichir.run();
                info("Morceau ajouté !");
            } catch (NumberFormatException ex) { info("Durée invalide."); }
        });

        btnRetour.addActionListener(e -> menuAdmin());

        JPanel nord = new JPanel(new GridLayout(1, 1));
        nord.setBackground(BG);
        nord.add(titre("⚙️ Gestion du catalogue"));

        changer(nord, scroll(liste), panelBas(btnAjouter, btnSupprimer, btnRetour));
    }

    private void menuGererAbonnes() {
        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> liste = liste(model);

        Runnable rafraichir = () -> {
            model.clear();
            List<Utilisateur> liste2 = utilisateurController.listerUtilisateurs();
            if (liste2.isEmpty()) model.addElement("  (aucun utilisateur)");
            else liste2.forEach(u -> {
                String statut = (u instanceof Abonne a) ? (a.isAbonnementActif() ? " ✓ actif" : " ✗ suspendu") : "";
                model.addElement("  " + u.getLogin() + "  (" + u.getClass().getSimpleName() + ")" + statut);
            });
        };
        rafraichir.run();

        JButton btnSuspendre = btnGris("⏸ Suspendre");
        JButton btnSupprimer = btnRouge("✕ Supprimer");
        JButton btnRetour    = btnGris("← Retour");

        btnSuspendre.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            List<Utilisateur> current = utilisateurController.listerUtilisateurs();
            if (idx < 0) { info("Sélectionnez un utilisateur."); return; }
            Utilisateur u = current.get(idx);
            if (u instanceof Abonne abonne) {
                abonne.setAbonnementActif(false);
                rafraichir.run();
                info("Compte suspendu.");
            } else info("Impossible (pas un abonné).");
        });

        btnRetour.addActionListener(e -> menuAdmin());

        JPanel nord = new JPanel(new GridLayout(1, 1));
        nord.setBackground(BG);
        nord.add(titre("👥 Gestion des abonnés"));

        changer(nord, scroll(liste), panelBas(btnSuspendre, btnSupprimer, btnRetour));
    }

    private void menuStatistiques() {
        Statistique stats = new Statistique(catalogueController.getCatalogue());
        Morceau top = stats.getMorceauLePlusEcoute();

        String contenu =
                "  Utilisateurs       : " + utilisateurController.listerUtilisateurs().size() + "\n\n" +
                        "  Morceaux          : " + catalogueController.listerMorceaux().size() + "\n" +
                        "  Albums            : " + catalogueController.listerAlbums().size() + "\n" +
                        "  Artistes          : " + catalogueController.listerArtistes().size() + "\n\n" +
                        "  Durée moyenne     : " + String.format("%.0f sec", stats.getDureeMoyenneMorceaux()) + "\n" +
                        "  Top morceau       : " + (top != null ? top.getTitre() + " (" + top.getNbEcoutes() + " écoutes)" : "aucun");

        JTextArea zone = new JTextArea(contenu);
        zone.setEditable(false);
        zone.setBackground(Color.WHITE);
        zone.setForeground(TEXTE);

        JButton btnRetour = btnGris("← Retour");
        btnRetour.addActionListener(e -> menuAdmin());

        JPanel nord = new JPanel(new GridLayout(1, 1));
        nord.setBackground(BG);
        nord.add(titre("📊 Statistiques"));

        changer(nord, zone, panelBas(btnRetour));
    }
}