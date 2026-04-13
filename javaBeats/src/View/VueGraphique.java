package View;

import Controller.CatalogueController;
import Controller.PlaylistController;
import Controller.UtilisateurController;

import javax.swing.*;
import java.awt.*;

public class VueGraphique extends JFrame {

    private UtilisateurController utilisateurController;
    private CatalogueController catalogueController;
    private PlaylistController playlistController;

    public VueGraphique(UtilisateurController u, CatalogueController c, PlaylistController p) {
        this.utilisateurController = u;
        this.catalogueController = c;
        this.playlistController = p;

        initUI();
    }

    private void initUI() {
        setTitle("Application Musique");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JLabel title = new JLabel("Bienvenue dans l'application musicale 🎵", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnAfficherCatalogue = new JButton("Afficher le catalogue");
        btnAfficherCatalogue.addActionListener(e -> afficherCatalogue());

        panel.add(title, BorderLayout.NORTH);
        panel.add(btnAfficherCatalogue, BorderLayout.CENTER);

        add(panel);
    }

    private void afficherCatalogue() {
        StringBuilder sb = new StringBuilder();

        catalogueController.getCatalogue().getMorceaux().forEach(m ->
                sb.append("- ").append(m.getTitre()).append("\n")
        );

        JOptionPane.showMessageDialog(this,
                sb.length() > 0 ? sb.toString() : "Catalogue vide",
                "Catalogue",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public void run() {
        setVisible(true);
    }
}