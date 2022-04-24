package packRobot;

import testxml.InterXml;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

/**
 * Définition physique d'un robot mobile en 2D.</p>
 * Cette classe permet de modifier le déplacement d'un composant par asservissement du premier ordre.
 * Options de collision, évitement et suivi.</p>
 * Les composants sont repérés dans l'espace selon des coordonnées cartésiennes (X,Y).
 * Le déplacement lui est implémenté par modification de l'angle et de la vitesse radiale.
 *
 * @see Robotxml Permet d'ajouter des comportements.
 */
public class Robot extends Element{
	/** Skin du Robot (fourmie)*/
	private BufferedImage image;
	/**
	 * Ouvre un fichier image pour instancier le skin {@link #image} du Robot.
	 * @param pathname Chemin et nom du fichier depuis le dossier principal du projet
	 */
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
	/** Angle d'orientation du Robot. Convention trigonimétrique (rad): 2pi modulo, origin sur le vecteur x
	 * Note: Sur la plupart des PC, l'origine est dans le coin supérieur gauche et le vecteur y est descendant. {@link #theta} augmente donc dans le sens horaire*/
	private double theta;
	/*private double longueur=20;
	private double largeur=15;*/
	/** interpréteur XML pour le caractéristiques physiques du Robot*/
	InterXml Physiquerobot = new InterXml("src/RobotPhysique");
	/** longeur du Robot: dimension selon l'axe colinéaire à son déplacement*/
	private double largeur = Physiquerobot.ReadCompState("taille","largeur");
	/** longeur du Robot: dimension selon l'axe orthogonal à son déplacement*/
	private double longueur = Physiquerobot.ReadCompState("taille","longueur");
	/** Si à true, le Robot est immobilisé. Sinon se déplace selon l'asservissement*/
	private boolean broken = false;
	/** à true si le Robot transporte une ressource (le skin est modifé en conséquence), à false sinon*/
	private boolean carry=false;

	/*commandes d'asservissement*/
	/** Vitesse radiale maximum*/
	public final double vitesseLigneMax;
	/** Vitesse radiale*/
	private double vitesseLigne;
	/** Commande de vitesse radiale*/
	private double ordreVitesseLigne;
	/** Comande d'angle*/
	private double ordreTheta;
	//private double ecartThetaChangement=Math.PI;
	/** Copie de la commande de vitesse radiale*/
	private double saveOrdreVitesse;
	/** Constante de temps pour l'asservissement de l'angle*/
	private int tauRota=20; //valeurs à déterminer ou configurer
	/** Constante de temps pour l'asservissement de la vitesse radiale*/
	private double tauAccel = 0.1;
	/** temps entre deux actualisations de la position*/
	private double deltaT;

	/** Largeur de la zone dans laquelle évoluent les composants {@link Robot} d'une simulation*/
	private static int areaWidth;
	/** Largeur de la zone dans laquelle évoluent les composants {@link Robot} d'une simulation*/
	private static int areaHeight;
	/** Distance maximale d'interaction*/
	public final double FULLRANGE;
	/** Distance médiane d'interaction*/
	public final double MIDRANGE=(rayonContact+rayonDetect)/2;
	/** Distance minimale d'interaction*/
	public final double MINRANGE=Math.min(2*rayonContact,rayonDetect);

	/*geters & seters*/

	/**
	 * Renvoie l'angle theta du Robot. Cet angle est la direction dans laquelle il avance.
	 * Valeur double en radian correspondant à l'angle du Robot dans la base (X,Y)
	 * @return angle theta
	 */
	public double getTheta(){return this.theta;}
	
	/**
	 * Définit l'angle theta du Robot. Cet angle est la direction dans laquelle il avance.
	 * Valeur double en radian correspondant à l'angle du Robot dans la base (X,Y)
	 * @param theta angle d'avancée du Robot
	 */
	public void setTheta(double theta) {this.theta = theta;}

	/**
	 * Renseigne la pésence d'une ressource transportée par le composant.
	 * @return true si le Robot transporte une ressource, false sinon
	 */
	public boolean isCarry() {
		return carry;
	}

	/**
	 * Défini si le Robot transorte une Ressource.
	 * @param carry true si le Robot transporte une ressource, false sinon
	 */
	public void setCarry(boolean carry) {
		this.carry = carry;
	}

	/**
	 * Renvoie la commande de vitesse du Robot pour l'asservissement.
	 * Cette vitesse est comprise dans [0,{@link #vitesseLigneMax}]
	 * @return vitesse de commande pour l'asservissement de la direction.
	 */
	public double getOrdreVitesseLigne(){return ordreVitesseLigne;}

	/**
	 * Renvoie la commande d'angle du Robot pour l'asservissement.
	 * Cet anglee est un double compris dans [0,2{@link Math#PI pi}]
	 * @return Angle de commande pour l'asservissement de la direction.
	 */
	public double getOrdreTheta(){return ordreTheta;}

	/**
	 * Définit la commande de vitesse du Robot pour l'asservissement.
	 * La vitesse finale est comprise dans [0,{@link #vitesseLigneMax}]
	 * @param ordre vitesse de commande pour l'asservissement de la direction.
	 */
	public void setOrdreVitesseLigne(double ordre){
		if (ordre<0) this.ordreVitesseLigne=0;
		else if(ordre>vitesseLigneMax) this.ordreVitesseLigne=vitesseLigne;
		else ordreVitesseLigne=ordre;
		
		saveOrdreVitesse=ordreVitesseLigne;
	}

	/**
	 * Définit la commande d'angle du Robot pour l'asservissement.
	 * L'angle final est stocké sous un double compris dans [0,2{@link Math#PI pi}]
	 * @param ordre angle de commande pour l'asservissement de la direction.
	 */
	public void setOrdreTheta(double ordre){
		this.ordreTheta=(ordre%(2*Math.PI)+2*Math.PI)%(2*Math.PI);
		if (Math.abs(ordreTheta-this.theta)>Math.abs(ordreTheta-(this.theta-2*Math.PI))) this.theta-=2*Math.PI;
		else if (Math.abs(ordreTheta-this.theta)>Math.abs(ordreTheta-(this.theta+2*Math.PI))) this.theta+=2*Math.PI;
		
		//ecartThetaChangement=Math.abs(theta-ordreTheta);
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

	/**
	 * Renvoie la zone de simulation des Robots.
	 * Cette zone en 2 dimensions délimite l'espace dans lequel les Robots peuvent se déplacer.
	 * @return zone de simulation.
	 */
	public static Dimension getArea(){return new Dimension(areaWidth,areaHeight);}
	/**
	 * Affiche la zone de simulation des Robots.
	 * Cette zone en 2 dimensions délimite l'espace dans lequel les Robots peuvent se déplacer.
	 */
	public static void areaPrompt(){System.out.println("Width: "+areaWidth+"\tHeight: "+areaHeight);}

	/**
	 * Renvoie l'état du Robot. Cet état traduit la capacité de se déplacer.
	 * @return true si le Robot est immobilisé, false sinon.
	 */
	public boolean getBroken(){return broken;}


	/*Constructeurs*/
	/**
	 * Crée un nouveau Robot aux coordonnées indiquées.
	 * Par défaut, le Robot va avancer droit devant lui.
	 * @param x position sur l'axe X du Robot
	 * @param y position sur l'axe Y du Robot
	 * @param theta angle du Robot dans la base (X,Y)
	 * @param timeBetweenFrame temps entre deux actualisations de la position et deux rafraichissements de l'affichage
	 */
	public Robot(double x, double y, double theta, long timeBetweenFrame){
		this.posX=x;
		this.posY=y;
		this.theta=theta;
		this.vitesseLigneMax=3;
		this.vitesseLigne= 0;
		this.ordreVitesseLigne=vitesseLigneMax;
		this.saveOrdreVitesse=ordreVitesseLigne;
		this.ordreTheta=theta;
		this.rayonContact = 5;
		this.rayonDetect=rayonContact+10;
		this.deltaT=timeBetweenFrame/25.0;
		this.FULLRANGE=rayonDetect;
		//this.rayonContact=Math.sqrt(Math.pow(this.largeur,2)+Math.pow(this.longueur,2));
		setImage("src/packRobot/Ant2.png");
	}
	/**
	 * Crée un nouveau Robot à des coordonnées aléatoire.
	 * Par défaut, le Robot va avancer droit devant lui.
	 * @param timeBetweenFrame temps entre deux actualisations de la position et deux rafraichissements de l'affichage
	 */
	public Robot(long timeBetweenFrame){
		this.posX=Math.random()*areaWidth;
		this.posY=Math.random()*areaHeight;
		this.theta=Math.random()*2*Math.PI;
		this.deltaT=timeBetweenFrame/25.0;
		this.vitesseLigne= 0;
		this.vitesseLigneMax=3;
		this.ordreVitesseLigne=vitesseLigneMax;
		this.saveOrdreVitesse=ordreVitesseLigne;

		this.ordreTheta=theta;
		this.rayonContact = 5;
		this.rayonDetect= rayonContact+10;
		this.FULLRANGE=rayonDetect;

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
	 */
	public void updateMouv(){

		if(Math.abs(theta-ordreTheta)>0.1) ordreVitesseLigne=0; //a faire avec des exception peut-etre
		else ordreVitesseLigne=saveOrdreVitesse;

		double alphaRota=tauRota/deltaT;
		double alphaAccel=tauAccel/deltaT;
		vitesseLigne=(ordreVitesseLigne+vitesseLigne*alphaAccel)/(1+alphaAccel);
		theta=(ordreTheta+theta*alphaRota)/(1+alphaRota);
		
		this.posX+=deltaT*vitesseLigne*Math.cos(theta);
		this.posY+=deltaT*vitesseLigne*Math.sin(theta);
	}

	/**le robot avance linéairement dans un contour fermé et "rebondit" de manière commandée à l'approche d'une bordure**/
	public void mouvInPanel(){
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
		
		this.updateMouv();
	}

	/**
	 * Rend le Robot immobile
	 */
	public void breakWheel(){
		this.vitesseLigne=0;
		this.broken=true;
	}
	/**
	 * Rend le Robot capable de se déplacer à nouveau.
	 */
	public void freewheel(){broken=false;}

	/**
	 * Modifie la trajectoire et la vitesse pour éviter un obstacle d'un certain angle. Le sens de rotation, et l'angle final qui
	 * en découle, dépendent de la différence entre la direction de l'obstacle et de la direction actuellement suivie par le Robot.
	 * @param thetaObstacle Angle de l'obstacle relativement au Robot, compris dans [0,2{@link Math#PI pi}]
	 * @param thetaDeviation Angle de déviation, indépendante du sens de rotation, compris dans [0,{@link Math#PI pi}]
	 */
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
	/**
	 * Détermine la direction et la vitesse à atteindre pour suivre un élément en restant à mi-chemin entre
	 * la distance de contact et la distance de détection. La vitesse est d'autant plus diminuée que le Robot est proche
	 * de l'Element à suivre.
	 * @param element Element, mobile ou non mobile à suivre.
	 */
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
	/**
	 * Détermine la direction optimale pour éviter un ensemble d'obstacles proches.
	 * @param obstacles liste des obstacles proches à éviter
	 */
	public void eviteNuageObstacle(LinkedList<Element> obstacles){
		//todo
	}

	/**
	 * Dessine le skin du Robot sur un graphique à ses coordonnées relativement à la position de la caméra.
	 * @param g Graphic sur lequel est dessiné le Robot
	 * @param cameraX Position sur l'axe X du coin supérieur gauche de la caméra
	 * @param cameraY Position sur l'axe Y du coin supérieur gauche de la caméra
	 */
	public void draw(Graphics g,int cameraX, int cameraY){
		double locationX = image.getWidth() / 2;
		double locationY = image.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(theta + 3*Math.PI/10, locationX, locationY);
		//System.out.println(locationX + ", " + locationY);
		//System.out.println(image);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		Graphics2D g2d = (Graphics2D) g;
		BufferedImage test = op.filter(image, null);
		g2d.drawImage(test, (int) (posX-longueur/2)-cameraX, (int) (posY-largeur/2)-cameraY, null);
		/*g.setColor(Color.magenta);
		g.drawRect((int) posX, (int) posY, 2,2);
		g.setColor(Color.BLUE);
		g.drawOval((int) (posX-rayonContact), (int) (posY-rayonContact), (int) rayonContact*2, (int) rayonContact*2);*/


	}

	/** fonction test **/
	public static void main(String[] args){
		Robot michel = new Robot(0,0,0,0);
		for(int i=0;i<Integer.parseInt(args[0]);i++){ 
			System.out.println("michel est en ("+michel.posX+";"+michel.posY+")");
			michel.updateMouv();
		}
	}
}
