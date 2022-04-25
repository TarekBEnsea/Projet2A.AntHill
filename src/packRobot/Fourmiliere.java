package packRobot;

import testxml.InterXml;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Classe permettant de créer le sprite de la fourmilière et de l'afficher.
 */
public class Fourmiliere extends Element{
    private Image image;
    private int width;
    private int height;

    /**
     * Création du sprite de la fourmilière et de sa zone d'interaction
     */
    public Fourmiliere() {
        rayonContact = 10;
        width = 200;
        height = 200;
        InterXml Physiquerobot = new InterXml("src/RobotPhysique");
        this.posX = Physiquerobot.ReadCompState("fourmilière","x");
        this.posY = Physiquerobot.ReadCompState("fourmilière","y");
        try {
            BufferedImage tmp = ImageIO.read(new File("src/packRobot/anthill.png"));
            this.image = new BufferedImage(width, height, 2);
            this.image.getGraphics().drawImage(tmp.getScaledInstance(width, height, 4), 0, 0, null);
        } catch (IOException e) {
            System.out.println("image non creer");
        }
    }

    /**
     * Affichage du sprite de la fourmilière en focntion de la position de la caméra
     * @param g paramètre de la fonction {@link Panneau#paintComponent(Graphics)}
     * @param cameraX position de la caméra sur l'axe X pour un affichage relatif à la caméra
     * @param cameraY position de la caméra sur l'axe Y pour un affichage relatif à la caméra
     * @see Panneau#paintComponent(Graphics)
     */
    public void draw(Graphics g,int cameraX, int cameraY){
        g.drawImage(image, (int) (posX - width/2)-cameraX, (int) (posY - height/2)-cameraY, null);
    }
}
