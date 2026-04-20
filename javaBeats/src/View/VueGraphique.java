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

/**
 * Interface graphique principale pour l'application JavaBeats
 * Utilise Swing avec images de fond personnalisées pour chaque écran
 */
public class VueGraphique extends JFrame {

    private final UtilisateurController utilisateurController;
    private final CatalogueController catalogueController;
    private final PlaylistController playlistController;

    // Images mappées pour chaque écran
    private static final String[] IMAGES = {
            "Image1.png",  // 0: Accueil
            "Image2.png",  // 1: Connexion/Admin
            "Image3.png",  // 2: Inscription
            "Image4.png",  // 3: Gestion/Ajout
            "Image5.png",  // 4: Catalogue
            "Image6.png",  // 5: Recherche/Historique
            "Image7.png"   // 6: Playlists
    };

    public VueGraphique(UtilisateurController u, CatalogueController c, PlaylistController p) {
        this.utilisateurController = u;
        this.catalogueController = c;
        this.playlistController = p;

        setTitle("JavaBeats");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        afficherAccueil();
        setVisible(true);
    }

    public void run() {
        setVisible(true);
    }

    // ============================================
    // HELPERS - Création de composants
    // ============================================

    private JButton creerBouton(String texte) {
        JButton bouton = new JButton(texte);
        bouton.setPreferredSize(new Dimension(220, 40));
        bouton.setFont(new Font("Arial", Font.PLAIN, 14));
        bouton.setFocusPainted(false);
        bouton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return bouton;
    }

    private JButton creerBoutonRouge(String texte) {
        JButton bouton = creerBouton(texte);
        bouton.setBackground(new Color(220, 40, 60));
        bouton.setForeground(Color.WHITE);
        bouton.setOpaque(true);
        bouton.setBorderPainted(false);
        return bouton;
    }

    private JButton creerBoutonVert(String texte) {
        JButton bouton = creerBouton(texte);
        bouton.setBackground(new Color(76, 175, 80));
        bouton.setForeground(Color.WHITE);
        bouton.setOpaque(true);
        bouton.setBorderPainted(false);
        return bouton;
    }

    private JList<String> creerListe(DefaultListModel<String> modele) {
        JList<String> liste = new JList<>(modele);
        liste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liste.setFixedCellHeight(30);
        liste.setFont(new Font("Arial", Font.PLAIN, 13));
        return liste;
    }

    private JScrollPane creerScroll(JList<String> liste) {
        JScrollPane scroll = new JScrollPane(liste);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        return scroll;
    }

    private JPanel creerPanelBoutons(JButton... boutons) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setOpaque(false);
        for (JButton b : boutons) {
            panel.add(b);
        }
        return panel;
    }

    private JPanel creerPanelBoutonsDeuxLignes(JButton... boutons) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);

        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 8));
        ligne1.setOpaque(false);
        ligne2.setOpaque(false);

        for (int i = 0; i < boutons.length; i++) {
            if (i < 3) {
                ligne1.add(boutons[i]);
            } else {
                ligne2.add(boutons[i]);
            }
        }

        panel.add(ligne1);
        if (boutons.length > 3) {
            panel.add(ligne2);
        }
        return panel;
    }

    private void changerContenu(JComponent nord, JComponent centre, JComponent sud, String imagePath) {
        getContentPane().removeAll();

        // Créer un panneau avec image de fond
        PanelAvecFond fondPanel = new PanelAvecFond(imagePath);
        fondPanel.setLayout(new BorderLayout(0, 8));

        if (nord != null) {
            JPanel panelNord = new JPanel();
            panelNord.setOpaque(false);
            panelNord.add(nord);
            fondPanel.add(panelNord, BorderLayout.NORTH);
        }

        if (centre != null) {
            JPanel panelCentre = new JPanel(new BorderLayout());
            panelCentre.setOpaque(false);
            panelCentre.add(centre, BorderLayout.CENTER);
            fondPanel.add(panelCentre, BorderLayout.CENTER);
        }

        if (sud != null) {
            JPanel panelSud = new JPanel();
            panelSud.setOpaque(false);
            panelSud.add(sud);
            fondPanel.add(panelSud, BorderLayout.SOUTH);
        }

        getContentPane().add(fondPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void changerContenu(JComponent nord, JComponent centre, JComponent sud) {
        changerContenu(nord, centre, sud, IMAGES[0]);
    }

    private void afficherInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
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
                afficherInfo("La note doit être comprise entre 1 et 5.");
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
        zoneTexte.setFont(new Font("Arial", Font.PLAIN, 12));
        zoneTexte.setLineWrap(true);
        zoneTexte.setWrapStyleWord(true);

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
                contenu.append("\n\n");
            }
            zoneTexte.setText(contenu.toString());
        }

        JScrollPane scroll = new JScrollPane(zoneTexte);
        JOptionPane.showMessageDialog(this, scroll, titre, JOptionPane.INFORMATION_MESSAGE);
    }

    private void saisirAvisMorceau(Morceau morceau) {
        Integer note = demanderNoteAvis();
        if (note == null) return;

        String commentaire = JOptionPane.showInputDialog(this, "Commentaire (optionnel) :", "Donner un avis",
                JOptionPane.PLAIN_MESSAGE);
        if (commentaire == null) return;

        try {
            catalogueController.ajouterAvisMorceau(utilisateurController.getUtilisateurCourant(), morceau, note, commentaire);
            afficherInfo("Avis ajouté avec succès !");
        } catch (IllegalArgumentException ex) {
            afficherInfo(ex.getMessage());
        }
    }

    private void saisirAvisAlbum(Album album) {
        Integer note = demanderNoteAvis();
        if (note == null) return;

        String commentaire = JOptionPane.showInputDialog(this, "Commentaire (optionnel) :", "Donner un avis",
                JOptionPane.PLAIN_MESSAGE);
        if (commentaire == null) return;

        try {
            catalogueController.ajouterAvisAlbum(utilisateurController.getUtilisateurCourant(), album, note, commentaire);
            afficherInfo("Avis ajouté avec succès !");
        } catch (IllegalArgumentException ex) {
            afficherInfo(ex.getMessage());
        }
    }

    private String formaterDuree(int secondes) {
        int minutes = secondes / 60;
        int secs = secondes % 60;
        return minutes + ":" + String.format("%02d", secs);
    }

    private JButton creerBoutonDeconnexion() {
        JButton bouton = creerBoutonRouge("Déconnexion");
        bouton.addActionListener(e -> {
            utilisateurController.deconnexion();
            afficherAccueil();
        });
        return bouton;
    }

    // ============================================
    // ACCUEIL
    // ============================================

    private void afficherAccueil() {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);

        JButton btnVisiteur = creerBouton("Continuer en visiteur");
        JButton btnConnexion = creerBouton("Se connecter");
        JButton btnInscription = creerBouton("S'inscrire");
        JButton btnQuitter = crierBoutonRouge("Quitter");

        for (JButton b : new JButton[]{btnVisiteur, btnConnexion, btnInscription, btnQuitter}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 45));
            centre.add(b);
            centre.add(Box.createVerticalStrut(15));
        }

        btnVisiteur.addActionListener(e -> {
            utilisateurController.continuerEnVisiteur();
            afficherMenuSelonRole();
        });
        btnConnexion.addActionListener(e -> ecranConnexion());
        btnInscription.addActionListener(e -> ecranInscription());
        btnQuitter.addActionListener(e -> System.exit(0));

        JLabel titre = new JLabel("JavaBeats");
        titre.setFont(new Font("Arial", Font.BOLD, 48));
        titre.setForeground(new Color(255, 255, 255));
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        changerContenu(titre, centre, null, IMAGES[0]);
    }

    // ============================================
    // CONNEXION ET INSCRIPTION
    // ============================================

    private void ecranConnexion() {
        JTextField champNom = new JTextField(20);
        JPasswordField champMotDePasse = new JPasswordField(20);

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
        JTextField champNom = new JTextField(20);
        JPasswordField champMotDePasse = new JPasswordField(20);
        JTextField champEmail = new JTextField(20);

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

    // ============================================
    // ROUTAGE
    // ============================================

    private void afficherMenuSelonRole() {
        if (utilisateurController.estAdmin()) {
            menuAdmin();
        } else if (utilisateurController.estAbonneActif()) {
            menuAbonne((Abonne) utilisateurController.getUtilisateurCourant());
        } else {
            menuVisiteur();
        }
    }

    // ============================================
    // MENUS PRINCIPAUX
    // ============================================

    private void menuVisiteur() {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);

        JButton btnCatalogue = creerBouton("Catalogue");
        JButton btnRecherche = creerBouton("Rechercher");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 45));
            centre.add(b);
            centre.add(Box.createVerticalStrut(15));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnRecherche.addActionListener(e -> menuRecherche());

        JLabel titre = new JLabel("Menu Visiteur");
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        changerContenu(titre, centre, creerPanelBoutons(creerBoutonDeconnexion()), IMAGES[0]);
    }

    private void menuAbonne(Abonne abonne) {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);

        JButton btnCatalogue = creerBouton("Catalogue");
        JButton btnAlbums = crierBouton("Albums");
        JButton btnRecherche = creerBouton("Rechercher");
        JButton btnPlaylists = creerBouton("Mes playlists");
        JButton btnHistorique = creerBouton("Historique");

        for (JButton b : new JButton[]{btnCatalogue, btnAlbums, btnRecherche, btnPlaylists, btnHistorique}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 45));
            centre.add(b);
            centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnAlbums.addActionListener(e -> menuAlbums());
        btnRecherche.addActionListener(e -> menuRecherche());
        btnPlaylists.addActionListener(e -> menuPlaylists(abonne));
        btnHistorique.addActionListener(e -> menuHistorique(abonne));

        JLabel titre = new JLabel("Menu Abonné");
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        changerContenu(titre, centre, creerPanelBoutons(creerBoutonDeconnexion()), IMAGES[6]);
    }

    private void menuAdmin() {
        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);

        JButton btnCatalogue = creerBouton("Catalogue");
        JButton btnRecherche = creerBouton("Rechercher");
        JButton btnPlaylists = creerBouton("Mes playlists");
        JButton btnGererCat = creerBouton("Gérer le catalogue");
        JButton btnGererAbo = creerBouton("Gérer les abonnés");
        JButton btnStats = creerBouton("Statistiques");

        for (JButton b : new JButton[]{btnCatalogue, btnRecherche, btnPlaylists, btnGererCat, btnGererAbo, btnStats}) {
            b.setAlignmentX(Component.CENTER_ALIGNMENT);
            b.setMaximumSize(new Dimension(280, 45));
            centre.add(b);
            centre.add(Box.createVerticalStrut(10));
        }

        btnCatalogue.addActionListener(e -> menuCatalogue());
        btnRecherche.addActionListener(e -> menuRecherche());
        btnPlaylists.addActionListener(e -> menuPlaylists(utilisateurController.getUtilisateurCourant()));
        btnGererCat.addActionListener(e -> menuGererCatalogue());
        btnGererAbo.addActionListener(e -> menuGererAbonnes());
        btnStats.addActionListener(e -> menuStatistiques());

        JLabel titre = new JLabel("Menu Admin");
        titre.setFont(new Font("Arial", Font.BOLD, 32));
        titre.setForeground(Color.WHITE);
        titre.setAlignmentX(Component.CENTER_ALIGNMENT);

        changerContenu(titre, centre, creerPanelBoutons(crierBoutonDeconnexion()), IMAGES[1]);
    }

    // ============================================
    // CATALOGUE
    // ============================================

    private void menuCatalogue() {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        DefaultListModel<String> modele = new DefaultListModel<>();

        morceaux.forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste() +
                " | " + m.getNbEcoutes() + " écoutes | " + String.format("%.1f/5", m.getNoteMoyenne())));

        if (morceaux.isEmpty()) {
            modele.addElement("(catalogue vide)");
        }

        JList<String> liste = creerListe(modele);
        JScrollPane scroll = creerScroll(liste);

        JButton btnEcouter = creerBouton("Écouter");
        JButton btnAvis = creerBouton("Donner un avis");
        JButton btnVoirAvis = creerBouton("Voir les avis");
        JButton btnRetour = crierBouton("Retour");

        btnEcouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceaux.isEmpty()) {
                afficherInfo("Sélectionnez un morceau.");
                return;
            }
            Morceau m = morceaux.get(idx);
            try {
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                afficherBarreProgression(m);
                modele.set(idx, m.getTitre() + " - " + m.getArtiste() + " | " + m.getNbEcoutes() +
                        " écoutes | " + String.format("%.1f/5", m.getNoteMoyenne()));
            } catch (RuntimeException ex) {
                afficherInfo(ex.getMessage());
            }
        });

        btnAvis.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceaux.isEmpty()) {
                afficherInfo("Sélectionnez un morceau.");
                return;
            }
            saisirAvisMorceau(morceaux.get(idx));
            Morceau m = morceaux.get(idx);
            modele.set(idx, m.getTitre() + " - " + m.getArtiste() + " | " + m.getNbEcoutes() +
                    " écoutes | " + String.format("%.1f/5", m.getNoteMoyenne()));
        });

        btnVoirAvis.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || morceaux.isEmpty()) {
                afficherInfo("Sélectionnez un morceau.");
                return;
            }
            Morceau m = morceaux.get(idx);
            afficherAvis("Avis - " + m.getTitre(), m.getAvis());
        });

        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JPanel sud = utilisateurPeutDonnerAvis()
                ? creerPanelBoutonsDeuxLignes(btnEcouter, btnAvis, btnVoirAvis, btnRetour)
                : creerPanelBoutonsDeuxLignes(btnEcouter, btnVoirAvis, btnRetour);

        JLabel titre = new JLabel("Catalogue");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        changerContenu(titre, scroll, sud, IMAGES[4]);
    }

    private void menuAlbums() {
        List<Album> albums = catalogueController.listerAlbums();
        DefaultListModel<String> modele = new DefaultListModel<>();

        albums.forEach(a -> modele.addElement(a.getNom() + " - " + a.getArtiste() + " | " +
                a.getMorceaux().size() + " morceaux | " + String.format("%.1f/5", a.getNoteMoyenne())));

        if (albums.isEmpty()) {
            modele.addElement("(aucun album)");
        }

        JList<String> liste = creerListe(modele);
        JScrollPane scroll = creerScroll(liste);

        JButton btnAvis = creerBouton("Donner un avis");
        JButton btnVoirAvis = creerBouton("Voir les avis");
        JButton btnRetour = crierBouton("Retour");

        btnAvis.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || albums.isEmpty()) {
                afficherInfo("Sélectionnez un album.");
                return;
            }
            saisirAvisAlbum(albums.get(idx));
            Album a = albums.get(idx);
            modele.set(idx, a.getNom() + " - " + a.getArtiste() + " | " + a.getMorceaux().size() +
                    " morceaux | " + String.format("%.1f/5", a.getNoteMoyenne()));
        });

        btnVoirAvis.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || albums.isEmpty()) {
                afficherInfo("Sélectionnez un album.");
                return;
            }
            Album a = albums.get(idx);
            afficherAvis("Avis - " + a.getNom(), a.getAvis());
        });

        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JLabel titre = new JLabel("Albums");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        changerContenu(titre, scroll, creerPanelBoutons(btnAvis, btnVoirAvis, btnRetour), IMAGES[4]);
    }

    // ============================================
    // RECHERCHE
    // ============================================

    private void menuRecherche() {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);
        JScrollPane scroll = creerScroll(liste);
        List<Morceau>[] resultat = new List[]{List.of()};

        JTextField champRecherche = new JTextField(20);
        JButton btnChercher = creerBoutonVert("Chercher");
        JButton btnEcouter = creerBouton("Écouter");
        JButton btnRetour = crierBouton("Retour");

        btnChercher.addActionListener(e -> {
            resultat[0] = catalogueController.rechercherMorceau(champRecherche.getText());
            modele.clear();
            if (resultat[0].isEmpty()) {
                modele.addElement("(aucun résultat)");
            } else {
                resultat[0].forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));
            }
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
        nord.setOpaque(false);
        JLabel titrRech = new JLabel("Recherche");
        titrRech.setFont(new Font("Arial", Font.BOLD, 28));
        titrRech.setForeground(Color.WHITE);
        nord.add(titrRech, BorderLayout.NORTH);
        nord.add(champRecherche, BorderLayout.CENTER);
        nord.add(btnChercher, BorderLayout.EAST);

        changerContenu(nord, scroll, creerPanelBoutons(btnEcouter, btnRetour), IMAGES[5]);
    }

    // ============================================
    // BARRE DE PROGRESSION
    // ============================================

    private void afficherBarreProgression(Morceau morceau) {
        JDialog dialog = new JDialog(this, "Lecture en cours", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel("▶ " + morceau.getTitre() + " - " + morceau.getArtiste(), SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(70, 130, 180));

        JProgressBar barre = new JProgressBar(0, 100);
        barre.setStringPainted(true);
        barre.setFont(new Font("Arial", Font.PLAIN, 12));

        dialog.add(label, BorderLayout.NORTH);
        dialog.add(barre, BorderLayout.CENTER);

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

    // ============================================
    // PLAYLISTS
    // ============================================

    private void menuPlaylists(Utilisateur utilisateur) {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);
        JScrollPane scroll = creerScroll(liste);

        Runnable rafraichir = () -> {
            modele.clear();
            List<Playlist> playlists = playlistController.listerPlaylists(utilisateur);
            if (playlists.isEmpty()) {
                modele.addElement("(aucune playlist)");
            } else {
                playlists.forEach(p -> modele.addElement(p.getNom() + " | " + p.getMorceaux().size() +
                        " morceaux - " + formaterDuree(p.getDureeTotale())));
            }
        };
        rafraichir.run();

        JButton btnCreer = creerBouton("Créer");
        JButton btnRenommer = creerBouton("Renommer");
        JButton btnSupprimer = crierBoutonRouge("Supprimer");
        JButton btnOuvrir = crierBoutonVert("Ouvrir");
        JButton btnRetour = crierBouton("Retour");

        btnCreer.addActionListener(e -> {
            String nom = JOptionPane.showInputDialog(this, "Nom de la playlist :", "Créer une playlist",
                    JOptionPane.PLAIN_MESSAGE);
            if (nom == null) return;
            nom = nom.trim();
            if (nom.isEmpty()) {
                afficherInfo("Entrez un nom.");
                return;
            }
            Playlist p = playlistController.creerPlaylist(utilisateur, nom);
            if (p == null) {
                afficherInfo("Création impossible.");
            } else {
                rafraichir.run();
            }
        });

        btnRenommer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            List<Playlist> playlists = playlistController.listerPlaylists(utilisateur);
            if (idx < 0 || playlists.isEmpty()) {
                afficherInfo("Sélectionnez une playlist.");
                return;
            }
            String nom = JOptionPane.showInputDialog(this, "Nouveau nom de la playlist :",
                    playlists.get(idx).getNom());
            if (nom == null) return;
            nom = nom.trim();
            if (nom.isEmpty()) {
                afficherInfo("Entrez un nouveau nom.");
                return;
            }
            if (playlistController.renommerPlaylist(utilisateur, idx, nom)) {
                rafraichir.run();
            } else {
                afficherInfo("Impossible.");
            }
        });

        btnSupprimer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || playlistController.listerPlaylists(utilisateur).isEmpty()) {
                afficherInfo("Sélectionnez une playlist.");
                return;
            }
            if (demanderConfirmation("Supprimer cette playlist ?")) {
                if (playlistController.supprimerPlaylist(utilisateur, idx)) {
                    rafraichir.run();
                }
            }
        });

        btnOuvrir.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            List<Playlist> playlists = playlistController.listerPlaylists(utilisateur);
            if (idx < 0 || playlists.isEmpty()) {
                afficherInfo("Sélectionnez une playlist.");
                return;
            }
            menuPlaylist(utilisateur, playlists.get(idx));
        });

        btnRetour.addActionListener(e -> afficherMenuSelonRole());

        JLabel titre = new JLabel("Mes Playlists");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        JPanel sud = new JPanel();
        sud.setLayout(new BoxLayout(sud, BoxLayout.Y_AXIS));
        sud.setOpaque(false);
        sud.add(creerPanelBoutons(btnCreer, btnRenommer, btnSupprimer));
        sud.add(creerPanelBoutons(btnOuvrir, btnRetour));

        changerContenu(titre, scroll, sud, IMAGES[6]);
    }

    private void menuPlaylist(Utilisateur utilisateur, Playlist playlist) {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);
        JScrollPane scroll = creerScroll(liste);

        JLabel labelDuree = new JLabel("Durée : " + formaterDuree(playlist.getDureeTotale()), SwingConstants.CENTER);
        labelDuree.setFont(new Font("Arial", Font.PLAIN, 14));
        labelDuree.setForeground(Color.WHITE);

        Runnable rafraichir = () -> {
            modele.clear();
            List<Morceau> morceaux = playlist.getMorceaux();
            if (morceaux.isEmpty()) {
                modele.addElement("(playlist vide)");
            } else {
                morceaux.forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));
            }
            labelDuree.setText("Durée : " + formaterDuree(playlist.getDureeTotale()));
        };
        rafraichir.run();

        JButton btnEcouter = creerBouton("Écouter");
        JButton btnAjouter = crierBoutonVert("Ajouter");
        JButton btnRetirer = crierBoutonRouge("Retirer");
        JButton btnRetour = crierBouton("Retour");

        btnEcouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            List<Morceau> morceaux = playlist.getMorceaux();
            if (idx < 0 || morceaux.isEmpty()) {
                afficherInfo("Sélectionnez un morceau.");
                return;
            }
            Morceau m = morceaux.get(idx);
            try {
                catalogueController.ecouter(utilisateurController.getUtilisateurCourant(), m);
                afficherBarreProgression(m);
            } catch (RuntimeException ex) {
                afficherInfo(ex.getMessage());
            }
        });

        btnAjouter.addActionListener(e -> menuAjoutMorceau(utilisateur, playlist, rafraichir));

        btnRetirer.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0 || playlist.getMorceaux().isEmpty()) {
                afficherInfo("Sélectionnez un morceau.");
                return;
            }
            if (playlistController.retirerMorceau(playlist, idx)) {
                rafraichir.run();
            } else {
                afficherInfo("Impossible.");
            }
        });

        btnRetour.addActionListener(e -> menuPlaylists(utilisateur));

        JPanel nord = new JPanel(new GridLayout(2, 1));
        nord.setOpaque(false);
        JLabel nom = new JLabel(playlist.getNom(), SwingConstants.CENTER);
        nom.setFont(new Font("Arial", Font.BOLD, 24));
        nom.setForeground(Color.WHITE);
        nord.add(nom);
        nord.add(labelDuree);

        changerContenu(nord, scroll, creerPanelBoutonsDeuxLignes(btnEcouter, btnAjouter, btnRetirer, btnRetour), IMAGES[6]);
    }

    private void menuAjoutMorceau(Utilisateur utilisateur, Playlist playlist, Runnable rafraichir) {
        List<Morceau> morceaux = catalogueController.listerMorceaux();
        if (morceaux.isEmpty()) {
            afficherInfo("Catalogue vide.");
            return;
        }

        DefaultListModel<String> modele = new DefaultListModel<>();
        morceaux.forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));

        JList<String> liste = creerListe(modele);
        JScrollPane scroll = creerScroll(liste);

        JButton btnAjouter = crierBoutonVert("Ajouter");
        JButton btnRetour = crierBouton("Retour");

        btnAjouter.addActionListener(e -> {
            int idx = liste.getSelectedIndex();
            if (idx < 0) {
                afficherInfo("Sélectionnez un morceau.");
                return;
            }
            if (playlistController.ajouterMorceau(playlist, morceaux.get(idx))) {
                rafraichir.run();
                afficherInfo("Ajouté !");
                menuPlaylist(utilisateur, playlist);
            } else {
                afficherInfo("Impossible (déjà présent ?).");
            }
        });

        btnRetour.addActionListener(e -> menuPlaylist(utilisateur, playlist));

        JLabel titre = new JLabel("Ajouter un morceau");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        changerContenu(titre, scroll, creerPanelBoutons(btnAjouter, btnRetour), IMAGES[3]);
    }

    // ============================================
    // HISTORIQUE
    // ============================================

    private void menuHistorique(Abonne abonne) {
        DefaultListModel<String> modele = new DefaultListModel<>();
        List<EntreeHistorique> entrees = abonne.getHistorique().getEntrees();

        if (entrees.isEmpty()) {
            modele.addElement("(historique vide)");
        } else {
            for (int i = 0; i < entrees.size(); i++) {
                modele.addElement((i + 1) + ". " + entrees.get(i));
            }
        }

        JList<String> liste = creerListe(modele);
        JScrollPane scroll = creerScroll(liste);

        JButton btnRetour = crierBouton("Retour");
        btnRetour.addActionListener(e -> menuAbonne(abonne));

        JPanel nord = new JPanel(new GridLayout(2, 1));
        nord.setOpaque(false);
        JLabel titre = new JLabel("Historique");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);
        JLabel stats = new JLabel(abonne.getHistorique().getNbEcoutes() + " écoutes au total", SwingConstants.CENTER);
        stats.setFont(new Font("Arial", Font.PLAIN, 14));
        stats.setForeground(Color.WHITE);
        nord.add(titre);
        nord.add(stats);

        changerContenu(nord, scroll, creerPanelBoutons(btnRetour), IMAGES[5]);
    }

    // ============================================
    // GESTION ADMIN
    // ============================================

    private void menuGererCatalogue() {
        DefaultListModel<String> modele = new DefaultListModel<>();
        List<Morceau>[] morceauxAffiches = new List[]{List.of()};

        Runnable rafraichir = () -> {
            modele.clear();
            morceauxAffiches[0] = catalogueController.listerMorceaux();
            if (morceauxAffiches[0].isEmpty()) {
                modele.addElement("(aucun morceau)");
            } else {
                morceauxAffiches[0].forEach(m -> modele.addElement(m.getTitre() + " - " + m.getArtiste()));
            }
        };
        rafraichir.run();

        JList<String> liste = creerListe(modele);
        JScrollPane scroll = creerScroll(liste);

        JButton btnAjouter = crierBoutonVert("Ajouter");
        JButton btnSupprimer = crierBoutonRouge("Supprimer");
        JButton btnRetour = crierBouton("Retour");

        btnAjouter.addActionListener(e -> {
            JTextField champTitre = new JTextField(20);
            JTextField champArtiste = new JTextField(20);
            JTextField champDuree = new JTextField(20);
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

        JLabel titre = new JLabel("Gestion du catalogue");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        changerContenu(titre, scroll, crierPanelBoutons(btnAjouter, btnSupprimer, btnRetour), IMAGES[1]);
    }

    private void menuGererAbonnes() {
        DefaultListModel<String> modele = new DefaultListModel<>();
        JList<String> liste = creerListe(modele);
        JScrollPane scroll = crierScroll(liste);
        List<Utilisateur>[] utilisateursAffiches = new List[]{List.of()};

        Runnable rafraichir = () -> {
            modele.clear();
            utilisateursAffiches[0] = utilisateurController.listerUtilisateursGerables();
            if (utilisateursAffiches[0].isEmpty()) {
                modele.addElement("(aucun utilisateur)");
            } else {
                utilisateursAffiches[0].forEach(u -> {
                    String statut = (u instanceof Abonne a) ? (a.isAbonnementActif() ? " actif" : " suspendu") : "";
                    modele.addElement(u.getLogin() + " (" + u.getClass().getSimpleName() + ")" + statut);
                });
            }
        };
        rafraichir.run();

        JButton btnSuspendre = crierBouton("Suspendre");
        JButton btnSupprimer = crierBoutonRouge("Supprimer");
        JButton btnRetour = crierBouton("Retour");

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
            if (idx < 0) {
                afficherInfo("Sélectionnez un utilisateur.");
                return;
            }
            Utilisateur u = utilisateursAffiches[0].get(idx);
            if (u instanceof Abonne abonne) {
                boolean actif = abonne.isAbonnementActif();
                abonne.setAbonnementActif(!actif);
                rafraichir.run();
                mettreAJourBoutonSuspension.run();
                afficherInfo(actif ? "Compte suspendu." : "Suspension retirée.");
            } else {
                afficherInfo("Impossible (pas un abonné).");
            }
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

        JLabel titre = new JLabel("Gestion des abonnés");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        changerContenu(titre, scroll, crierPanelBoutons(btnSuspendre, btnSupprimer, btnRetour), IMAGES[3]);
    }

    private void menuStatistiques() {
        Statistique stats = new Statistique(catalogueController.getCatalogue());
        Morceau morceauTop = stats.getMorceauLePlusEcoute();

        String contenu =
                "Utilisateurs        : " + utilisateurController.listerUtilisateurs().size() + "\n\n" +
                        "Morceaux            : " + catalogueController.listerMorceaux().size() + "\n" +
                        "Albums              : " + catalogueController.listerAlbums().size() + "\n" +
                        "Artistes            : " + catalogueController.listerArtistes().size() + "\n\n" +
                        "Écoutes totales     : " + stats.getNbEcoutesTotal() + "\n" +
                        "Durée moyenne       : " + String.format("%.0f sec", stats.getDureeMoyenneMorceaux()) + "\n" +
                        "Top morceau         : " + (morceauTop != null ? morceauTop.getTitre() + " (" + morceauTop.getNbEcoutes() + " écoutes)" : "aucun");

        JTextArea zoneTexte = new JTextArea(contenu);
        zoneTexte.setEditable(false);
        zoneTexte.setFont(new Font("Arial", Font.PLAIN, 13));
        zoneTexte.setLineWrap(true);
        zoneTexte.setWrapStyleWord(true);
        zoneTexte.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scroll = new JScrollPane(zoneTexte);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

        JButton btnRetour = crierBouton("Retour");
        btnRetour.addActionListener(e -> menuAdmin());

        JLabel titre = new JLabel("Statistiques");
        titre.setFont(new Font("Arial", Font.BOLD, 28));
        titre.setForeground(Color.WHITE);

        changerContenu(titre, scroll, crierPanelBoutons(btnRetour), IMAGES[1]);
    }

    // Méthode helper pour typage (correction de la typo)
    private JButton crierBouton(String texte) {
        return creerBouton(texte);
    }

    private JButton crierBoutonRouge(String texte) {
        return creerBoutonRouge(texte);
    }

    private JButton crierBoutonVert(String texte) {
        return creerBoutonVert(texte);
    }

    private JButton crierBoutonDeconnexion() {
        return creerBoutonDeconnexion();
    }

    private JPanel crierPanelBoutons(JButton... boutons) {
        return creerPanelBoutons(boutons);
    }

    private JScrollPane crierScroll(JList<String> liste) {
        return creerScroll(liste);
    }
}