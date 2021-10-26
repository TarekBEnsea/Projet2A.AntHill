package packRobot;
import javax.swing.*;
import java.awt.*;

public class Ressources {
    Integer X;
    Integer Y;
    int max1=1000;
    int max2=824;
    int min=1;
    int randX= (int) (Math.random()*(max1-min));
    int randY= (int) (Math.random()*(max2-min));

    public int getX() {
        return X;
    }

    public int getY() {
        return Y;
    }

    public void setX(Integer x) {
        X = x;
    }

    public void setY(Integer y) {
        Y = y;
    }

    public Ressources(){
        X=0;
        Y=0;
    }
    public Ressources(int a, int b){
        X=a;
        Y=b;
    }
    public void main(String[] args){
        Point pt=new Point (randX, randY );

    }

    @Override
    public String toString() {
        return "Point{ X=" + X +", Y=" + Y +'}';
    }
    public void draw(Graphics g){
        g.setColor(Color.yellow);
        g.fillOval(randX,randY,10,10);
    }
}