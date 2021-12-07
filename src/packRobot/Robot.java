package packRobot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
//import java.awt.Graphics2D;

public class Robot extends Element{
	private BufferedImage image;
	/**position**/
	private int tauRota=20; //valeur à déterminer ou configurer
	private double tauAccel = 0.1;
	private double theta; //2 pi modulo, sens horaire, origin on x vector
		
	/**commandes**/
	public final double vitesseLigneMax=3;
	private double vitesseLigne;
	private double ordreVitesseLigne;
	private double ordreTheta;
	private double ecartThetaChangement=Math.PI;
	private double saveOrdreVitesse;
	
	/**dimensions et hitboxes**/
	private double longueur=20;
	private double largeur=15;
	private boolean broken = false;
	
	
	// XML
	private ArrayList<String> listeComportement = new ArrayList<>();
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	private long time;
	private double avanceX;
	private double avanceY;
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	private int k = 0;
	public ArrayList<String> getListeComportement() {
		return listeComportement;
	}

	/**xml geters & seters **/

	public void setAvanceX(double avanceX) {
		this.avanceX = avanceX;
	}
	public void setAvanceY(double avanceY) {
		this.avanceY = avanceY;
	}
	
	/**position getters & setters**/
	public double getTheta(){return this.theta;}
	
	/**commands getters & setters**/
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

	public boolean getBroken(){return broken;}

	/**Constructeurs**/
	public Robot(double x, double y, double theta){
		this.posX=x;
		this.posY=y;
		this.theta=theta;
		this.vitesseLigne= 0;
		this.ordreVitesseLigne=0;
		this.saveOrdreVitesse=ordreVitesseLigne;
		this.ordreTheta=Math.PI;
		this.rayonContact =10;
		//this.rayonContact=Math.sqrt(Math.pow(this.largeur,2)+Math.pow(this.longueur,2));
		try {
			//image = ImageIO.read(new File("src/packRobot/Ant.png"));
			BufferedImage tmp = ImageIO.read(new File("src/packRobot/Ant2.png"));
			this.image = new BufferedImage(largeur, longueur, 2);
			this.image.getGraphics().drawImage(tmp.getScaledInstance(largeur, longueur, 4), 0, 0, (ImageObserver)null);
		} catch (IOException e) {
			System.out.println("image non créée");
		}
	}
	public Robot(){
		this.posX=Math.random()*Fenetre.width;
		this.posY=Math.random()*Fenetre.height;
		this.theta=Math.random()*2*Math.PI;
		this.vitesseLigne= 0;
		this.ordreVitesseLigne=vitesseLigneMax;
		this.saveOrdreVitesse=ordreVitesseLigne;
		this.ordreTheta=theta;
		this.rayonContact =10;
		this.rayonDetect=rayonContact+30;
		try {
			//image = ImageIO.read(new File("src/packRobot/Ant.png"));
			BufferedImage tmp = ImageIO.read(new File("src/packRobot/Ant2.png"));
			this.image = new BufferedImage(largeur, longueur, 2);
			this.image.getGraphics().drawImage(tmp.getScaledInstance(largeur, longueur, 4), 0, 0, (ImageObserver)null);
		} catch (IOException e) {
			System.out.println("image non créée");
		}
	}
	
	/**méthodes**/
	public boolean AvanceXY(){
		if(avanceX-posX > 0) this.setOrdreTheta(Math.atan((avanceY-posY)/(avanceX-posX)));
		else this.setOrdreTheta(Math.atan((avanceY-posY)/(avanceX-posX))+Math.PI);
		updateMouv(1);
		return Math.abs(avanceY-posY) < 1 && Math.abs(avanceX-posX) < 1;
	}

	public void turn(int angle, long time){

	}
	public void updateMouv(double deltaT){
		/*if(Math.abs(theta-ordreTheta)>ecartThetaChangement*0.15) ordreVitesseLigne=vitesseLigneMax/4; //a faire avec des exception peut-etre
		else {
			ordreVitesseLigne=saveOrdreVitesse;
			//theta=ordreTheta;
		}*/

		double alphaRota=tauRota/deltaT;
		double alphaAccel=tauAccel/deltaT;
		vitesseLigne=(ordreVitesseLigne+vitesseLigne*alphaAccel)/(1+alphaAccel);
		theta=(ordreTheta+theta*alphaRota)/(1+alphaRota);
		
		this.posX+=deltaT*vitesseLigne*Math.cos(theta);
		this.posY+=deltaT*vitesseLigne*Math.sin(theta);
	}

	public void breakWheel(){
		//this.vitesseLigneMax=0;
		//this.ordreVitesseLigne=0;
		//this.ordreTheta=this.theta;
		//this.saveOrdreVitesse=0;

		if (! broken) System.out.println(this.toString()+":\t"+getPprocheDistance());
		this.vitesseLigne=0;
		this.broken=true;
	}
	public void freewheel(){broken=false;}

	public void eviter(double theta){
		double angle=((this.theta-theta)%(2*Math.PI)+2*Math.PI)%(2*Math.PI);

		if (getPprocheDistance()>rayonDetect || getPprocheDistance()== -1) setOrdreVitesseLigne(vitesseLigneMax);
		else if (getPprocheDistance()<rayonContact) breakWheel();
		else{
			double relativeMultiplier=(getPprocheDistance()-rayonContact)/(rayonDetect-rayonContact);
			setOrdreVitesseLigne(vitesseLigneMax*relativeMultiplier*0.7+0.1);
			if (voisin==this.getClass()) {
				if (angle < Math.PI) setOrdreTheta(theta + Math.PI * (1 - relativeMultiplier));
				else if (angle > Math.PI) setOrdreTheta(theta - Math.PI * (1 - relativeMultiplier));
			}
			else {
				if (angle<Math.PI/2) setOrdreTheta(theta+2*Math.PI/3);
				else if (angle>3*Math.PI/2) setOrdreTheta(theta-2*Math.PI/3);
			}
		}
	}

	public void mouvInPanel(){
		/**le robot avance linéairement dans un contour fermé et "rebondit" de manière commandée à l'approche d'une bordure**/
		if (broken) return;
		int x= (int) this.getPosX();
		int y= (int) this.getPosY();
		double ordrAngle= this.getOrdreTheta();
		boolean change=false;
		
		//System.out.println("Fourmi en ("+(double) ((int) (x*100))/100+";"+(double) ((int) (y*100))/100+
		//")\ttheta= "+(double) ((int) (fourmi1.getTheta()*100))/100+" ordreTheta: "+(double) ((int) (ordrAngle*100))/100);
		
		if ((x<10 && Math.cos(ordrAngle)<0) || (x>Fenetre.width-30 && Math.cos(ordrAngle)>0)) {ordrAngle=Math.PI-ordrAngle; change=true;}
		if ((y<10 && Math.sin(ordrAngle)<0) || (y>Fenetre.height-30 && Math.sin(ordrAngle)>0)) {ordrAngle=-ordrAngle; change=true;}
		if (change) {
			this.setOrdreTheta(ordrAngle);
		}
		if (x<10 || x>Fenetre.width-30 || y<10 || y>Fenetre.height-30) this.setOrdreVitesseLigne(0.5);


		this.updateMouv(1);
	}

	public void draw(Graphics g){
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(theta + Math.PI/4, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(op.filter(image, null), (int) posX, (int) posY, null);


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
