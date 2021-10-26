package packRobot;

public abstract class Element{
	protected double posX; //horizontal, to the right
	protected double posY; // vertical, downward
	protected double rayon;
	
	public double getPosX(){return this.posX;}
	public double getPosY(){return this.posY;}
	
	double getDistance(Element p){
		return Math.hypot((this.posX-p.posX),(this.posY-p.posY));
	}
	public boolean enContact(Element r2){
		return (getDistance(r2)<(r2.rayon+this.rayon));
	}
	
}
