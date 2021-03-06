package packRobot;

import javax.swing.*;
import java.awt.*;

/**
 * Ancienne fenêtre de simulation sans onglets
 * @deprecated
 */
public class Fenetre extends JFrame{
  private Panneau pan;
  static double width;
  static double height;

  /**
   * Classe de création d'une fenêtre pour le lancement de la simulation
   * @throws InterruptedException
   * @deprecated
   */
  public Fenetre() throws InterruptedException {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    this.width = screenSize.getWidth();
    this.height = screenSize.getHeight();
    Robot.initArea((int)width,(int)height);

	pan = new Panneau();
    this.setTitle("Animation");
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    this.setUndecorated(true);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);
    this.setContentPane(pan);
    this.setVisible(true);
    pan.testgo();


  }
}
  
