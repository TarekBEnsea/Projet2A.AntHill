package packRobot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
//import java.awt.Graphics2D;

public class Robot extends Element {
	private BufferedImage image;
	/**position**/
	private int tau=20; //valeur à déterminer ou configurer
	private double theta; //2 pi modulo, sens horaire, origin on x vector
		
	/**commandes**/
	private double vitesseLigneMax=5;
	private double vitesseLigne;
	private double ordreVitesseLigne;
	private double ordreTheta;
	private double ecartThetaChangement=Math.PI;
	private double saveOrdreVitesse;
	
	/**dimensions et hitboxes**/
	private double longueur=20;
	private double largeur=15;
	private double rayon=5;
	
	/**position geters & seters**/
	public double getPosX(){return this.posX;}
	public double getPosY(){return this.posY;}
	public double getTheta(){return this.theta;}
	
	/**commands geters & seters**/
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

	/**Constructeurs**/
	public Robot(double x, double y){
		this.posX=x;
		this.posY=y;
		this.theta=0;
		this.vitesseLigne= 0;
		this.ordreVitesseLigne=0;
		this.saveOrdreVitesse=ordreVitesseLigne;
		this.ordreTheta=Math.PI;
		//this.rayon=Math.sqrt(Math.pow(this.largeur,2)+Math.pow(this.longueur,2));
	}
	public Robot(){
		this.posX=Math.random()*500+00;
		this.posY=Math.random()*500+00;
		this.theta=0;
		this.vitesseLigne= 0;
		this.ordreVitesseLigne=1;
		this.saveOrdreVitesse=ordreVitesseLigne;
		this.ordreTheta=Math.random()*Math.PI;
		//this.rayon=Math.sqrt(Math.pow(this.largeur,2)+Math.pow(this.longueur,2));
		try {
			image = ImageIO.read(new File("src/packRobot/Ant.png"));
		} catch (IOException e) {
			System.out.println("image non cr�er");
		}
	}
	
	/**méthodes**/
	public void updateMouv(double deltaT){
		if(Math.abs(theta-ordreTheta)>ecartThetaChangement*0.05) ordreVitesseLigne=0; //a faire avec des exception peut-etre
		else ordreVitesseLigne=saveOrdreVitesse;
		
		double alpha=tau/deltaT;
		vitesseLigne=(ordreVitesseLigne+vitesseLigne*alpha)/(1+alpha);
		theta=(ordreTheta+theta*alpha)/(1+alpha);
		
		this.posX+=deltaT*vitesseLigne*Math.cos(theta);
		this.posY+=deltaT*vitesseLigne*Math.sin(theta);
	}
	
	public void mouvInPanel(int sizePanX, int sizePanY){
		/**le robot avance linéairement dans un contour fermé et "rebondit" de manière commandée à l'approche d'une bordure**/		
		int x= (int) this.getPosX();
		int y= (int) this.getPosY();
		double ordrAngle= this.getOrdreTheta();
		boolean change=false;
		double angle;
		
		//System.out.println("Fourmi en ("+(double) ((int) (x*100))/100+";"+(double) ((int) (y*100))/100+
		//")\ttheta= "+(double) ((int) (fourmi1.getTheta()*100))/100+" ordreTheta: "+(double) ((int) (ordrAngle*100))/100);
		
		if ((x<220 && Math.cos(ordrAngle)<0) || (x>sizePanX-30 && Math.cos(ordrAngle)>0)) {ordrAngle=Math.PI-ordrAngle; change=true;}
		if ((y<220 && Math.sin(ordrAngle)<0) || (y>sizePanY-30 && Math.sin(ordrAngle)>0)) {ordrAngle=-ordrAngle; change=true;}
		if (change) this.setOrdreTheta(ordrAngle);
		
		this.updateMouv(1);
	}

	public void draw(Graphics g){
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(theta + 45, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(op.filter(image, null), (int) posX, (int) posY, null);


	}
	
	/** fonction test **/
	public static void main(String[] args){
		Robot michel = new Robot(0,0);
		for(int i=0;i<Integer.parseInt(args[0]);i++){ 
			System.out.println("michel est en ("+michel.posX+";"+michel.posY+")");
			michel.updateMouv(1);
		}
	}
}
