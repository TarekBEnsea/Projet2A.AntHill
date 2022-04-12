package packRobot;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ressources extends Element{

    double max1=Robot.getArea().width;
    double max2=Robot.getArea().height;
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

            //image = new BufferedImage(1,1,1);
            BufferedImage tmp = new BufferedImage(1,1,2);

            switch (name) {
                case("fb") :  tmp = ImageIO.read(new File("src/packRobot/fb.png"));

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
    public Ressources(int x, int y,String name){
        this.name=name;
        rayonContact =15;
        posY= y;
        posX= x;
        init_ressource();
    }

    private void init_ressource(){
        try {
            BufferedImage tmp = new BufferedImage(1,1,2);

            switch (name) {
                case("fb") :  tmp = ImageIO.read(new File("src/packRobot/fb.png"));

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
    public void draw(Graphics g,int cameraX,int cameraY){
        if(taille > 0) {
            if(oldtaille - taille > 0) image = image.getScaledInstance(taille,taille,Image.SCALE_SMOOTH);
            g.drawImage(image, (int) (posX - rayonContact)-cameraX, (int) (posY - rayonContact)-cameraY, null);
           /* g.setColor(Color.BLUE);
            g.drawOval((int) (posX-rayonContact), (int) (posY-rayonContact), (int) (2*rayonContact), (int) (2*rayonContact));
            g.setColor(Color.green);
            g.drawRect((int) posX, (int) posY, 2, 2);*/
            oldtaille = taille;
        }
        else{
            System.out.println("J'existe !");
        }
    }
    public void setTaille(int taille){ this.taille = taille; }
    public int getTaille(){ return taille; }
    public String getName() {
        return name;
    }
}
