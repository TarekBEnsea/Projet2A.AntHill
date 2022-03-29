package packRobot;

import java.util.ArrayList;

public class Robotxml extends Robot{

    private int k = 0;
    private double avanceX;
    private double avanceY;
    private long time;
    private Comportement comportement;
    private ArrayList<String> listeComportement = new ArrayList<>();
    private int lastComportementFinished;

    public Robotxml(long timeBetweenFrame) {
        super(timeBetweenFrame);
    }

    public void setAvanceX(double avanceX) {
        this.avanceX = avanceX;
    }
    public void setAvanceY(double avanceY) {
        this.avanceY = avanceY;
    }

    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }

    public int getLastComportementFinished() {
        return lastComportementFinished;
    }
    public void setLastComportementFinished(int lastComportementFinished) {
        this.lastComportementFinished = lastComportementFinished;
    }

    public void setComportement(Comportement comportement) {
        this.comportement = comportement;
    }
    public Comportement getComportement() {
        return comportement;
    }
    public int getK() {
        return k;
    }
    public void setK(int k) {
        this.k = k;
    }
    public ArrayList<String> getListeComportement() {
        return listeComportement;
    }

    public boolean AvanceXY(){
        if(avanceX-posX > 0) this.setOrdreTheta(Math.atan((avanceY-posY)/(avanceX-posX)));
        else this.setOrdreTheta(Math.atan((avanceY-posY)/(avanceX-posX))+Math.PI);
        updateMouv();
        return Math.abs(avanceY-posY) < 1 && Math.abs(avanceX-posX) < 1;
    }
}
