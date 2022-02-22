package packRobot;

public class Border extends Element{
    public final BorderSide side;

    public Border(BorderSide side,double position){
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
     * Renvoie la direction de la Border par rapport à e.
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
        return "Border{" + side + "(" + (getPosX()) + ")}";
    }

    public static void main(String[] args) {
        Robot.initArea(400,200);
        Border coin1= new Border(BorderSide.BOTTOM,Robot.getArea().getHeight());
        Border coin2= new Border(BorderSide.TOP,0);
        Border coin3= new Border(BorderSide.LEFT,0);
        Border coin4= new Border(BorderSide.RIGHT,Robot.getArea().getWidth());
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