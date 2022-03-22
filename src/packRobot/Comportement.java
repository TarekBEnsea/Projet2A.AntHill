package packRobot;

import testxml.InterXml;

import java.util.ArrayList;

public class Comportement {

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getId() {
        return id;
    }

    private String name;
    private InterXml comportementsimple;
    private Robotxml robot;
    private ArrayList<Robotxml> robots;
    private ArrayList<Ressources> ressources;
    private Integer id;

    public Comportement(Robotxml robot,ArrayList<Robotxml> robots,ArrayList<Ressources> ressources, InterXml comportementsimple, String previousName, Integer oldId) {
        this.robot = robot;
        this.robots = robots;
        this.ressources = ressources;
        this.comportementsimple = comportementsimple;

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
                    System.out.println("Id: "+id+" Priority: "+priorityid);
                    if( priority == priorityid) {
                        System.out.println("Id Trouvé: "+id);
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

    public boolean capteurs(){
        Boolean b1 = capteurAntsnextto();
        Boolean b2 = capteurRessourcenextto();
        Boolean b3 = capteurLastComportementFinished(robot);
        Boolean b4 = capteuroutofbound();
        return b1 && b2 && b3 && b4;
    }

    private boolean capteuroutofbound() {
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "outofbound")) == 1) {
            b = false;
            if ((robot.posX<10 && Math.cos(robot.getOrdreTheta())<0) || (robot.posX>Fenetre.width-30 && Math.cos(robot.getOrdreTheta())>0)) b = true;
            if ((robot.posY<10 && Math.sin(robot.getOrdreTheta())<0) || (robot.posY>Fenetre.height-30 && Math.sin(robot.getOrdreTheta())>0)) b = true;
        }
        return b;
    }


    private boolean capteurRessourcenextto() {
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "ressourcesnextto")) == 1) {
            b = false;
            for (Ressources ressource : ressources) {
                b = b || robot.enContact(ressource);
            }
        }
        return b;
    }

    private boolean capteurAntsnextto() {
        boolean b = true;
        if(Integer.parseInt(comportementsimple.ReadCompStateId(id, "antsnextto")) == 1){
            b = false;
            for(Robot robot2 : robots) {
                b = b || robot.enContact(robot2);
            }
        }
        return b;
    }

    private boolean capteurLastComportementFinished(Robotxml robot) {
        boolean b = true;
        int lastComportementFinished = Integer.parseInt(comportementsimple.ReadCompStateId(id, "lastcomportementfinished"));
        if(lastComportementFinished > 0) {
            b = robot.getLastComportementFinished() == lastComportementFinished;
        }
        return b;
    }

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
            default:
                break;
        }
    }
}
