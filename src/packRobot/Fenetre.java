package packRobot;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Fenetre extends JFrame{
  private Panneau pan;
  
  public Fenetre(){  

	pan = new Panneau();
    this.setTitle("Animation");
    this.setSize(300, 300);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setContentPane(pan);
    this.setVisible(true);
    pan.go();
    
  }
}
  