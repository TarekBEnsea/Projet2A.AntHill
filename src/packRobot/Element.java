package packRobot;

public abstract class Element{
	protected double posX; //horizontal, to the right
	protected double posY; // vertical, downward
	protected double rayonContact=0;
	protected double rayonDetect=0;
	private double pprocheDistance; //distance du plus proche obstacle
	public Class voisin;

	public double getPosX(){return this.posX;}
	public double getPosY(){return this.posY;}
	public double getPprocheDistance(){return pprocheDistance;}
	public void resetPprocheDistance(){pprocheDistance= -1;}
	
	public double getDistance(Element p){
		return Math.hypot((this.posX-p.posX),(this.posY-p.posY));
	}
	public double getDistance(MapBorder b){return b.getDistance(this);}
	public double getDirection(MapBorder b){return b.getDirection(this);}

	/**
	 * Renvoie la direction d'un obstacle dans l'inveralle [-pi,pi].
	 * @param p Element visé.
	 * @return L'angle dans lequel se trouve l'element.
	 */
	public double getDirection(Element p){
		double x=p.posX-this.posX;
		double y=p.posY-this.posY;
		double angl=Math.acos(x/(pprocheDistance+p.rayonContact));
		return angl*Math.signum(y/(pprocheDistance+p.rayonContact));
	}
	public double getDirection(Element p, double distance){
		double x=p.posX-this.posX;
		double y=p.posY-this.posY;
		double angl=Math.acos(x/distance);
		return angl*Math.signum(y/distance);
	}
	public boolean enContact(Element r2){
		return (getDistance(r2)<(r2.rayonContact +this.rayonContact));
	}
	public boolean estProche(Element r2, boolean ignoreOthers){
		double distance=getDistance(r2)-r2.rayonContact;
		if (distance<pprocheDistance || pprocheDistance== -1){
			pprocheDistance=distance;
			voisin=r2.getClass();
			return  (distance < this.rayonDetect);
		}
		else return (ignoreOthers && distance<this.rayonDetect);
	}
	public boolean estProche(MapBorder b, boolean ignoreOthers){
		double distance=Math.abs(getDistance(b)-b.rayonContact);
		if (distance<pprocheDistance || pprocheDistance== -1){
			pprocheDistance=distance;
			voisin=b.getClass();
			return  (distance < this.rayonDetect);
		}
		else return (ignoreOthers && distance<this.rayonDetect);
	}
}
