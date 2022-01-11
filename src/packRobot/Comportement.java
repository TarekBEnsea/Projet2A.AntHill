package packRobot;

import testxml.InterXml;

import java.util.ArrayList;

public class Comportement {

    public String getName() {
        return name;
    }

    private String name;
    private InterXml comportementsimple;
    private Robot robot;
    private ArrayList<Robot> robots;
    private ArrayList<Ressources> ressources;

    public Comportement(Robot robot,ArrayList<Robot> robots,ArrayList<Ressources> ressources, InterXml comportementsimple) {
        this.robot = robot;
        this.robots = robots;
        this.ressources = ressources;
        this.comportementsimple = comportementsimple;

        ArrayList<String> liste_comportement;
        liste_comportement = comportementsimple.ReturnXmlNode("Comportement");
        Boolean nextComportement = false;
        for(int priority = 0; priority < liste_comportement.size(); priority++) {
            for(int j = 0; j< comportementsimple.ReturnXmlNode("Comportement").size(); j++) {
                if(nextComportement);
                else{
                    if( priority == comportementsimple.ReadCompState(liste_comportement.get(j), "priority")) {
                        name = liste_comportement.get(j);
                        switch (name) {
                            case "MouvXY":
                                if(capteurMouvXY()) {
                                    nextComportement = true;
                                    System.out.println("mouv");
                                }
                                break;
                            case "Stop":
                                if(capteurStop()) {
                                    System.out.println("stop");
                                    nextComportement = true;
                                }
                                break;
                            case "GoToXY":
                                if(capteurGoToXY()) {
                                    nextComportement = true;
                                    System.out.println("goto");
                                }
                                break;
                            default:
                                nextComportement = true;
                                break;
                        }
                    }
                }
            }
        }
    }

    public boolean capteurMouvXY(){
        Boolean b1 = capteurAntsnextto("MouvXY");
        Boolean b2 = capteurRessourcenextto("MouvXY");
        return b1 && b2;
    }

    public boolean capteurStop(){
        Boolean b1 = capteurAntsnextto("Stop");
        Boolean b2 = capteurRessourcenextto("Stop");
        Boolean b3 = capteuroutofbound("Stop");
        return b1 && b2 && b3;
    }

    public boolean capteurGoToXY(){
        Boolean b1 = capteuroutofbound("GoToXY");
        return b1;
    }

    private Boolean capteuroutofbound(String name) {
        Boolean b = true;
        if(comportementsimple.ReadCompState(name, "outofbound") == 1) {
            b = false;
            if ((robot.posX<10 && Math.cos(robot.getOrdreTheta())<0) || (robot.posX>1000-30 && Math.cos(robot.getOrdreTheta())>0)) b = true;
            if ((robot.posY<10 && Math.sin(robot.getOrdreTheta())<0) || (robot.posY>600-30 && Math.sin(robot.getOrdreTheta())>0)) b = true;
        }
        return b;
    }


    private Boolean capteurRessourcenextto(String name) {
        Boolean b = true;
        if(comportementsimple.ReadCompState(name, "ressourcesnextto") == 1) {
            b = false;
            for (Ressources ressource : ressources) {
                b = b || robot.enContact(ressource);
            }
        }
        return b;
    }

    private Boolean capteurAntsnextto(String name) {
        Boolean b = true;
        if(comportementsimple.ReadCompState(name, "antsnextto") == 1){
            b = false;
            for(Robot robot2 : robots) {
                b = b || robot.enContact(robot2);
            }
        }
        return b;
    }
/*
    public void XMLtoJava(){
        switch (name){
            case "MouvXY":
                robot.setAvanceX(comportementsimple.ReadCompState(name, "x") + robot.getPosX());
                robot.setAvanceY(comportementsimple.ReadCompState(name, "y") + robot.getPosY());
                break;
            case "GoToXY":
                robot.setAvanceX(comportementsimple.ReadCompState(name, "x"));
                robot.setAvanceY(comportementsimple.ReadCompState(name, "y"));
                break;
            case "Stop":
                robot.setTime(comportementsimple.ReadCompState(name, "time"));
            default:
                break;
        }
    }*/
}
