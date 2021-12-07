package packRobot;

import testxml.InterXml;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class Panneau extends JPanel implements KeyListener {
	private int posX = -50;
	private int posY = -50;
	private int theta = 0;
	public ArrayList<Robot> robots;
	public ArrayList<Ressources> ressources;
  
	public Panneau() {
		this.setFocusable(true);
		this.addKeyListener(this);

	  	robots = new ArrayList<>();
	  	ressources= new ArrayList<>();
		//robots.add(new Robot(30,30,0));
		for(int i = 0; i<50; i++) {
			robots.add(new Robot());
		}
		for (int j =0; j<15;j++){
			String name;
			double p = Math.random();
			if(p < 0.33) name = "fraise";
			else if (p <0.66) name = "pdt";
			else name = "fb";
			ressources.add(new Ressources(name));
		}
	}
  
  
  	public void paintComponent(Graphics g) {
		this.setBackground(Color.white);
		g.setColor(getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
	  	for (Ressources unress : ressources){
		  	unress.draw(g);
	  	}
		for(Robot robot : robots) {
			robot.draw(g);
		}
	}
  
  	public void go(){
		Robot fourmi1, fourmi2;
		Ressources ressource1;
	  	for(;;){
		  	for (int i=0; i<robots.size(); i++){
				fourmi1=robots.get(i);
			  	fourmi1.mouvInPanel();
				fourmi1.resetPprocheDistance();
				fourmi1.setOrdreVitesseLigne(fourmi1.vitesseLigneMax);
				
				if (!fourmi1.getBroken()) {
					for (int j = 0; j < robots.size(); j++) {
						if (i != j) {
							fourmi2 = robots.get(j);
							if (fourmi1.estProche(fourmi2)) {
						  /*if (fourmi1.enContact(fourmi2)) {
							  fourmi1.breakWheel();
							  fourmi2.breakWheel();
						  }*/
								//else {
								fourmi1.eviter(fourmi1.getDirection(fourmi2));
								//fourmi2.eviter(fourmi2.getDirection(fourmi1));
								//}
							}
						}
					}
					
					for (int j = 0; j < ressources.size(); j++) {
						ressource1 = ressources.get(j);
						if (fourmi1.estProche(ressource1)) {
							if (fourmi1.enContact(ressource1)) {
							//	  fourmi1.breakWheel();
								if(ressource1.getTaille() > 5) ressource1.setTaille(ressource1.getTaille()-5);
								//ressources.remove(ressources.size()-1);
								//this.ressources.remove(ressources.size()-1);
							}
							else {
							fourmi1.eviter(fourmi1.getDirection(ressource1));
							}
						}
					}
				}
			}

			this.repaint();

			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
  	}
	
	public void testgo(){
		Robot fourmi1, fourmi2;
		Ressources ressource1;
		long past = System.currentTimeMillis();
		long duration = 0;

		InterXml comportementsimple = new InterXml("src/testxml/ComportementSimple");
		for(Robot robot : robots) {
			for(String comportement : comportementsimple.ReturnXmlNode("Comportement")){
				robot.getListeComportement().add(comportement);
			}
		}

		for(Robot robot : robots) XMLtoJava(robot, robot.getK(), comportementsimple);

	  	while(robots.get(0).getListeComportement().size() > robots.get(0).getK() ){
		  	for (int i=0; i<robots.size(); i++){
				fourmi1=robots.get(i);

				switch (fourmi1.getListeComportement().get(fourmi1.getK())){
					case "MouvXY":
						if(fourmi1.AvanceXY()) {
							fourmi1.setK(fourmi1.getK() + 1);
							XMLtoJava(fourmi1, fourmi1.getK(), comportementsimple);
						};
						break;
					case "GoToXY":
						if(fourmi1.AvanceXY()) {
							fourmi1.setK(fourmi1.getK() + 1);
							XMLtoJava(fourmi1, fourmi1.getK(), comportementsimple);
						};
						break;
					case "Stop":
						fourmi1.setTime(fourmi1.getTime() - duration);
						if(fourmi1.getTime() < 0) {
							fourmi1.setK(fourmi1.getK() + 1);
							XMLtoJava(fourmi1, fourmi1.getK(), comportementsimple);
						}
						
						break;
					default:
						break;
					}
				
				/*fourmi1.setTime(fourmi1.getTime() - duration);
				if(fourmi1.getTime() < 0) {
					fourmi1.setK(fourmi1.getK() + 1);
					fourmi1.setTime(comportementsimple.ReadCompState(fourmi1.getListeComportement().get(fourmi1.getK()), "time"));
				}*/

				for (int j=i+1; j<robots.size(); j++){
				  	fourmi2=robots.get(j);
				  	if(fourmi1.enContact(fourmi2)){
					  	fourmi1.breakWheel();
					  	fourmi2.breakWheel();
				  	}
			  	}
			  	for (int j=0; j<ressources.size(); j++){
				  	ressource1=ressources.get(j);
				  	if(fourmi1.enContact(ressource1)){
					  	fourmi1.breakWheel();
						ressources.remove(ressources.get(j));
				  	}
			  	}
			}

			this.repaint();

			try {
				Thread.sleep(25);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}


			long now = System.currentTimeMillis();
			duration = now - past;
			past = now;
		}
  	}

    public void XMLtoJava(Robot robot, int k, InterXml comportementsimple){
		switch (robot.getListeComportement().get(robot.getK())){
			case "MouvXY":
				robot.setAvanceX(comportementsimple.ReadCompState(robot.getListeComportement().get(k), "x") + robot.getPosX());
				robot.setAvanceY(comportementsimple.ReadCompState(robot.getListeComportement().get(k), "y") + robot.getPosY());
				break;
			case "GoToXY":
				robot.setAvanceX(comportementsimple.ReadCompState(robot.getListeComportement().get(k), "x"));
				robot.setAvanceY(comportementsimple.ReadCompState(robot.getListeComportement().get(k), "y"));
				break;
			case "Stop":
				robot.setTime(comportementsimple.ReadCompState(robot.getListeComportement().get(k), "time"));
			default:
				break;
		}
	}

  	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(1);
		}

	}

}
