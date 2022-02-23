package packRobot;

public class MapBorder extends Element{
    public final BorderSide side;

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

    @Override
    public double getDistance(Element e){
        switch (side){
            case LEFT: case RIGHT: return Math.abs(e.getPosX()-posX);
            case TOP,BOTTOM: return Math.abs(e.getPosY()-posY);
            default:  System.out.println("Erreur de calcul de distance de la bordure");return 0;
        }
    }

    /**
     * Renvoie la direction de la MapBorder par rapport à e.
     * @param e Element cible.
     * @return Direction de la bordure.
     */
    @Override
    public double getDirection(Element e){
        switch (side){
            case TOP: return -Math.PI/2; //Yneg
            case BOTTOM: return Math.PI/2; //Ypos
            case LEFT: return Math.PI; //Xneg
            case RIGHT: return 0; //xpos
            default: return 0; //erreur masquée
        }
    }

    @Override
    public String toString() {
        return "MapBorder{" + side + "(" + (getPosX()) + ")}";
    }

    public static void main(String[] args) {
        Robot.initArea(400,200);
        MapBorder coin1= new MapBorder(BorderSide.BOTTOM,Robot.getArea().getHeight());
        MapBorder coin2= new MapBorder(BorderSide.TOP,0);
        MapBorder coin3= new MapBorder(BorderSide.LEFT,0);
        MapBorder coin4= new MapBorder(BorderSide.RIGHT,Robot.getArea().getWidth());
        Robot roboto = new Robot(185,50,0);

        roboto.resetPprocheDistance();
        System.out.println(roboto.estProche(coin4, false));
        System.out.println(roboto.getDistance(coin1));
        System.out.println(roboto.getDistance(coin2));
        System.out.println(roboto.getDistance(coin3));
        System.out.println(roboto.getDistance(coin4));
    }
}
enum BorderSide{
    BOTTOM,TOP,LEFT,RIGHT
}
