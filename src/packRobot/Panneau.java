package packRobot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Panneau extends JPanel implements KeyListener {

	private int posX = -50;
	private int posY = -50;
	private int theta = 0;
	private int cpt_fr = 0;
	private int cpt_fb=0;
	private int cpt_pdt=0;
	public ArrayList<Robot> robots;
	public ArrayList<Ressources> ressources;

	public Panneau() {
		this.setFocusable(true);
		this.addKeyListener(this);
		robots = new ArrayList<>();
		ressources = new ArrayList<>();
		for (int i = 0; i < 200; i++) {
			robots.add(new Robot());
		}
		for (int j = 0; j < 4; j++) {
			String name;
			double p = Math.random();
			if (p < 0.33) name = "fraise";
			else if (p < 0.66) name = "pdt";
			else name = "fb";
			ressources.add(new Ressources(name));
		}
	}

	public void paintComponent(Graphics g) {
		this.setBackground(Color.white);
		g.setColor(getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (Robot robot : robots) {
			robot.draw(g);
		}
		for (Ressources unress : ressources) {
			unress.draw(g);
		}
	}

	public void go() {
		Robot fourmi1, fourmi2;
		Ressources ressource1;
		for (; ; ) {
			for (int i = 0; i < robots.size(); i++) {
				fourmi1 = robots.get(i);
				fourmi1.mouvInPanel();

				for (int j = i + 1; j < robots.size(); j++) {
					fourmi2 = robots.get(j);
					if (fourmi1.enContact(fourmi2)) {
						//fourmi1.breakWheel();
						//fourmi2.breakWheel();
					}
				}
				for (int j = 0; j < ressources.size(); j++) {
					ressource1 = ressources.get(j);
					if (fourmi1.enContact(ressource1) && !fourmi1.isCarry()) {
						if (ressource1.getTaille() > 10) {
							ressource1.setTaille(ressource1.getTaille() - 10);
							fourmi1.setCarry(true);
							switch (ressource1.getName()) {
								case "fraise":
									fourmi1.setImage("src/packRobot/ant+fr.png");
									cpt_fr++;
									System.out.println("Le nombre de fraise récuperée est de : " + cpt_fr);
									break;
								case "fb":
									fourmi1.setImage("src/packRobot/ant+fb.png");
									cpt_fb++;
									System.out.println("Le nombre de framboise récuperée est de : " + cpt_fb);
									break;
								case "pdt":
									fourmi1.setImage("src/packRobot/ant+pdt.png");
									cpt_pdt++;
									System.out.println("Le nombre de pomme de terre récuperée est de : " + cpt_pdt);
									break;
								default:
									break;
							}
						}
						else ressource1 = null;
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

	public void keyTyped(KeyEvent e) {
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			System.exit(1);
		}

	}

}