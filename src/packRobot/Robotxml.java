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
    private double inf_posx = -15000;
    private double inf_posy = -15000;

    public Robotxml(long timeBetweenFrame) {
        super(timeBetweenFrame);
    }

    public void setAvanceX(double avanceX) {
        this.avanceX = avanceX;
    }
    public void setAvanceY(double avanceY) {
        this.avanceY = avanceY;
    }

    public void setInf_posx(double inf_posx) {
        this.inf_posx = inf_posx;
    }
    public void setInf_posy(double inf_posy) {
        this.inf_posy = inf_posy;
    }
    public double getInf_posx() {
        return inf_posx;
    }
    public double getInf_posy() {
        return inf_posy;
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
    public ArrayList<String> getListeComportement() {
        return listeComportement;
    }

    public boolean AvanceXY(){
        if(avanceX-posX > 0) this.setOrdreTheta(Math.atan((avanceY-posY)/(avanceX-posX)));
        else this.setOrdreTheta(Math.atan((avanceY-posY)/(avanceX-posX))+Math.PI);
        updateMouv();
        return Math.abs(avanceY-posY) < 1 && Math.abs(avanceX-posX) < 1;
    }

    public boolean hasInformation(){
        return (inf_posx!=-15000 || inf_posy!=-15000);
    }
}
