package packRobot;

import javax.swing.*;
import java.awt.*;

public class Fenetre extends JFrame{
  private Panneau pan;
  static double width;
  static double height;
  
  public Fenetre(){
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.width = screenSize.getWidth();
    this.height = screenSize.getHeight();

	pan = new Panneau();
    this.setTitle("Animation");
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setUndecorated(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setContentPane(pan);
    this.setVisible(true);
    pan.go();
    
  }
}
  