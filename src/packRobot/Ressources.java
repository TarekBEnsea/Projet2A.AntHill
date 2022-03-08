package packRobot;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ressources extends Element{

    double max1=Fenetre.width;
    double max2=Fenetre.height;
    int min =10;
    private Image image;
    private String name;
    private int taille = 30;
    private int oldtaille = 30;

    public Ressources(String name){
        this.name=name;
        rayonContact =15;
        posY= (int) (Math.random()*(max2-min));
        posX= (int) (Math.random()*(max1-min));
        try {
            BufferedImage tmp = new BufferedImage(1,1,2);

            switch (name) {
                case("fb") : tmp = ImageIO.read(new File("src/packRobot/fb.png"));
                    break;
                case("fraise") :  tmp = ImageIO.read(new File("src/packRobot/fraise.png"));
                    break;
                case("pdt") :  tmp = ImageIO.read(new File("src/packRobot/pdt.png"));
                    break;
                default:
                    break;
            }

            this.image = new BufferedImage(taille, taille, 2);
            this.image.getGraphics().drawImage(tmp.getScaledInstance(taille, taille, 4), 0, 0, null);

        } catch (IOException e) {
            System.out.println("image non cr�er");
        }

    }

    @Override
    public String toString() {
        return "Point{ X=" + posX +", Y=" + posY +'}';
    }
    public void draw(Graphics g){
        if(taille > 0) {
            if(oldtaille - taille > 0) image = image.getScaledInstance(taille,taille,Image.SCALE_SMOOTH);
            g.drawImage(image, (int) (posX - rayonContact), (int) (posY - rayonContact), null);
            /*g.setColor(Color.BLUE);
            g.drawOval((int) (posX-rayonContact), (int) (posY-rayonContact), (int) (2*rayonContact), (int) (2*rayonContact));
            g.setColor(Color.green);
            g.drawRect((int) posX, (int) posY, 2, 2);*/
            oldtaille = taille;
        }
    }
    public void setTaille(int taille){ this.taille = taille; }
    public int getTaille(){ return taille; }
    public String getName() {
        return name;
    }

}
