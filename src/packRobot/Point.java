package packRobot;

import java.io.Serializable;

public class Point implements Serializable {

    int X;
    int Y;

    public Point(){
        X=0;
        Y=0;
    }
    public Point(int a, int b){
        X=a;
        Y=b;
    }

    @Override
    public String toString() {
        return "Point{ X=" + X +", Y=" + Y +'}';
    }
    public int getX() {
        return X;
    }
    public int getY() {
        return Y;
    }
    public void setX(int x) {
        X = x;
    }
    public void setY(int y) {
        Y = y;
    }
}
