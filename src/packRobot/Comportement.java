package packRobot;

import testxml.InterXml;

import java.awt.Dimension;
import java.util.ArrayList;

public class Comportement {
    /**
     * Retourne le nom de la fonction. Si le nom de la fonction est "" alors le comportement n'agis plus.
     * @return le nom de la fonction
     * @see #setName(String)
     */
    public String getName() {
        return name;
    }

    /**
     * Permet de renommer le comportement. Si le nom de la fonction est "" alors le comportement n'agis plus.
     * @param name nom de la fonction
     * @see #getName()
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retourne l'id du comportement
     * @return l'id du comportement
     */
    public Integer getId() {
        return id;
    }

    private String name;
    private InterXml comportementsimple;
    private Robotxml robot;
    private ArrayList<Robotxml> robots;
    private ArrayList<Robotxml> robotsSansInfo = new ArrayList<>();
    private ArrayList<Ressources> ressources;
    private Integer id;
    private int simWidth, simHeight;
    private int infoX;
    private int infoY;

    /**
     * Choisi un nouveau comportement pour un robot spécifique en fonction de l'ancien comportement et de la liste des comportements.
     * La fonction ne peut choisir qu'un comportement qui est plus prioritaire que le dernier.
     * @param robot le robot pour lequel on choisi le comportement
     * @param robots la liste de tous les robots
     * @param ressources la liste de toutes les ressources
     * @param comportementsimple la liste des comportements
     * @param previousName le nom du dernier comportement du robot
     * @param oldId l'id du dernier comportement du robot
     */
    public Comportement(Robotxml robot,ArrayList<Robotxml> robots,ArrayList<Ressources> ressources, InterXml comportementsimple, String previousName, Integer oldId) {
        this.robot = robot;
        this.robots = robots;
        this.ressources = ressources;
        this.comportementsimple = comportementsimple;
        Dimension simDim = Robot.getArea();
        this.simWidth = simDim.width;
        this.simHeight = simDim.height;

        ArrayList<String> liste_comportement;
        liste_comportement = comportementsimple.ReturnXmlNode("Comportement");
        boolean nextComportement = false;
        int currentPriority;
        if(previousName.equals("")) currentPriority = liste_comportement.size();
        else currentPriority = comportementsimple.ReadCompState(previousName, "priority");
        for(int priority = 0; priority < currentPriority; priority++) {
            for(int id = 0; id < liste_comportement.size(); id++) {
                if(nextComportement);
                else{
                    int priorityid = Integer.parseInt(comportementsimple.ReadCompStateId(id, "priority"));
                    //System.out.println("Id: "+id+" Priority: "+priorityid);
                    if( priority == priorityid) {
                        //System.out.println("Id Trouvé: "+id);
                        name = comportementsimple.ReadCompStateId(id, "name");
                        this.id = id;
                        if(capteurs()) {
                            nextComportement = true;
                            //System.out.println(name);
                        }
                    }
                }
            }
        }
        if(!nextComportement){
            name = previousName;
            id = oldId;
        }
    }

    /**
     * Retourne True si toutes les conditions sont vérifier pour le choix du comportement sinon False.
     * @return True si toutes les conditions sont vérifier pour le choix du comportement sinon False
     * @see #capteurFourmiProche()
     * @see #capteurFourmiProcheSansInfo()
     * @see #capteurInformation()
     * @see #capteurIsCarrying()
     * @see #capteurLastComportementFinished()
     * @see #capteuroutofbound()
     * @see #capteurRessourceProche()
     */
    public boolean capteurs(){
        return  capteurFourmiProche() &&
                capteurRessourceProche() &&
                capteurLastComportementFinished() &&
                capteuroutofbound() &&
                capteurInformation() &&
                capteurFourmiProcheSansInfo() &&
                capteurIsCarrying();
    }

    /**
     * Vérifie si un robot est en dehors de la carte.
     * Par défaut retourne True s'il n'est pas activé dans le fichier XML.
     * @return True si le robot est en dehors de la carte.
     * @see #capteurs()
     */
    private boolean capteuroutofbound() {
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "outofbound")) == 1) {
            //if ((robot.posX<10 && Math.cos(robot.getOrdreTheta())<0) || (robot.posX>Fenetre.width-30 && Math.cos(robot.getOrdreTheta())>0)) b = true;
            //if ((robot.posY<10 && Math.sin(robot.getOrdreTheta())<0) || (robot.posY>Fenetre.height-30 && Math.sin(robot.getOrdreTheta())>0)) b = true;
            b= (    robot.posX<robot.rayonContact || robot.posX>simWidth- robot.rayonContact 
                ||  robot.posY<robot.rayonContact || robot.posY>simHeight- robot.rayonContact );
        }
        return b;
    }

    /**
     * Vérifie si un robot est proche d'une ressource.
     * Par défaut retourne True s'il n'est pas activé dans le fichier XML.
     * @return True si le robot est proche d'une ressource.
     * @see #capteurs()
     */
    private boolean capteurRessourceProche(){
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "ressourcesnextto")) == 1){
            b = false;
            for(Ressources ressource : ressources){
                if(robot.estProche(ressource,true)){
                    infoX = (int) ressource.getPosX();
                    infoY = (int) ressource.getPosY();
                    b = true;
                }
            }
        }
        return b;
    }

    /**
     * Vérifie si le dernier comportement du robot correspond à celui spécifié dans le XML.
     * Cela permet d'effectuer plusieurs comportements à la suite.
     * Par défaut retourne True s'il n'est pas activé dans le fichier XML.
     * @return True si le dernier comportement du robot correspond à celui spécifié dans le XML.
     * @see #capteurs()
     */
    private boolean capteurLastComportementFinished() {
        boolean b = true;
        int lastComportementFinished = Integer.parseInt(comportementsimple.ReadCompStateId(id, "lastcomportementfinished"));
        if(lastComportementFinished >= 0) {
            b = robot.getLastComportementFinished() == lastComportementFinished;
        }
        return b;
    }

    /**
     * Vérifie si un robot est proche d'un autre robot.
     * Par défaut retourne True s'il n'est pas activé dans le fichier XML.
     * @return True si le robot est proche d'un autre robot.
     * @see #capteurs()
     */
    private boolean capteurFourmiProche(){
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "antsnextto")) == 1){
            b = false;
            for(Robotxml robot2 : robots){
                if(!robot.equals(robot2) && robot.estProche(robot2,true)){
                    b = true;
                }
            }
        }
        return b;
    }

    /**
     * Vérifie si un robot est porteur des coordonnées d'une ressource.
     * Par défaut retourne True s'il n'est pas activé dans le fichier XML.
     * @return True si le robot est porteur des coordonnées d'une ressource.
     * @see #capteurs()
     */
    private boolean capteurInformation(){
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "information")) == 1){
            b = robot.hasInformation();
        }
        return b;
    }

    /**
     * Vérifie si un robot est proche d'un autre robot et que ce dernier n'est pas porteur d'une information.
     * Par défaut retourne True s'il n'est pas activé dans le fichier XML.
     * @return True si le robot est proche d'un autre robot et que ce dernier n'est pas porteur d'une information.
     * @see #capteurs()
     */
    private boolean capteurFourmiProcheSansInfo(){
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "fourmisansinfo")) == 1){
            robotsSansInfo.clear();
            for(Robotxml robot2 : robots) {
                if (robot.estProche(robot2, true) && !robot2.hasInformation()){
                    robotsSansInfo.add(robot2);
                }
            }
            b = robotsSansInfo.size() > 0;
        }
        return b;
    }

    /**
     * Vérifie si un robot porte une ressource.
     * Par défaut retourne True s'il n'est pas activé dans le fichier XML.
     * @return True si le robot porte une ressource.
     * @see #capteurs()
     */
    private boolean capteurIsCarrying(){
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "iscarrying")) == 1){
            b = robot.isCarry();
        }
        return b;
    }

    /**
     * Change certaines variables des robots en fonction du nom comportement
     */
    public void XMLtoJava(){
        switch (name){
            case "MouvXY":
                robot.setAvanceX(Integer.parseInt(comportementsimple.ReadCompStateId(id, "x")) + robot.getPosX());
                robot.setAvanceY(Integer.parseInt(comportementsimple.ReadCompStateId(id, "y")) + robot.getPosY());
                break;
            case "GoToXY":
                robot.setAvanceX(Integer.parseInt(comportementsimple.ReadCompStateId(id, "x")));
                robot.setAvanceY(Integer.parseInt(comportementsimple.ReadCompStateId(id, "y")));
                break;
            case "Stop":
                robot.setTime(Integer.parseInt(comportementsimple.ReadCompStateId(id, "time")));
                break;
            case "GoToElement":
                robot.setAvanceX(robot.getInf_posx());
                robot.setAvanceY(robot.getInf_posy());
                break;
            case "Communique":
                for(Robotxml robot2 : robotsSansInfo){
                    robot2.setInf_posx(robot.getInf_posx());
                    robot2.setInf_posy(robot.getInf_posy());
                }
                break;
            case "GetInformation":
                robot.setInf_posy(infoY);
                robot.setInf_posx(infoX);
                break;
            default:
                break;
        }
    }
}
