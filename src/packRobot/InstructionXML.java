package packRobot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Bloc instruction à instancier dans l'onglet de programmation simplifié.
 * Contient:
 *      ID+nom d'instruction
 *      tldw
 */
public class InstructionXML extends JPanel {
    private static int nombreInstructions;
    private final static String[] nomsTriggers = new String[]{"capteurAntsnextto","capteurRessourcenextto","capteurLastComportementFinished", "capteuroutofbound"};

    private JLabel IDlabel;
    private int instructionID;
    private JTextField nomInstruction;
    private JComboBox fonctionJ;
    private int nombreParam;
    private JLabel[] nomFonctionParams;
    private JTextField[] fonctionparams;
    private JPanel triggerPan = new JPanel();//"cases à cocher pour les triggers"
    private JCheckBox[] triggers;
    private ItemListener triggersListener;
    private JTextField priorite;
    private JComboBox typeLimite;
    private JTextField limite;

    public InstructionXML() {
        instructionID=nombreInstructions++;
        IDlabel = new JLabel("IID: "+instructionID);
        nomInstruction = new JTextField(8);//setColumns()
        fonctionJ = new JComboBox(new String[]{"*Fonction*","MouvXY", "GoToXY", "Stop"});
        triggers = new JCheckBox[nomsTriggers.length];
        for(int i=0;i<nomsTriggers.length;i++){
            triggers[i] = new JCheckBox(nomsTriggers[i]);
            triggers[i].addItemListener(triggersListener);
        }
        triggersListener = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                Object source = e.getItemSelectable();

                for (int i = 0; i < nomsTriggers.length; i++) {
                    if (source == triggers[i]) {
                        // nomsTriggers[i];
                        break;
                    }
                }
            }
        };
        priorite = new JTextField(2);
        typeLimite = new JComboBox<>(new String[]{"*Limite*","None","temps(ms)","distance(p)"});
        limite = new JTextField(3);

        initFonction();
        insertInPane();
    }

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
                        nomFonctionParams= new JLabel[]{new JLabel("t(ms): ")};
                        nomFonctionParams[0].setName("time");
                        fonctionparams= new JTextField[]{new JTextField(4)};
                    default: nombreParam=0; break;
                }
                affiFonctionParam();
            }
        });

        typeLimite.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedItem=typeLimite.getSelectedItem().toString();
                try{remove(8+2*nombreParam);} catch (ArrayIndexOutOfBoundsException exception) {}
                switch (selectedItem){
                    case "None","*Limite*": break;
                    default: add(limite); break;
                }
                validate(); repaint();
            }
        });
    }

    private  void removeFonctionParam(){for(int i=0; i<2*nombreParam;i++) remove(3);}
    private void affiFonctionParam(){
        for(int i=0; i<nombreParam;i++){
            add(nomFonctionParams[i],3+2*i);
            add(fonctionparams[i],3+2*i+1);
        }
        validate(); repaint();
    }

    private void insertInPane(){
        add(IDlabel);
        add(nomInstruction);
        add(fonctionJ);
        add(new JLabel("Triggers"));
        for(JCheckBox cbox : triggers){
            triggerPan.add(new JLabel("&"));
            triggerPan.add(cbox);
        }
        triggerPan.remove(0);
        add(triggerPan);
        add(new JLabel("Priorité:"));
        add(priorite);
        add(typeLimite);
    }

    public String[] generateSynthTab(){
        String[] efe = new String[]{"name","MoveXY","x","42","y","12","id","0","priority","20","triggers","0","0","0"};
        String[] synthTab = new String[6+2*nombreParam+2*nomsTriggers.length];
        synthTab[0]="name";
        synthTab[1]=fonctionJ.getSelectedItem().toString();
        for(int i=0;i<nombreParam;i++){
            synthTab[2+2*i]=nomFonctionParams[i].getName();
            synthTab[3+2*i]=fonctionparams[i].getText();
        }
        synthTab[2+2*nombreParam]="id";
        synthTab[3+2*nombreParam]= Integer.valueOf(instructionID).toString();
        synthTab[4+2*nombreParam]="priority";
        synthTab[5+2*nombreParam]= priorite.getText();
        for(int i=0;i<nomsTriggers.length;i++){
            synthTab[6+2*nombreParam+2*i]=nomsTriggers[i];
            if(triggers[i].isSelected()) synthTab[7+2*nombreParam+2*i]="1";
            else synthTab[7+2*nombreParam+2*i]="0";
        }
        return synthTab;
    }
    public static int getNombreInstructions() {return nombreInstructions;}
    public void supprInstruction(){
        //to do
    }

    //public
}
