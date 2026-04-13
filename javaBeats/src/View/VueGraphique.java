package View;

import Controller.CatalogueController;
import Controller.PlaylistController;
import Controller.UtilisateurController;
import Model.Morceau;
import Model.User.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VueGraphique extends JFrame {

    private final UtilisateurController utilisateurController;
    private final CatalogueController catalogueController;
    @SuppressWarnings("unused")
    private final PlaylistController playlistController;

    public VueGraphique(UtilisateurController u, CatalogueController c, PlaylistController p) {
        this.utilisateurController = u;
        this.catalogueController = c;
        this.playlistController = p;
        initUI();
    }

    private void initUI() {
        setTitle("JavaBeats");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titre = new JLabel("Bienvenue dans JavaBeats", SwingConstants.CENTER);
        titre.setFont(new Font("Arial", Font.BOLD, 20));

        JButton btnChoisirProfil = new JButton("Choisir un profil");
        btnChoisirProfil.addActionListener(e -> choisirProfil());

        panel.add(titre, BorderLayout.NORTH);
        panel.add(btnChoisirProfil, BorderLayout.CENTER);
        add(panel);
    }

    private void choisirProfil() {
        String[] options = {"Visiteur", "Inscrit", "Administrateur"};
        int choix = JOptionPane.showOptionDialog(
                this,
                "Choisissez un profil.",
                "Profils",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choix == 0) {
            utilisateurController.continuerEnVisiteur();
            afficherCatalogue("Catalogue visiteur");
        } else if (choix == 1) {
            ouvrirProfilInscrit();
        } else if (choix == 2) {
            ouvrirProfilAdmin();
        }
    }

    private void ouvrirProfilInscrit() {
        String[] options = {"Connexion", "Inscription"};
        int choix = JOptionPane.showOptionDialog(
                this,
                "Choisissez une action pour le profil inscrit.",
                "Profil inscrit",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choix == 0) {
            connecterInscrit();
        } else if (choix == 1) {
            inscrireAbonne();
        }
    }

    private void connecterInscrit() {
        JTextField loginField = new JTextField();
        JPasswordField motDePasseField = new JPasswordField();
        Object[] message = {
                "Login :", loginField,
                "Mot de passe :", motDePasseField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Connexion", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        boolean connecte = utilisateurController.connexion(
                loginField.getText(),
                new String(motDePasseField.getPassword())
        );

        if (!connecte) {
            JOptionPane.showMessageDialog(this, "Connexion impossible.");
            return;
        }

        if (utilisateurController.estAdmin()) {
            ouvrirMenuAdmin();
            return;
        }

        afficherCatalogue("Catalogue inscrit");
    }

    private void inscrireAbonne() {
        JTextField loginField = new JTextField();
        JPasswordField motDePasseField = new JPasswordField();
        JTextField emailField = new JTextField();
        Object[] message = {
                "Login :", loginField,
                "Mot de passe :", motDePasseField,
                "Email :", emailField
        };

        int result = JOptionPane.showConfirmDialog(this, message, "Inscription", JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        boolean inscrit = utilisateurController.inscriptionAbonne(
                loginField.getText(),
                new String(motDePasseField.getPassword()),
                emailField.getText()
        );

        if (!inscrit) {
            JOptionPane.showMessageDialog(this, "Inscription impossible.");
            return;
        }

        afficherCatalogue("Catalogue inscrit");
    }

    private void ouvrirProfilAdmin() {
        boolean connecte = utilisateurController.connexion("admin", "admin");
        if (!connecte) {
            JOptionPane.showMessageDialog(this, "Compte administrateur indisponible.");
            return;
        }
        ouvrirMenuAdmin();
    }

    private void ouvrirMenuAdmin() {
        String[] options = {"Liste des utilisateurs", "Liste des musiques"};
        int choix = JOptionPane.showOptionDialog(
                this,
                "Choisissez une vue administrateur.",
                "Administrateur",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        if (choix == 0) {
            afficherUtilisateurs();
        } else if (choix == 1) {
            afficherMusiquesAdmin();
        }
    }

    private void afficherUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurController.listerUtilisateurs();
        StringBuilder sb = new StringBuilder("Liste des utilisateurs :\n\n");

        for (Utilisateur utilisateur : utilisateurs) {
            sb.append("- ")
                    .append(utilisateur.getLogin())
                    .append(" (")
                    .append(utilisateur.getClass().getSimpleName())
                    .append(")\n");
        }

        sb.append("\nAjout et suppression a implementer plus tard.");
        afficherTexte("Utilisateurs", sb.toString());
    }

    private void afficherMusiquesAdmin() {
        StringBuilder sb = new StringBuilder("Liste des musiques :\n\n");
        for (Morceau morceau : catalogueController.listerMorceaux()) {
            sb.append("- ").append(morceau.getTitre()).append("\n");
        }
        sb.append("\nAjout et suppression a implementer plus tard.");
        afficherTexte("Musiques", sb.toString());
    }

    private void afficherCatalogue(String titre) {
        StringBuilder sb = new StringBuilder();
        for (Morceau morceau : catalogueController.listerMorceaux()) {
            sb.append("- ").append(morceau.getTitre()).append("\n");
        }
        afficherTexte(titre, sb.length() == 0 ? "Catalogue vide." : sb.toString());
    }

    private void afficherTexte(String titre, String contenu) {
        JTextArea zone = new JTextArea(contenu);
        zone.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(zone);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        JOptionPane.showMessageDialog(this, scrollPane, titre, JOptionPane.INFORMATION_MESSAGE);
    }

    public void run() {
        setVisible(true);
    }
}
