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
    private static int IDaUpdate=0;
    private static int IDremoved;
    private final static String[] nomsTriggersAbrev = new String[]{"F proche","R proche","fin instruc","OOB", "has info", "F without info"};
    private final static String[] nomsTriggersComplet = new String[]{"antsnextto","ressourcesnextto","lastcomportementfinished", "outofbound", "information", "fourmisansinfo"};
    
    private JButton suppInstructionButton;
    private JLabel IDlabel;
        private int instructionID;
    private JTextField nomInstruction;
    private JComboBox fonctionJ;
        private int nombreParam;
    private JTextField trigParam;
    private int indexTrig;
        private JLabel[] nomFonctionParams;
        private JTextField[] fonctionparams;
    private JPanel triggerPan = new JPanel();//"cases à cocher pour les triggers"
        private JCheckBox[] triggers;
    private JTextField priorite;
    private JComboBox typeLimite;
        private JTextField limite;

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
                        nomFonctionParams= new JLabel[]{new JLabel("t(ms):")};
                        nomFonctionParams[0].setName("time");
                        fonctionparams= new JTextField[]{new JTextField(4)};
                        break;
                    default: nombreParam=0; break;
                }
                affiFonctionParam();
            }
        });

        typeLimite.addActionListener(new ActionListener() {
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
        });
    }

    private  void removeFonctionParam(){for(int i=0; i<2*nombreParam;i++) remove(4);}
    private void affiFonctionParam(){
        for(int i=0; i<nombreParam;i++){
            add(nomFonctionParams[i],4+2*i);
            add(fonctionparams[i],4+2*i+1);
        }
        validate(); repaint();
    }

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
            System.out.println(cbox.getText());
            if(cbox.getText().equals("fin instruc")){
                indexTrig = 2*nombreTrigger-1;
                cbox.addItemListener(new ItemListener() {
                    @Override
                    public void itemStateChanged(ItemEvent e) {
                        if( e.getStateChange() == ItemEvent.SELECTED){
                            trigParam = new JTextField(2);
                            triggerPan.add(trigParam, indexTrig);
                        }else{
                            triggerPan.remove(indexTrig);
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
        add(typeLimite);
    }

    public String[] generateSynthTab(){
        String[] synthTab = new String[6+2*nombreParam+2*nomsTriggersAbrev.length];
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
        //synthTab[6+2*nombreParam]="triggers";
        for(int i = 0; i< nomsTriggersComplet.length; i++){
            synthTab[6+2*nombreParam+2*i]= nomsTriggersComplet[i];
            if(triggers[i].isSelected()) {
                if(triggers[i].getText().equals("fin instruc")) synthTab[7+2*nombreParam+2*i]=trigParam.getText();
                else synthTab[7+2*nombreParam+2*i]="1";
            }
            else if(triggers[i].getText().equals("fin instruc")) synthTab[7+2*nombreParam+2*i]="-1";
            else synthTab[7+2*nombreParam+2*i]="0";
        }
        return synthTab;
    }
    public static int getNombreInstructions() {return nombreInstructions;}
    //public int getInstructionID() {return instructionID;}
    public JButton getSuppInstructionButton() {return suppInstructionButton;}
    //private class IdOverridenException extends Exception{}
    public void decrID(){
        if(IDaUpdate>0 && instructionID>IDremoved){
            instructionID--;
            IDaUpdate--;
        }
        //else throw new IdOverridenException();
    }
    public int delAndUpdate(){
        IDaUpdate=nombreInstructions-instructionID-1;
        nombreInstructions--;
        IDremoved=instructionID;
        return instructionID;
    }
}
