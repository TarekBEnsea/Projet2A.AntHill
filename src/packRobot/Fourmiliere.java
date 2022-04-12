package packRobot;

import testxml.InterXml;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Fourmiliere extends Element{
    private Image image;
    private int width;
    private int height;

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

    public void draw(Graphics g){
        g.drawImage(image, (int) (posX - width/2), (int) (posY - height/2), null);
    }
}
