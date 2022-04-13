package packRobot;

import javax.swing.*;

/**
 * Panneau permettant de renseigner les conditions de simulation.
 * Permet de modifier les caractéristiques des Robots.
 * Permet de sélectionner la condition de victoire.
 */
public class PhysiqueXML extends JPanel {
    private static final String[] nomsScenarion = new String[]{"*Scénario*","par défaut"};

    private JTextField longueurRobot;
    private JTextField largeurRobot;
    private JTextField nbRessources;
    private JTextField effectifRobot;
    /** Permet de selectionner la modalité de placement des Robots et Ressources dans la fenêtre de simulation*/
    JRadioButton[] typePlacement;
    /** Permet de sélectionner le scénario qui détermine les conditions de simulation et de victoire*/
    private JComboBox<String> scenarioVictoire;

    /**
     * Construit un nouveau panneau {@link JPanel} pour renseigner la physique des Robots
     * et les conditions de simulation avec tous les champs à compléter.
     */
    public PhysiqueXML(){
        effectifRobot = new JTextField(2);
        nbRessources = new JTextField(2);
        longueurRobot = new JTextField(2);
        largeurRobot = new JTextField(2);
        typePlacement = new JRadioButton[]{new JRadioButton("Automatique"),new JRadioButton("Libre")};
        ButtonGroup groupPlacementBoutons = new ButtonGroup();
        for(JRadioButton jb:typePlacement) groupPlacementBoutons.add(jb);
        scenarioVictoire = new JComboBox<>(nomsScenarion);

        insertInPane();
    }

    /**
     * Place tous les sous-composants de la bibliothèque swing dans le panneau {@link JPanel} principal
     */
    private void insertInPane(){
        add(new JLabel("Effectifs"));
        add(effectifRobot);
        add(new JLabel("Ressources"));
        add(nbRessources);
        add(new JLabel("Longueur Robot:"));
        add(longueurRobot);
        add(new JLabel("Largeur Robot:"));
        add(largeurRobot);
        add(new JLabel("Placement:"));
        for(JRadioButton jb:typePlacement) add(jb);
        typePlacement[0].doClick();
    }

    /**
     * Génère un tableau de chaînes de caractères contenant alternativement
     * le nom des paramètres de simulation et leur valeur (! au format String)
     * @return Tableau des paramètres de simulation
     */
    public String[] generateSynthTab(){
        String[] synthTab = new String[2*4];
        synthTab[0]="nbRobot";
        synthTab[1]=effectifRobot.getText();
        synthTab[2]="nbRessources";
        synthTab[3]=nbRessources.getText();
        synthTab[4]="longeurRobot";
        synthTab[5]=longueurRobot.getText();
        synthTab[6]="largeurRobot";
        synthTab[7]=largeurRobot.getText();

        return synthTab;
    }

    /**
     * Renseigne sur la modalité de placement des {@link Element} de simulation
     * @return true si le placement se fait à la souris, false s'il est réalisé automatiquement
     * @throws NullPointerException si aucune des cases n'a été cochée
     */
    public boolean isPlacementFree() throws NullPointerException{
        if(typePlacement[0].isSelected()) return false;
        else if(typePlacement[1].isSelected()) return true;
        else throw new NullPointerException("No Button Selected");
    }
}
