package packRobot;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Panneau extends JPanel {
  private int posX = -50;
  private int posY = -50;
  private int theta = 0;
  public ArrayList<Robot> robots;
  
  public Panneau() {

	  robots = new ArrayList<Robot>();
	    //robots.add(new Robot(30,30,0));
	    //robots.add(new Robot(150,30,0));
	    for(int i = 0; i<1000; i++) {
	    	robots.add(new Robot());
	    }
  }
  
  
  public void paintComponent(Graphics g) {
	  this.setBackground(Color.white);
	  g.setColor(getBackground());
	  g.fillRect(0, 0, this.getWidth(), this.getHeight());
	  for(Robot robot : robots) {
		  	robot.draw(g);
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
	  for(;;){
		 for(Robot robot : robots) {
			    robot.mouvInPanel(1366,768);
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