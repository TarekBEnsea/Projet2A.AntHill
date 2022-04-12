package packRobot;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Liste des robots d'un essaim.
 * Le but de cette classe est de rendre plus efficace l'interaction entre robots.
 *
 * @todo paramètres propre à un type de robot (faction de robot)
 */
public class EssaimRobot extends ArrayList<Robotxml> {
    //private LinkedList<Robotxml>[][] listVictimMatrix;
    /** Matrice classant les robots de l'essaim. Les cases sont remplies en fonction des coordonnées des robots
     * @see #initMatrix
     * @see #remplitMatrix */
    private LinkedList<Robotxml>[][] listMenaceMatrix;
    /** Unité de segmentation de la matrice {@link #listMenaceMatrix}*/
    private int elementDimension;

    /** Crée un nouvel essaim de robots vide*/
    public EssaimRobot(){
        super();
    }

    /**
     * Ajout le robot à la fin de la liste.
     * Si la liste est vide, initialise listMenaceMatrix en fonction de la distance de la distance de détection du premier élément.
     * @param r Robot à ajouter à l'essaim
     * @return true (as specified by Collection.add)
     */
    @Override
    public boolean add(Robotxml r){
        if (this.isEmpty()) initMatrix(r);
        return super.add(r);
    }

    /**
     * Crée la matrice des Menaces. Cette matrice segmente l'espace de simulation en carrés de coté {@link #elementDimension}
     * @param robotxml robot de référence pour la dimension (surface recouverte) des cases de la matrice.
     */
    private void initMatrix(Robotxml robotxml){
        System.out.println("plop");
        Dimension robotarea=robotxml.getArea();
        elementDimension=(int) robotxml.FULLRANGE;
        int lignes=(int)Math.ceil(robotarea.height/elementDimension)+1;
        int colonnes=(int)Math.ceil(robotarea.width/elementDimension)+1;
        listMenaceMatrix=new LinkedList[lignes][colonnes];
        for(int i=0;i<lignes;i++){
            for(int j=0;j<colonnes;j++){
                listMenaceMatrix[i][j] = new LinkedList<Robotxml>();
            }
        }
        /*
        System.out.println(new Robotxml());
        Robotxml[] aze= new Robotxml[6];
        for(Robotxml r:aze) r= new Robotxml();
        System.out.println(aze[5]);
        for(int i=0;i<6;i++) aze[i]= new Robotxml();
        System.out.println(aze[5]);
            //n'est-ce pas truculent?
        */
    }

    /**
     * Place chaque robot dans une case de la matrice {@link #listMenaceMatrix} en fonction de la position dans l'aire de simulation.
     */
    public void remplitMatrix(){
        for(LinkedList[] tab1: listMenaceMatrix){
            for(LinkedList l:tab1) l.clear();
        }
        for(Robotxml robotxml:this){
            int colonneJ = (int)Math.floor(robotxml.getPosX()/elementDimension);
            int ligneI = (int)Math.floor(robotxml.getPosY()/elementDimension);
            listMenaceMatrix[ligneI][colonneJ].add(robotxml);
        }
    }

    /**
     * Rassemble les robots proche du robot en argument dans une liste et la renvoie.
     * @param robotxml Robot victime autour duquel on cherche des voisins
     * @return Liste des robots proche du robot en argument
     */
    public LinkedList<Robotxml> menacesProches(Robotxml robotxml){
        /** Liste des robots considérés comme potentiellement proche du robot en argument.*/
        LinkedList<Robotxml> listMenacesDirectes = new LinkedList<>();
        int colonneJ = (int)(robotxml.getPosX()/elementDimension);
        int ligneI = (int)(robotxml.getPosY()/elementDimension);

        listMenacesDirectes.clear();
        for(int i=-1;i<2;i++){
            for (int j=-1;j<2;j++){
                try{for(Robotxml r: listMenaceMatrix[ligneI-1][colonneJ-1]) listMenacesDirectes.add(r);
                } catch(ArrayIndexOutOfBoundsException e){};
            }
        }
        return listMenacesDirectes;

        /* //old mistake
        int colonneJ = (int)Math.floor(.5+robotxml.getPosX()/elementDimension);
        int ligneI = (int)Math.floor(.5+robotxml.getPosY()/elementDimension);

        listMenacesDirectes.clear();
        if(ligneI>0){
            if(colonneJ>0){
                for(Robotxml r: listMenaceMatrix[ligneI-1][colonneJ-1]) listMenacesDirectes.add(r);}
            if(colonneJ<listMenaceMatrix[0].length-1){
                for(Robotxml r: listMenaceMatrix[ligneI-1][colonneJ+1]) listMenacesDirectes.add(r);}
        }
        if(ligneI<listMenaceMatrix.length-1){
            if(colonneJ>0){
                for(Robotxml r: listMenaceMatrix[ligneI+1][colonneJ-1]) listMenacesDirectes.add(r);}
            if(colonneJ<listMenaceMatrix[0].length-1){
                for(Robotxml r: listMenaceMatrix[ligneI+1][colonneJ+1]) listMenacesDirectes.add(r);}
        }
        return listMenacesDirectes;
         */
    }
}
