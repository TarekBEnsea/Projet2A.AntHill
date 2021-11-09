package packRobot;

import java.awt.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.*;

public class Panneau extends JPanel implements KeyListener {
  private int theta = 0;
  public ArrayList<Robot> robots;
  public ArrayList<Ressources> ressources;
  JLabel label;
  JTextField text;

  
  public Panneau() {
	  label = new JLabel();
	  text = new JTextField();
	  this.add(label);
	  this.add(text);
	  text.addKeyListener(this);
	  robots = new ArrayList<Robot>();
	  ressources= new ArrayList<Ressources>();
	    for(int i = 0; i<500; i++) {
	    	robots.add(new Robot());
	    }
	  for (int j =0; j<30;j++){
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
			}
		this.repaint();
		
	    try {
	      Thread.sleep(25);
	    } catch (InterruptedException e) {
	      e.printStackTrace();
	    }
	  }
  }
  public void keyTyped(KeyEvent e){}
  public void keyReleased(KeyEvent e){}
  public void keyPressed(KeyEvent e){
	  int key = e.getKeyCode();
	  System.out.println("key pressed");
	  if(key == KeyEvent.VK_ESCAPE){
		  System.out.println("escape key pressed");
		  System.exit(1);
	  }
  }

}