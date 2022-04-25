package packRobot;

import testxml.InterXml;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Panneau extends JPanel implements KeyListener, Runnable, MouseListener, MouseMotionListener {
	/*private int posX = -50;
	private int posY = -50;
	private int theta = 0;*/
	private boolean[] touchesDeplacement;
	private int cameraY, cameraX;
	/** type de simulation par défault simulera un déplacement prédéfini de fourmis qui
	* ne prend pas en compte de comportement en XML.
	*/
	private SimulationType simulationType=SimulationType.DEFAULT;
	private int cpt_fr = 0;
	private int cpt_fb=0;
	private int cpt_pdt=0;
	private long timeBetweenFrame=10;
	private InterXml PhysiqueRobot = new InterXml("src/RobotPhysique");
	private int headcount = PhysiqueRobot.ReadCompState("fourmi","effectif");
	private int ressourcesnum = PhysiqueRobot.ReadCompState("fourmi","ressources");
	private boolean theEnd = false;
	private ArrayList<Robotxml> robots;
	private ArrayList<Ressources> resources;
	private ArrayList<Integer> save_resources;
	/** Bordures de la zone de simulation dans laquelle sont censées se déplacer les fourmis*/
	private MapBorder[] borders;
	private Fourmiliere fourmiliere;
	double theta=0;
	private Point click = new Point();
	private Point drag = new Point();
	private boolean placement=false;
	private boolean dragged=false;

	public Panneau() {
		this.setBackground(Color.white);
		this.setFocusable(true);
		this.addKeyListener(this);
	  	robots = new ArrayList<>();
	  	resources= new ArrayList<>();
		save_resources = new ArrayList<>();
		fourmiliere = new Fourmiliere();
		rand_ants(headcount);
		rand_ress(ressourcesnum);
		addMouseListener(this);
		addMouseMotionListener(this);
		touchesDeplacement = new boolean[4];
		for(boolean b : touchesDeplacement) {b = false;}
		borders=new MapBorder[4];
		borders[0]= new MapBorder(BorderSide.TOP,0);
		borders[1]= new MapBorder(BorderSide.RIGHT,Robot.getArea().getWidth());
		borders[2]= new MapBorder(BorderSide.BOTTOM,Robot.getArea().getHeight());
		borders[3]= new MapBorder(BorderSide.LEFT,0);
	}

	/**
	 * Lance une simulation selon le type sélectionné en amont.
	 */
	@Override
	public void run() {
		switch (simulationType){
			case DEFAULT -> {System.out.println("Lancement simulation auto");
				try {go();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			case XMLCONTROLED -> {System.out.println("Lancement simulation XML"); testgo();}
		}
	}

	/**
	 * Change le type de simulation. Cette sélection entre en compte au prochain appel de {@link #run}.
	 * @param simulationType type de simulation sélectionné
	 */
	public void setSimulationType(SimulationType simulationType) {
		this.simulationType = simulationType;
	}

	/**
	 * Repeint l'ensemble des composants de la simulation. Cette fonction repeint les robots et les ressources
	 * ainsi que les bordures de la zone de simulation. Les positions d'affichage sont relatives à une caméra qui peut être déplacée
	 * @see #deplaceCamera
	 * @param g the Graphics object to protect
	 */
	@Override
  	public void paintComponent(Graphics g) {
		int maxX=Robot.getArea().width;
		int maxY=Robot.getArea().height;
		g.setColor(getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawLine(-cameraX,-cameraY,maxX-cameraX,-cameraY);
		g.drawLine(maxX-cameraX,-cameraY,maxX-cameraX,maxY-cameraY);
		g.drawLine(maxX-cameraX,maxY-cameraY,-cameraX,maxY-cameraY);
		g.drawLine(-cameraX,maxY-cameraY,-cameraX,-cameraY);
		for(Robot robot : robots) {robot.draw(g,cameraX,cameraY);}
		for (Ressources unress : resources){unress.draw(g,cameraX,cameraY);}
		fourmiliere.draw(g,cameraX,cameraY);
		
		if(theEnd){
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString("Bravo !", 200, 200);
		}
	}

	/**
	 * Fonction updatant les positions des fourmis à chaque frame
	 * @throws InterruptedException
	 */
  	public void go() throws InterruptedException {
		Robotxml fourmi1, fourmi2;
		for(;;){
		  	for (int i=0; i<robots.size(); i++){
				fourmi1=robots.get(i);
			  	fourmi1.mouvInPanel();

				for (int j=i+1; j<robots.size(); j++){
				  	fourmi2=robots.get(j);
				  	if(fourmi1.enContact(fourmi2)){
					  	fourmi1.breakWheel();
					  	fourmi2.breakWheel();
				  	}
			  	}
				for (Ressources resource : resources) {
					if (resource.getTaille() < 10) {
					save_resources.add(resources.indexOf(resource));
					}
					if (fourmi1.enContact(resource) && !fourmi1.isCarry()) {
						resource.setTaille(resource.getTaille() - 10);
						resource.rayonContact = resource.rayonContact - 5;
						fourmi1.setCarry(true);
						switch (resource.getName()) {
							case "fraise" -> {
								fourmi1.setImage("src/packRobot/ant+fr.png");
							}
							case "fb" -> {
								fourmi1.setImage("src/packRobot/ant+fb.png");
								cpt_fb++;
							}
							case "pdt" -> {
								fourmi1.setImage("src/packRobot/ant+pdt.png");
								cpt_pdt++;
							}
							default -> {
							}
						}
					}
				}
				for (Integer index : save_resources){
					resources.remove(index);
					ressourcesnum-=1;
				}
				save_resources = new ArrayList<>();
			}


			this.repaint();
			Thread.sleep(timeBetweenFrame);
		}
	}

	/**
	 * Fonction initialisant le comportement des fourmis et lancant la fonction {@link #run_simulation(InterXml)} pour chaque frame tant que l'objectif n'est pas atteint.
	 */
	public void testgo() {

		InterXml comportementsimple = new InterXml("src/testxml/ComportementTest.xml");

		for (Robotxml robot : robots) {
			robot.setComportement(new Comportement(robot, robots, resources, comportementsimple, "", -1));
			robot.getComportement().XMLtoJava();
		}

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				run_simulation(comportementsimple);
				if(theEnd){
					this.cancel();
				}
			}
		}, 0, timeBetweenFrame);
	}

	/**
	 * Fonction updatant les caractéristiques des fourmis : leurs positions, leurs comportements, leurs sprites, leurs interaction avec les ressources.
	 * Update la postion de la caméra.
	 * @param comportementsimple fichier XML des comportements
	 */
	private void run_simulation(InterXml comportementsimple) {
		Robotxml fourmi;
		for (int i = 0; i < robots.size(); i++) {
			fourmi = robots.get(i);
			String oldName = fourmi.getComportement().getName();
			Integer oldId = fourmi.getComportement().getId();
			fourmi.setComportement(new Comportement(fourmi, robots, resources, comportementsimple, oldName, oldId));
			String newName = fourmi.getComportement().getName();
			Integer id = fourmi.getComportement().getId();

			for (Ressources resource : resources) {
				if (resource.getTaille() < 10) {
					save_resources.add(resources.indexOf(resource));
					for(Robotxml fourmis : robots){
						if(resource.getPosX() == fourmis.getInf_posx() && resource.getPosY() == fourmis.getInf_posy()){
							fourmis.setInf_posx(-15000);
							fourmis.setInf_posy(-15000);
						}
					}
				}
				if (fourmi.enContact(resource) && !fourmi.isCarry()) {
					resource.setTaille(resource.getTaille() - 10);
					resource.rayonContact = resource.rayonContact - 5;
					fourmi.setCarry(true);
					switch (resource.getName()) {
						case "fraise" -> {
							fourmi.setImage("src/packRobot/ant+fr.png");
							cpt_fr++;
						}
						case "fb" -> {
							fourmi.setImage("src/packRobot/ant+fb.png");
							cpt_fb++;
						}
						case "pdt" -> {
							fourmi.setImage("src/packRobot/ant+pdt.png");
							cpt_pdt++;
						}
						default -> {
						}
					}
				}
			}

			if(fourmi.enContact(fourmiliere)){
				fourmi.setImage("src/packRobot/Ant2.png");
				fourmi.setCarry(false);
			}

			for (Integer index : save_resources) {
				resources.remove((int) index);
				ressourcesnum -= 1;
			}
			theEnd = (ressourcesnum <= 0);
			save_resources.clear();

			if (!oldName.equals(newName)) {
				fourmi.getComportement().XMLtoJava();
				//System.out.println("changement");
			}
			switch (newName) {
				case "MouvXY", "GoToXY" -> {
					if (fourmi.AvanceXY()) {
						//System.out.println(newName +" fini: " + id);
						fourmi.getComportement().setName("");
						fourmi.setLastComportementFinished(id);
					}
				}
				case "GoToElement" -> {
					if (fourmi.AvanceXY()) {
						//System.out.println(newName +" fini: " + id);
						fourmi.getComportement().setName("");
						fourmi.setLastComportementFinished(id);
						if(!fourmi.isCarry()){
							fourmi.setInf_posx(-15000);
							fourmi.setInf_posy(-15000);
						}
					}
				}
				case "Stop" -> {
					fourmi.setTime(fourmi.getTime() - timeBetweenFrame);
					if (fourmi.getTime() < 0) {
						//System.out.println("Stop fini");
						fourmi.getComportement().setName("");
						fourmi.setLastComportementFinished(id);
					}
				}
				case "GetInformation", "Communique" -> {
					fourmi.getComportement().setName("");
					fourmi.setLastComportementFinished(id);
				}
				default -> {
				}
			}

		}
		deplaceCamera();
		this.repaint();
	}

	/**
	 * Place un nombre nb de ressource sur la carte à des positions randomisées.
	 * @param nb nombre de ressource à placer
	 */
	public void rand_ress(int nb) {
		for (int j = 0; j < nb; j++) {
			String name;
			double p = Math.random();
			if (p < 0.33) name = "fraise";
			else if (p < 0.66) name = "pdt";
			else name = "fb";
			resources.add(new Ressources(name));
		}
	}

	/**
	 * Place un nombre nb de fourmis sur la carte à des positions randomisées.
	 * @param nb nombre de fourmis à placer
	 */
	public void rand_ants(int nb){
		for(int i = 0; i<nb; i++) {
			robots.add(new Robotxml(timeBetweenFrame));
		}
	}
	public void activate_placement(){
		placement=true;
	}
	public void desactivate_placement(){
		placement=false;
	}

	@Override
  	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyPressed(KeyEvent e) {
		/*if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(1);
		}
		else*/ switch (e.getKeyCode()){
			case(38): touchesDeplacement[0]=true; break; //go up
			case(37): touchesDeplacement[1]=true; break; //go left
			case(40): touchesDeplacement[2]=true; break; //go down
			case(39): touchesDeplacement[3]=true; break; //go right
		}
	}
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()){
			case(38): touchesDeplacement[0]=false; break; //go up
			case(37): touchesDeplacement[1]=false; break; //go left
			case(40): touchesDeplacement[2]=false; break; //go down
			case(39): touchesDeplacement[3]=false; break; //go right
		}
	}

	/**
	 * Déplace la caméra dans une des 8 directions possibles du plan à l'aide des flèches directionnelles.
	 * Il est possible de se déplacer dans les 4 directions correspondant aux flèches
	 * ainsi que toute combinaison de deux flèches orthogonales.
	 */
	private void deplaceCamera(){
		if(touchesDeplacement[0]) cameraY-=5;
		if(touchesDeplacement[1]) cameraX-=5;
		if(touchesDeplacement[2]) cameraY+=5;
		if(touchesDeplacement[3]) cameraX+=5;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Place soit une ressource soit une fourmi sur la zone sélectionnée de la carte
	 * @param e
	 */
	@Override
	public void mousePressed(MouseEvent e){
		if(placement) {
			if(click.getX() == e.getX() && click.getY() == e.getY()) return;
			else {
				click.setX(e.getX());
				click.setY(e.getY());
			}
			if (e.getButton() == MouseEvent.BUTTON1) {
				Robotxml robot = new Robotxml(e.getX(), e.getY(), 0,timeBetweenFrame);
				robots.add(robot);
				dragged = true;
				this.repaint();
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				String name;
				double p = Math.random();
				if (p < 0.33) name = "fraise";
				else if (p < 0.66) name = "pdt";
				else name = "fb";
				Ressources ress = new Ressources(e.getX(), e.getY(), name);
				resources.add(ress);
				ressourcesnum++;
				dragged = false;
				this.repaint();
			}
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	/**
	 * Permet la rotation de la fourmi au moment de son placement lorsqu'on reste appuyé sur celle-ci
	 * @param e
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		if (dragged && placement) {
			try {
				drag.setX(e.getX());
				drag.setY(e.getY());
				double theta = Math.atan((drag.getY() - click.getY()) / (drag.getX() - click.getX()));
				Robotxml r1 = robots.get(robots.size() - 1);
				if ((drag.getX() - click.getX()) > 0)
					r1.setTheta(theta);
				else r1.setTheta(theta + Math.PI);
				this.repaint();
			} catch (ArithmeticException a) {
			} catch (IndexOutOfBoundsException a) {
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}
}

/**
 * Type de simulation. Correspond au lancement de {@link Panneau#go()} ou {@link Panneau#testgo()}
 */
enum SimulationType{
	DEFAULT,XMLCONTROLED
}
