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

	  robots = new ArrayList<Robot>();
	  ressources= new ArrayList<Ressources>();
	    //robots.add(new Robot(30,30,0));
	    //robots.add(new Robot(150,30,0));
	    for(int i = 0; i<100; i++) {
	    	robots.add(new Robot());
	    }
	  for (int j =0; j<50;j++){
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
  


  public int getTheta() {
	return theta;
}

public void setTheta(int theta) {
	this.theta = theta;
}

public int getPosX() {
    return posX;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public int getPosY() {
    return posY;
  }

  public void setPosY(int posY) {
    this.posY = posY;
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