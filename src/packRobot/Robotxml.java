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

    /**
     * Appel le constructeur de {@link Robot} : {@link #Robotxml(long)}
     * @param timeBetweenFrame le temps entre chaque raffraichissemnt de l'affichage de l'écran
     */
    public Robotxml(long timeBetweenFrame) {
        super(timeBetweenFrame);
    }

    /**
     * Appel le constructeur de {@link Robot} : {@link #Robotxml(int, int, double, long)}
     * @param x coordonnée en x du robot à créer
     * @param y coordonnée en y du robot à créer
     * @param theta angle de rotation du robot à créer
     * @param timeBetweenFrame le temps entre chaque raffraichissemnt de l'affichage de l'écran
     */
    public Robotxml(int x, int y, double theta, long timeBetweenFrame) {super(x,y,theta,timeBetweenFrame);}

    /**
     * Permet de changer la coordonnée en x de l'objectif du robot
     * @param avanceX coordonnée en x de l'objectif du robot
     */
    public void setAvanceX(double avanceX) {
        this.avanceX = avanceX;
    }

    /**
     * Permet de changer la coordonnée en y de l'objectif du robot
     * @param avanceY coordonnée en y de l'objectif du robot
     */
    public void setAvanceY(double avanceY) {
        this.avanceY = avanceY;
    }

    /**
     * Permet de changer la coordonnée en x de l'information du robot
     * @param inf_posx la coordonnée en x de l'information du robot
     */
    public void setInf_posx(double inf_posx) {
        this.inf_posx = inf_posx;
    }

    /**
     * Permet de changer la coordonnée en y de l'information du robot
     * @param inf_posy la coordonnée en y de l'information du robot
     */
    public void setInf_posy(double inf_posy) {
        this.inf_posy = inf_posy;
    }

    /**
     * Retourne la coordonnée en x de l'information du robot
     * @return la coordonnée en x de l'information du robot
     */
    public double getInf_posx() {
        return inf_posx;
    }

    /**
     * Retourne la coordonnée en y de l'information du robot
     * @return la coordonnée en y de l'information du robot
     */
    public double getInf_posy() {
        return inf_posy;
    }

    /**
     * Retourne  .
     * @return
     */
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
