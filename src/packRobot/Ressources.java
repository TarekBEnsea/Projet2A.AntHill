package packRobot;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Ressources {

    double max1=Fenetre.width;
    double max2=Fenetre.height;
    int min=1;
    int randX= (int) (Math.random()*(max1-min));
    int randY= (int) (Math.random()*(max2-min));
    private Image fb;
    private Image fraise;
    private Image pdt;
    private String name;

    public int getRandX() {
        return randX;
    }

    public int getRandY() {
        return randY;
    }

    public void setRandX(Integer x) {
        randX = x;
    }

    public void setY(Integer y) {
        randY = y;
    }

    public Ressources(String name){
        this.name=name;
        try {
            BufferedImage tmp = ImageIO.read(new File("src/packRobot/fb.png"));
            fb = tmp.getScaledInstance(30,30,Image.SCALE_SMOOTH);
            BufferedImage tmp2 = ImageIO.read(new File("src/packRobot/fraise.png"));
            fraise = tmp2.getScaledInstance(30,30,Image.SCALE_SMOOTH);
            BufferedImage tmp3 = ImageIO.read(new File("src/packRobot/pdt.png"));
            pdt = tmp3.getScaledInstance(30,30,Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("image non cr�er");
        }
    }
    public Ressources(int a, int b){
        randX=a;
        randY=b;
    }
    public void main(String[] args){
        Point pt=new Point (randX, randY );

    }

    @Override
    public String toString() {
        return "Point{ X=" + randX +", Y=" + randY +'}';
    }
    public void draw(Graphics g){
        //g.setColor(Color.yellow);
        //g.fillOval(randX,randY,10,10);
        if(name == "fb") g.drawImage(fb, (int) randX, (int) randY, null);
        if(name == "fraise") g.drawImage(fraise,(int) randX, (int) randY, null);
        if(name == "pdt") g.drawImage(pdt,(int) randX, (int) randY, null);
    }
}