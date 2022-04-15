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
	private MapBorder[] borders;
	private Fourmiliere fourmiliere;
	double theta=0;
	private Point click = new Point();
	private Point drag = new Point();
	private boolean placement=false;

	public Panneau() {
		this.setFocusable(true);
		this.addKeyListener(this);
	  	robots = new ArrayList<>();
	  	resources= new ArrayList<>();
		save_resources = new ArrayList<>();
		fourmiliere = new Fourmiliere();
		addMouseListener(this);
		addMouseMotionListener(this);
		rand_ants(15);
		rand_ress(10);
		borders=new MapBorder[4];
		borders[0]= new MapBorder(BorderSide.TOP,0);
		borders[1]= new MapBorder(BorderSide.RIGHT,Robot.getArea().getWidth());
		borders[2]= new MapBorder(BorderSide.BOTTOM,Robot.getArea().getHeight());
		borders[3]= new MapBorder(BorderSide.LEFT,0);
	}

	@Override
	public void run() {
		switch (simulationType){
			case DEFAULT -> {System.out.println("ceci n'en est pas");
				try {go();
				} catch (InterruptedException e) {e.printStackTrace();}
			}
			case XMLCONTROLED -> {System.out.println("ceci est du XML"); testgo();}
		}
	}

	public void setSimulationType(SimulationType simulationType) {
		this.simulationType = simulationType;
	}
	
  	public void paintComponent(Graphics g) {
		this.setBackground(Color.white);
		if(theEnd){
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.BOLD, 50));
			g.drawString("Bravo !", 200, 200);
		}
		else{
			g.setColor(getBackground());
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			for(Robot robot : robots) {
				robot.draw(g);
			}
			for (Ressources unress : resources){
				unress.draw(g);
			}
			fourmiliere.draw(g);
		}
	}

  	public void go() throws InterruptedException {
		Robotxml fourmi1, fourmi2;
		Ressources ressource1;
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
			Thread.sleep(25);
		}
	}


	public void testgo() {

		long past = System.currentTimeMillis();
		long duration = 0;


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
						if(! fourmi.isCarry()){
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

			/*fourmi1.setTime(fourmi1.getTime() - duration);

			if(fourmi1.getTime() < 0) {
				fourmi1.setK(fourmi1.getK() + 1);
				fourmi1.setTime(comportementsimple.ReadCompState(fourmi1.getListeComportement().get(fourmi1.getK()), "time"));
			}*/
		}
		this.repaint();
	}
	/*
			try {
				Thread.sleep(timeBetweenFrame);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			long now = System.currentTimeMillis();
			duration = now - past;
			past = now;
		}
	}*/

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
  	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(1);
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e){
		if(placement==true) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				click.setX(e.getX());
				click.setY(e.getY());
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

enum SimulationType{
	DEFAULT,XMLCONTROLED
}