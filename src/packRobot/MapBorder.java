package packRobot;

/**
 * Bordure de la zone de simulation. Les Robots doivent évoluer entre 4 Bordure ayant chacune
 * un des 4 type de tags de {@link BorderSide bordure}: {@link BorderSide#TOP TOP},{@link BorderSide#RIGHT RIGHT},{@link BorderSide#BOTTOM BOTTOM} ou {@link BorderSide#LEFT LEFT}
 */
public class MapBorder extends Element{
    /** Type de Bordure*/
    private final BorderSide side;

    /**
     * Crée une nouvelle MapBorder correspondant à un bord d'une zone rectangulaire.
     * @param side un des 4 types de bordure possible {@link BorderSide#TOP TOP},{@link BorderSide#RIGHT RIGHT},
     * {@link BorderSide#BOTTOM BOTTOM} ou {@link BorderSide#LEFT LEFT}
     * @param position position sur l'axe orthogonal à l'axe de la bordure.
     *                 position en X pour {@link BorderSide#TOP TOP} et {@link BorderSide#BOTTOM BOTTOM}
     *                 position en Y pour {@link BorderSide#RIGHT RIGHT} et {@link BorderSide#LEFT LEFT}
     */
    public MapBorder(BorderSide side, double position){
        this.side=side;
        switch (side){
            case LEFT:
            case RIGHT:
                posX=position; posY=0;break;
            case TOP, BOTTOM:
                posX=0; posY=position; break;
            default: System.out.println("Erreur d'affectation de la bordure");posY=0; posX=0; break;
        }
    }

    /**
     * Renvoie la distance entre le centres du l'Element et cette bordure. Cette distance est définie par
     * la plus proche distance entre le centre de l'Element et un point de la droite de la bordure soit un segment orthogonal
     * à la bordure passant par l'Element.
     * @param e Élément dont on veut la distance.
     * @return distance à l'Element en argument
     */
    @Override
    public double getDistance(Element e){
        switch (side){
            case LEFT: case RIGHT: return Math.abs(e.getPosX()-posX);
            case TOP,BOTTOM: return Math.abs(e.getPosY()-posY);
            default:  System.out.println("Erreur de calcul de distance de la bordure");return 0;
        }
    }

    /**
     * Renvoie la direction de l'Element e par rapport à la MapBorder.
     * @param e Element cible.
     * @return Direction de l'Element.
     */
    @Override
    public double getDirection(Element e){
        switch (side){
            case LEFT,RIGHT:
                if(e.posX-posX>0) return 0; //Xpos
                else return Math.PI; //Xneg
            case TOP,BOTTOM:
                if(e.posY-posY>0) return Math.PI/2; //Ypos
                else return -Math.PI/2; //Yneg
            default: return 0; //erreur masquée
        }
    }

    /**
     * Renvoie la représentation en String de l'objet contenant le coté attribué ainsi que sa position.
     * @return représentation en String de l'objet
     */
    @Override
    public String toString() {
        return "MapBorder{" + side + "(" + (getPosX()) + ")}";
    }
}
/**
 * Type de bordure. Représente un des côtés du rectangle que forme la zone de simulation.
 */
enum BorderSide{
    BOTTOM,TOP,LEFT,RIGHT
}
