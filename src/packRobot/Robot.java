package packRobot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;


public class Robot extends Element{
	private BufferedImage image;
	public void setImage(String pathname) {
		try {
			BufferedImage tmp = ImageIO.read(new File(pathname));
			this.image = new BufferedImage((int)largeur, (int)longueur, 2);
			this.image.getGraphics().drawImage(tmp.getScaledInstance((int)largeur, (int)longueur, 4), 0, 0, (ImageObserver)null);
		} catch (IOException e) {
			System.out.println("image non créée");
		}
	}
	/*position et état*/
	private double theta; //2 pi modulo, sens horaire, origin on x vector
	private double longueur=20;
	private double largeur=15;
	private boolean broken = false;
	private boolean carry=false;

	/*commandes d'asservissement*/
	public final double vitesseLigneMax;
	private double vitesseLigne;
	private double ordreVitesseLigne;
	private double ordreTheta;
	private double ecartThetaChangement=Math.PI;
	private double saveOrdreVitesse;
	private int tauRota=20; //valeur à déterminer ou configurer
	private double tauAccel = 0.1;
	private static int areaWidth;
	private static int areaHeight;
	public final double FULLRANGE=rayonDetect;
	public final double MIDRANGE=(rayonContact+rayonDetect)/2;
	public final double MINRANGE=Math.min(2*rayonContact,rayonDetect);

	/*position geters & seters*/
	public double getTheta(){return this.theta;}

	public boolean isCarry() {
		return carry;
	}
	public void setCarry(boolean carry) {
		this.carry = carry;
	}

	/*commands geters & seters*/
	public double getOrdreVitesseLigne(){return ordreVitesseLigne;}
	public double getOrdreTheta(){return ordreTheta;}
	public void setOrdreVitesseLigne(double ordre){
		if (ordre<0) this.ordreVitesseLigne=0;
		else if(ordre>vitesseLigneMax) this.ordreVitesseLigne=vitesseLigne;
		else ordreVitesseLigne=ordre;
		
		saveOrdreVitesse=ordreVitesseLigne;
	}
	public void setOrdreTheta(double ordre){
		this.ordreTheta=(ordre%(2*Math.PI)+2*Math.PI)%(2*Math.PI);
		if (Math.abs(ordreTheta-this.theta)>Math.abs(ordreTheta-(this.theta-2*Math.PI))) this.theta-=2*Math.PI;
		else if (Math.abs(ordreTheta-this.theta)>Math.abs(ordreTheta-(this.theta+2*Math.PI))) this.theta+=2*Math.PI;
		
		ecartThetaChangement=Math.abs(theta-ordreTheta);
	}
	/**
	 * Définit la zone dans laquelle se déplacent les Robots.
	 * à utiliser avec:
	 * Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	 * int width = screenSize.getWidth();
	 * int height = screenSize.getHeight();
	 * @param width largeur de la zone.
	 * @param height hauteur de la zone.
	 */
	public static void initArea(int width, int height){
		areaWidth=width;
		areaHeight=height;
	}
	public static Dimension getArea(){return new Dimension(areaWidth,areaHeight);}
	public static void areaPrompt(){System.out.println("Width: "+areaWidth+"\tHeight: "+areaHeight);}

	/*Constructeurs*/
	public Robot(double x, double y, double theta){
		this.posX=x;
		this.posY=y;
		this.theta=theta;
		this.vitesseLigneMax=5;
		this.vitesseLigne= 0;
		this.ordreVitesseLigne=0;
		this.saveOrdreVitesse=ordreVitesseLigne;
		this.ordreTheta=theta;
		this.rayonContact =10;
		this.rayonDetect=rayonContact+10;
		//this.rayonContact=Math.sqrt(Math.pow(this.largeur,2)+Math.pow(this.longueur,2));
		
		setImage("src/packRobot/Ant2.png");
	}
	public Robot(){
		this.posX=Math.random()*areaWidth;
		this.posY=Math.random()*areaHeight;
		this.theta=Math.random()*2*Math.PI;
		this.vitesseLigne= 0;
		this.vitesseLigneMax=3;
		this.ordreVitesseLigne=vitesseLigneMax;
		this.saveOrdreVitesse=ordreVitesseLigne;

		this.ordreTheta=theta;
		this.rayonContact =0;
		this.rayonDetect=rayonContact+90;
		try {
			BufferedImage tmp = ImageIO.read(new File("src/packRobot/Ant2.png"));
			this.image = new BufferedImage((int)largeur, (int)longueur, 2);
			this.image.getGraphics().drawImage(tmp.getScaledInstance((int)largeur, (int)longueur, 4), 0, 0, (ImageObserver)null);
		} catch (IOException e) {
			System.out.println("image non créée");
		}
	}


	/*méthodes*/
	
	/**
	 * Asservissement de la position en fonction de la commande.
	 * @param deltaT temps écoulé (en ms) depuis la dernière actualisation.
	 */
	public void updateMouv(double deltaT){

		if(Math.abs(theta-ordreTheta)>0.1) ordreVitesseLigne=0; //a faire avec des exception peut-etre
		else ordreVitesseLigne=saveOrdreVitesse;

		double alphaRota=tauRota/deltaT;
		double alphaAccel=tauAccel/deltaT;
		vitesseLigne=(ordreVitesseLigne+vitesseLigne*alphaAccel)/(1+alphaAccel);
		theta=(ordreTheta+theta*alphaRota)/(1+alphaRota);
		
		this.posX+=deltaT*vitesseLigne*Math.cos(theta);
		this.posY+=deltaT*vitesseLigne*Math.sin(theta);
	}

	public void mouvInPanel(){
		/**le robot avance linéairement dans un contour fermé et "rebondit" de manière commandée à l'approche d'une bordure**/
		if (broken) return;
		int x= (int) this.getPosX();
		int y= (int) this.getPosY();
		double ordrAngle= this.getOrdreTheta();
		boolean change=false;
		double angle;
		
		//System.out.println("Fourmi en ("+(double) ((int) (x*100))/100+";"+(double) ((int) (y*100))/100+
		//")\ttheta= "+(double) ((int) (fourmi1.getTheta()*100))/100+" ordreTheta: "+(double) ((int) (ordrAngle*100))/100);
		
		if ((x<10 && Math.cos(ordrAngle)<0) || (x>Fenetre.width-30 && Math.cos(ordrAngle)>0)) {ordrAngle=Math.PI-ordrAngle; change=true;}
		if ((y<10 && Math.sin(ordrAngle)<0) || (y>Fenetre.height-30 && Math.sin(ordrAngle)>0)) {ordrAngle=-ordrAngle; change=true;}
		if (change) this.setOrdreTheta(ordrAngle);
		
		this.updateMouv(1);
	}

	public void breakWheel(){
		this.vitesseLigne=0;
		this.broken=true;
	}
	public void freewheel(){broken=false;}

	public void devieTraj(double thetaObstacle,double thetaDeviation){
		double angle=((this.theta- thetaObstacle)%(2*Math.PI)+3*Math.PI)%(2*Math.PI)-Math.PI; //donne l'angle relatif à l'obstacle dans [-pi,pi]
		if(thetaDeviation<0) thetaDeviation=0;
		else if(thetaDeviation>Math.PI) thetaDeviation=Math.PI;

		if (getPprocheDistance()>rayonDetect || getPprocheDistance()== -1) setOrdreVitesseLigne(vitesseLigneMax);
		else if (getPprocheDistance()<rayonContact) breakWheel();
		else{
			double relativeMultiplier=(Math.abs(angle)/(Math.PI)-.5);
			setOrdreVitesseLigne(vitesseLigneMax*relativeMultiplier*0.7+0.1);
			if (angle<Math.PI/2) setOrdreTheta(thetaObstacle +thetaDeviation);
			else if (angle>3*Math.PI/2) setOrdreTheta(thetaObstacle -thetaDeviation);
		}
	}
	public void suivre(Element element){
		double theta=getDirection(element);
		double angle=((this.theta-theta)%(2*Math.PI)+2*Math.PI)%(2*Math.PI); //donne l'angle relatif à l'obstacle dans [0;2pi]

		if (getPprocheDistance()>rayonDetect || getPprocheDistance()== -1) setOrdreVitesseLigne(vitesseLigneMax);
		else if (getPprocheDistance()<rayonContact) breakWheel();
		else{
			double relativeMultiplier=(getPprocheDistance()-rayonContact)/(rayonDetect-rayonContact);
			setOrdreVitesseLigne(vitesseLigneMax*relativeMultiplier*0.7+0.1);
			if (angle < Math.PI) setOrdreTheta(theta + Math.PI * (1 - relativeMultiplier));
			else if (angle > Math.PI) setOrdreTheta(theta - Math.PI * (1 - relativeMultiplier));
		}
	}

	public void draw(Graphics g){
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(theta + 3*Math.PI/10, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		Graphics2D g2d = (Graphics2D) g;

		g2d.drawImage(op.filter(image, null), (int) (posX-longueur/2), (int) (posY-largeur/2), null);

		/*g.setColor(Color.magenta);
		g.drawRect((int) posX, (int) posY, 2,2);
		g.setColor(Color.BLUE);
		g.drawOval((int) (posX-rayonContact), (int) (posY-rayonContact), (int) rayonContact*2, (int) rayonContact*2);*/


	}

	/** fonction test **/
	public static void main(String[] args){
		Robot michel = new Robot(0,0,0);
		for(int i=0;i<Integer.parseInt(args[0]);i++){ 
			System.out.println("michel est en ("+michel.posX+";"+michel.posY+")");
			michel.updateMouv(1);
		}
	}
}
