package packRobot;

public abstract class Element{
	protected double posX; //horizontal, to the right
	protected double posY; // vertical, downward
	protected double rayonContact;
	protected double rayonDetect;
	protected double rayon;
	private double pprocheDistance; //distance du plus proche obstacle
	public Class voisin;

	public double getPosX(){return this.posX;}
	public double getPosY(){return this.posY;}
	public double getPprocheDistance(){return pprocheDistance;}
	public void resetPprocheDistance(){pprocheDistance= -1;}
	
	public double getDistance(Element p){
		return Math.hypot((this.posX-p.posX),(this.posY-p.posY));
	}
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
	public boolean estProche(Element r2){
		//pprocheDistance=getDistance(r2);
		//return (getDistance(r2)<(r2.rayonContact +this.rayonDetect));

		double distance=getDistance(r2)-r2.rayonContact;
		if (distance<pprocheDistance || pprocheDistance== -1){
			pprocheDistance=distance;
			voisin=r2.getClass();
			return  (distance < this.rayonDetect);
		}
		else return false;
	}
}
