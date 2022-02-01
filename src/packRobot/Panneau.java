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
	private int cpt_fr = 0;
	private int cpt_fb=0;
	private int cpt_pdt=0;
	public ArrayList<Robotxml> robots;
	public ArrayList<Ressources> ressources;

	public Panneau() {
		this.setFocusable(true);
		this.addKeyListener(this);
	  	robots = new ArrayList<>();
	  	ressources= new ArrayList<>();
		//robots.add(new Robot(30,30,0));
		//robots.add(new Robot(150,30,0));
		for(int i = 0; i<100; i++) {
			robots.add(new Robotxml());
		}
		for (int j =0; j<5;j++){
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
		for(Robot robot : robots) {
			robot.draw(g);
		}
	  	for (Ressources unress : ressources){
		  	unress.draw(g);
	  	}
	}

	/*
  	public void go(){
		Robot fourmi1, fourmi2;
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
			  	for (int j=0; j<ressources.size(); j++){
				  	ressource1=ressources.get(j);
				  	if(fourmi1.enContact(ressource1)){
					  	fourmi1.breakWheel();
						if(ressource1.getTaille() > 5) ressource1.setTaille(ressource1.getTaille()-5);
						//ressources.remove(ressources.size()-1);
						//this.ressources.remove(ressources.size()-1);
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
  	*/

  	public void testgo(){
		Robotxml fourmi1, fourmi2;
		long past = System.currentTimeMillis();
		long duration = 0;


		InterXml comportementsimple = new InterXml("src/testxml/ComportementSimple");


		for(Robotxml robot : robots) {
			robot.setComportement(new Comportement(robot, robots, ressources, comportementsimple)); ;
			robot.getComportement().XMLtoJava();
		}

	  	while(true){
		  	for (int i=0; i<robots.size(); i++){
				fourmi1=robots.get(i);
				String oldName = fourmi1.getComportement().getName();
				fourmi1.setComportement(new Comportement(fourmi1, robots, ressources, comportementsimple));
				String newName = fourmi1.getComportement().getName();

				for (Ressources ressource : ressources) {
					if (fourmi1.enContact(ressource) && !fourmi1.isCarry()) {
						if (ressource.getTaille() > 10) {
							ressource.setTaille(ressource.getTaille() - 10);
							fourmi1.setCarry(true);
							switch (ressource.getName()) {
								case "fraise" -> {
									fourmi1.setImage("src/packRobot/ant+fr.png");
									cpt_fr++;
									System.out.println("Le nombre de fraise récuperée est de : " + cpt_fr);
								}
								case "fb" -> {
									fourmi1.setImage("src/packRobot/ant+fb.png");
									cpt_fb++;
									System.out.println("Le nombre de framboise récuperée est de : " + cpt_fb);
								}
								case "pdt" -> {
									fourmi1.setImage("src/packRobot/ant+pdt.png");
									cpt_pdt++;
									System.out.println("Le nombre de pomme de terre récuperée est de : " + cpt_pdt);
								}
								default -> {
								}
							}
						}
					}
				}

				if(!oldName.equals(newName)){
					fourmi1.getComportement().XMLtoJava();
					System.out.println("changement");
				}
				switch (newName) {
					case "MouvXY", "GoToXY" -> {
						if (fourmi1.AvanceXY()) {
							fourmi1.getComportement().XMLtoJava();
						}
					}
					case "Stop" -> {
						fourmi1.setTime(fourmi1.getTime() - duration);
						if (fourmi1.getTime() < 0) {
							fourmi1.getComportement().XMLtoJava();
						}
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



  	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(1);
		}

	}
}