package packRobot;

import javax.swing.*;

public class PhysiqueXML extends JPanel {
    private JTextField Longueur;
    private JTextField  Largeur;
    private JTextField numressources;
    private JTextField Effectifs;

    public PhysiqueXML(){
        numressources = new JTextField(2);
        Effectifs = new JTextField(2);
        numressources = new JTextField(2);
        Longueur = new JTextField(2);
        Largeur = new JTextField(2);
        insertInPane();
    }

    private void insertInPane(){
        add(new JLabel("Effectifs"));
        add(Effectifs);
        add(new JLabel("Ressources"));
        add(numressources);
        add(new JLabel("Longueur Robot:"));
        add(Longueur);
        add(new JLabel("Largeur Robot:"));
        add(Largeur);
    }
}

    