package packRobot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Bloc instruction à instancier dans l'onglet de programmation simplifié.
 * Chaque instance correspond à une fonction déclenchée selon certains capteurs et au dessous d'une
 * certaine priorité
 */
public class InstructionXML extends JPanel {
    /** Nombre total d'instructions instanciées*/
    private static int nombreInstructions;
    /** Nombre d'instruction dont il faut actualiser le numéro d'ID en cas de suppression*/
    private static int IDaUpdate=0;
    /** Dernier numéro d'ID supprimé. Cet ID doit être remplacé par l'ID immédiatement supérieur sauf si
     * il s'agissait du dernier*/
    private static int IDremoved;

    /** Nom des triggers tels qu'affichés dans l'interface*/
    private final static String[] nomsTriggersAbrev = new String[]{"F proche","R proche","fin instruc","OOB", "has info", "F without info", "is carrying"};
    /** Nom des triggers correspondants à leur balise XML*/
    private final static String[] nomsTriggersComplet = new String[]{"antsnextto","ressourcesnextto","lastcomportementfinished", "outofbound", "information", "fourmisansinfo", "iscarrying"};
    
    /** Bouton de suppression d'instruction*/
    private JButton suppInstructionButton;
    /** Label affichant le numéro d'ID*/
    private JLabel IDlabel;
        /** numéro d'ID d'instruction. Celui-ci est unique parmis toutes les instructions encore instanciées*/
        private int instructionID;
    /** Nom de l'instruction*/
    private JTextField nomInstruction;
    /** Boite de sélection de la fonction à exécuter par l'instruction*/
    private JComboBox fonctionJ;
        /** Nombre de paramètres à renseigner pour exécuter la fonction*/
        private int nombreParam;
        /** nom des paramètres*/
        private JLabel[] nomFonctionParams;
        /** valeur des paramètres*/
        private JTextField[] fonctionparams;
    /** Panneau pour sélectionner les triggers (capteurs à valider pour exécuter l'instruction)*/
    private JPanel triggerPan = new JPanel();//"cases à cocher pour les triggers"
        /** Tableau contenant les différentes boîtes de selection des triggers*/
        private JCheckBox[] triggers;
        /** zone pour renseigner l'ID de l'instruction à surveiller. 
         * Le capteur est actif (permet le lancement de l'instruction) quand l'instruction en question s'est terminée*/
        private JTextField finInstructBox;
        /** Indice de {@link #finInstructBox} parmis les {@link JComponent composant} de {@link #triggerPan}*/
        private int finInstructIndex;
    /** Champ pour renseigner la priorité, sous forme d'entiers naturels
     * 0 est la priorité la plus forte, +inifity la priorité la plus faible*/
    private JTextField priorite;
    /*private JComboBox typeLimite;
        private JTextField limite;*/

    /**
     * Crée un nouveau panneau de remplissage d'une instruction.
     * Les différents champs peuvent être remplis ou sélectionnés.
     * Cette instruction peut aussi être supprimée.
     */
    public InstructionXML() {
        instructionID=nombreInstructions++;
        suppInstructionButton = new JButton("-");
        IDlabel = new JLabel("IID: "+instructionID);
        nomInstruction = new JTextField(8);//setColumns()
        fonctionJ = new JComboBox(new String[]{"*Fonction*","MouvXY", "GoToXY", "GoToElement", "Stop", "Communique", "GetInformation"});
        triggers = new JCheckBox[nomsTriggersAbrev.length];
        for(int i = 0; i< nomsTriggersAbrev.length; i++){
            triggers[i] = new JCheckBox(nomsTriggersAbrev[i]);
        }
        priorite = new JTextField(2);
        /*typeLimite = new JComboBox<>(new String[]{"*Limite*","None","temps(ms)","distance(p)"});
        limite = new JTextField(3);*/

        initFonction();
        insertInPane();
    }

    /** Initialise les différentes actions exécutées lors de la sélection d'un item.
     * Ces actions permettent de faire apparaître les champs à remplir lorsque c'est nécessaire.*/
    private void initFonction(){
        fonctionJ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeFonctionParam();
                String selectedItem=fonctionJ.getSelectedItem().toString();
                switch (selectedItem) {
                    case "MouvXY":
                    case "GoToXY":
                        nombreParam=2;
                        nomFonctionParams= new JLabel[]{new JLabel("X:"), new JLabel("Y:")};
                        nomFonctionParams[0].setName("x");
                        nomFonctionParams[1].setName("y");
                        fonctionparams= new JTextField[]{new JTextField(3),new JTextField(3)};
                        break;
                    case "Stop":
                        nombreParam=1;
                        nomFonctionParams= new JLabel[]{new JLabel("t(ms):")};
                        nomFonctionParams[0].setName("time");
                        fonctionparams= new JTextField[]{new JTextField(4)};
                        break;
                    default: nombreParam=0; break;
                }
                affiFonctionParam();
            }
        });

        /*typeLimite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem=typeLimite.getSelectedItem().toString();
                try{remove(9+2*nombreParam);} catch (ArrayIndexOutOfBoundsException exception) {}
                switch (selectedItem){
                    case "None","*Limite*": break;
                    default: add(limite); break;
                }
                validate(); repaint();
            }
        });*/
    }

    /** Retire de l'affichage les paramètres de la fonction précédemment sélectionnée*/
    private  void removeFonctionParam(){for(int i=0; i<2*nombreParam;i++) remove(4);}
    private void affiFonctionParam(){
        for(int i=0; i<nombreParam;i++){
            add(nomFonctionParams[i],4+2*i);
            add(fonctionparams[i],4+2*i+1);
        }
        validate(); repaint();
    }

    /** Ajoute les différents composants de l'instruction au {@link JPanel panneau} principal*/
    private void insertInPane(){
        add(suppInstructionButton);
        add(IDlabel);
        add(nomInstruction);
        add(fonctionJ);
        add(new JLabel("Triggers"));
        int nombreTrigger = 0;
        for(JCheckBox cbox : triggers){
            triggerPan.add(new JLabel("&"));
            triggerPan.add(cbox);
            nombreTrigger+=1;
            if(cbox.getText().equals("fin instruc")){
                finInstructIndex = 2*nombreTrigger-1;
                cbox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if( e.getStateChange() == ItemEvent.SELECTED){
                            finInstructBox = new JTextField(2);
                            triggerPan.add(finInstructBox, finInstructIndex);
                        }else{
                            triggerPan.remove(finInstructIndex);
                        }
                        validate();
                    }
                });
            }
        }
        triggerPan.remove(0);
        add(triggerPan);
        add(new JLabel("Priorité:"));
        add(priorite);
        //add(typeLimite);
    }

    /**
     * Génère un tableau contenant les noms des paramètres de l'instruction ainsi que leur valeur.
     * Chaque couple commence par le nom du paramètre, qui est le même que le nom de la balise XML, puis par la valeur.
     * @return Tableau des paramètres
     */
    public String[] generateSynthTab(){
        String[] synthTab = new String[6+2*nombreParam+2*nomsTriggersAbrev.length];
        synthTab[0]="name";
        try{
            synthTab[1]=fonctionJ.getSelectedItem().toString();
        } catch (NullPointerException e) {return null;}
        for(int i=0;i<nombreParam;i++){
            synthTab[2+2*i]=nomFonctionParams[i].getName();
            synthTab[3+2*i]=fonctionparams[i].getText();
        }
        synthTab[2+2*nombreParam]="id";
        synthTab[3+2*nombreParam]= Integer.valueOf(instructionID).toString();
        synthTab[4+2*nombreParam]="priority";
        synthTab[5+2*nombreParam]= priorite.getText();
        //synthTab[6+2*nombreParam]="triggers";
        for(int i = 0; i< nomsTriggersComplet.length; i++){
            synthTab[6+2*nombreParam+2*i]= nomsTriggersComplet[i];
            if(triggers[i].isSelected()) {
                if(triggers[i].getText().equals("fin instruc")) synthTab[7+2*nombreParam+2*i]=finInstructBox.getText();
                else synthTab[7+2*nombreParam+2*i]="1";
            }
            else if(triggers[i].getText().equals("fin instruc")) synthTab[7+2*nombreParam+2*i]="-1";
            else synthTab[7+2*nombreParam+2*i]="0";
        }
        return synthTab;
    }

    /**
     * Renvoie le nombre d'instructions actuellement instanciées.
     * @return nombre d'instructions instanciées
     */
    public static int getNombreInstructions() {return nombreInstructions;}
    //public int getInstructionID() {return instructionID;}

    /**
     * Renvoie le bouton de suppression d'instruction afin de pouvoir récupérer la fonction exécutée par son click.
     * @return bouton de suppression d'instruction.
     */
    public JButton getSuppInstructionButton() {return suppInstructionButton;}
    //private class IdOverridenException extends Exception{}

    /**
     * Diminue lorsque cela est autorisé l'ID de l'instruction.
     * Cette fonction doit être appelée après la suppression d'une instruction.
     */
    public void decrID(){
        if(IDaUpdate>0 && instructionID>IDremoved){
            instructionID--;
            IDaUpdate--;
            IDlabel.setText("IID: "+instructionID);
        }
        //else throw new IdOverridenException();
    }

    /**
     * Enregistre le nom de l'instruction supprimée afin d'actualiser les ID supérieurs.
     * @return l'instruction supprimée
     */
    public int delAndUpdate(){
        IDaUpdate=nombreInstructions-instructionID-1;
        nombreInstructions--;
        IDremoved=instructionID;
        return instructionID;
    }
}
