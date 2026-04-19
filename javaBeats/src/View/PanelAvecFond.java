package View;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

class PanelAvecFond extends JPanel {
    private BufferedImage image;

    public PanelAvecFond(String cheminImage) {
        try {
            image = ImageIO.read(new File(cheminImage));
        } catch (Exception e) {
            System.err.println("Image non trouvée: " + cheminImage);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}