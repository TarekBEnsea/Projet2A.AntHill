package packRobot;

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
		//robots.add(new Robot(150,30,0));
		for(int i = 0; i<30; i++) {
			robots.add(new Robot());
		}
		for (int j =0; j<10;j++){
			String name;
			if(Math.random() < 0.5) name = "fraise";
			else name = "pdt";
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

  	public void keyTyped(KeyEvent e) {}
	public void keyPressed(KeyEvent e) {}
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(1);
		}

	}
}