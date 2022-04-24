package packRobot;

/**
 * Classe de base pour tous les composants de la simulation.
 * Gère les positions absolue, relative et les distances.
 */
public abstract class Element{
	/** Position de l'Element selon l'axe X (horizontal)*/
	protected double posX; //horizontal, to the right
	/** Position de l'Element selon l'axe Y (vertical)*/
	protected double posY; // vertical, downward
	/** Distance correspondant aux dimensions physiques de l'objet */
	protected double rayonContact=0;
	/** Distance correspondant à la possibilité de détecter la présence d'objets dans un rayon relativement proche*/
	protected double rayonDetect=0;
	/**  distance du plus proche obstacle*/
	private double pprocheDistance;
	/**  Classe du plus proche obstacle*/
	public Class voisin;

	/** Retourne la projection orthogonale de la position de l'Element sur l'axe X. Cette coordonnée correspond à la distance
	 * sur l'axe horizontal en partant du coin supérieur gauche de la zone de simulation.
	 * @return la position selon l'axe X de l'Element.
	 */
	public double getPosX(){return this.posX;}
	/** Retourne la projection orthogonale de la position de l'Element sur l'axe Y. Cette coordonnée correspond à la distance
	 * sur l'axe vertical en partant du coin supérieur gauche de la zone de simulation.
	 * @return la position selon l'axe Y de l'Element.
	 */
	public double getPosY(){return this.posY;}
	/**
	 * Renvoie la distance entre l'Element et le plus proche obstacle.
	 * Cet obstacle est défini par des appels de {@link #estProche} sur l'ensemble des obstacles possibles.
	 * @return distance au plus proche obstacle.
	 */
	public double getPprocheDistance(){return pprocheDistance;}
	/**
	 * Réinitialise la distance au plus proche obstacle {@link #pprocheDistance}.
	 * Pour cette valeur, les obstacles sont considérés à l'infini.
	 */
	public void resetPprocheDistance(){pprocheDistance= -1;}

	/**
	 * Renvoie la distance entre le centres du l'élément et le centre de ce composant.
	 * @param p Élément dont on veut la distance
	 * @return distance à l'élément en argument
	 */
	public double getDistance(Element p){
		return Math.hypot((this.posX-p.posX),(this.posY-p.posY));
	}
	/**
	 * Renvoie la distance entre la bordure et le centre de ce composant.
	 * Cette fonction est équivalente à {@link packRobot.MapBorder#getDistance(Element)}.
	 * @param b Bordure dont on veut la direction
	 * @return La direction relative de l'élément en argument
	 */
	public double getDistance(MapBorder b){return b.getDistance(this);}
	/**
	 * Renvoie la direction d'une bordure dans l'inveralle [0,2{@link Math#PI pi}].
	 * Cette fonction est équivalente à {@link packRobot.MapBorder#getDirection(Element)}+pi.
	 * @param b Bordure visé.
	 * @return L'angle dans lequel se trouve la bordure.
	 */
	public double getDirection(MapBorder b){return Math.PI+b.getDirection(this);}

	/**
	 * Renvoie la direction d'un obstacle dans l'inveralle [-{@link Math#PI pi},{@link Math#PI pi}].
	 * @param p Element visé.
	 * @return L'angle dans lequel se trouve l'element.
	 */
	public double getDirection(Element p){
		double x=p.posX-this.posX;
		double y=p.posY-this.posY;
		double distance = getDistance(p);
		//double angl=Math.acos(x/(pprocheDistance+p.rayonContact));
		double angl=Math.acos(x/distance);
		if(Math.signum(y)!=0) return angl*Math.signum(y);
		else return angl;
	}
	/*public double getDirection(Element p, double distance){
		double x=p.posX-this.posX;
		double y=p.posY-this.posY;
		double angl=Math.acos(x/distance);
		return angl*Math.signum(y/distance);
	}*/

	/**
	 * Retourne si le composant et l'élément en paramètre sont en contact.
	 * @param r2 Élément à tester
	 * @return true si les surfaces de contact des deux éléments se chevauchent, false sinon
	 */
	public boolean enContact(Element r2){
		return (getDistance(r2)<(r2.rayonContact +this.rayonContact));
	}
	/**
	 * Retourne si le composant et l'élément en paramètre sont en contact.
	 * @param b Bordure à tester
	 * @return true si la surface de contact du composant recouvre une partie de la bordure en paramètre, false sinon
	 */
	public boolean enContact(MapBorder b){
		return (getDistance(b)<(b.rayonContact +this.rayonContact));
	}
	/**
	 * Retourne si la bordure en paramètre est détecté comme proche par le composant.
	 * @param r2 Élément à tester à tester
	 * @param ignoreOthers si à true, cette méthode est équivalente à {@link #estProche(Element r2)}, sinon renvoie si l'objet est le plus proche détecté
	 * @return true si la surface de contact du paramètre chevauchent la surface de détection du composant, false sinon
	 */
	public boolean estProche(Element r2, boolean ignoreOthers){
		double distance=getDistance(r2)-r2.rayonContact;
		if (distance<pprocheDistance || pprocheDistance== -1){
			pprocheDistance=distance;
			voisin=r2.getClass();
			return  (distance < this.rayonDetect);
		}
		else return (ignoreOthers && distance<this.rayonDetect);
	}
	/**
	 * Retourne si la bordure en paramètre est détecté comme proche par le composant.
	 * @param b Bordure à tester
	 * @param ignoreOthers si à true, cette méthode est equivalente à {@link #estProche(MapBorder b)}, sinon renvoie si l'objet est le plus proche détecté
	 * @return true si la surface de détection du composant recouvre une partie de la bordure en paramètre, false sinon
	 */
	public boolean estProche(MapBorder b, boolean ignoreOthers){
		double distance=Math.abs(getDistance(b)-b.rayonContact);
		if (distance<pprocheDistance || pprocheDistance== -1){
			pprocheDistance=distance;
			voisin=b.getClass();
			return  (distance < this.rayonDetect);
		}
		else return (ignoreOthers && distance<this.rayonDetect);
	}

	/**
	 * Retourne si l'élément en paramètre est détecté comme proche par le composant.
	 * @param r2 Élément à tester
	 * @return true si la surface de contact du paramètre chevauchent la surface de détection du composant, false sinon
	 */
	public boolean estProche(Element r2){
		double distance=getDistance(r2)-r2.rayonContact;
		return distance<this.rayonDetect;
	}
	/**
	 * Retourne si la bordure en paramètre est détecté comme proche par le composant.
	 * @param b Bordure à tester
	 * @return true si la surface de détection du composant recouvre une partie de la bordure en paramètre, false sinon
	 */
	public boolean estProche(MapBorder b){
		double distance=Math.abs(getDistance(b)-b.rayonContact);
		return distance<this.rayonDetect;
	}
}
