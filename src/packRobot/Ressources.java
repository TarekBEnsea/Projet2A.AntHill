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

    public Ressources(String name){
        this.name=name;
        rayonContact =15;
        posY= (int) (Math.random()*(max2-min));
        posX= (int) (Math.random()*(max1-min));
        try {
            image = new BufferedImage(1,1,1);
            switch (name) {
                case("fb") :  image = ImageIO.read(new File("src/packRobot/fb.png"));
                    break;
                case("fraise") :  image = ImageIO.read(new File("src/packRobot/fraise.png"));
                    break;
                case("pdt") :  image = ImageIO.read(new File("src/packRobot/pdt.png"));
                    break;
                default:
                    break;
            }

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
            Image tmp;
            tmp = image.getScaledInstance(taille, taille, Image.SCALE_SMOOTH);
            g.drawImage(tmp, (int) (posX - rayonContact), (int) (posY - rayonContact), null);
            /*g.setColor(Color.BLUE);
            g.drawOval((int) (posX-rayonContact), (int) (posY-rayonContact), (int) (2*rayonContact), (int) (2*rayonContact));
            g.setColor(Color.green);
            g.drawRect((int) posX, (int) posY, 2, 2);*/
        }
    }
    public void setTaille(int taille){ this.taille = taille; }
    public int getTaille(){ return taille; }
    public String getName() {
        return name;
    }

}
