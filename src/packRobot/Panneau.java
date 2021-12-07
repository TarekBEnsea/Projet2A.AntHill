package packRobot;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.JPanel;

public class Panneau extends JPanel {
	private int posX = -50;
	private int posY = -50;
	private int theta = 0;
	public ArrayList<Robot> robots;
	public ArrayList<Ressources> ressources;
  
	public Panneau() {

	  	robots = new ArrayList<>();
	  	ressources= new ArrayList<>();
		//robots.add(new Robot(30,30,0));
		//robots.add(new Robot(150,30,0));
		for(int i = 0; i<50; i++) {
			robots.add(new Robot());
		}
		for (int j =0; j<15;j++){
			String name;
			if(Math.random() < 0.5) name = "fraise";
			else name = "pdt";
			name="pdt";
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
							//if (fourmi1.enContact(ressource1)) {
							//	  fourmi1.breakWheel();
							//}
							//else {
							fourmi1.eviter(fourmi1.getDirection(ressource1));
							//}
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
}
