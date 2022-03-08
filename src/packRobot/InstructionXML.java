package packRobot;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

/**
 * Bloc instruction à instancier dans l'onglet de programmation simplifié.
 * Contient:
 *      ID+nom d'instruction
 *      tldw
 */
public class InstructionXML extends JPanel {
    private static int nombreInstructions;

    private JLabel IDlabel;
        private int instructionID;
    private JTextField nomInstruction;
    private JComboBox fonctionJ;
        private int nombreParam;
        private JLabel[] nomFonctionParams;
        private JTextField[] fonctionparams;
        //private JTextComponent
    //private "cases à cocher pour les triggers"
    private JTextField priorite;
    private JComboBox typeLimite;
        private JTextField limite;

    public InstructionXML() {
        instructionID=nombreInstructions++;
        IDlabel = new JLabel("IID: "+instructionID);
        nomInstruction = new JTextField(8);//setColumns()
        fonctionJ = new JComboBox(new String[]{"*Fonction*","aaa", "bbb"});
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
                    case "aaa":
                        nombreParam=1;
                        nomFonctionParams= new JLabel[]{new JLabel("t(ms):")};
                        fonctionparams= new JTextField[]{new JTextField(4)};
                        break;
                    case "bbb":
                        nombreParam=2;
                        nomFonctionParams= new JLabel[]{new JLabel("X:"), new JLabel("Y:")};
                        fonctionparams= new JTextField[]{new JTextField(3),new JTextField(3)};
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
                try{remove(5+2*nombreParam);} catch (ArrayIndexOutOfBoundsException exception) {}
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
        //add "triggers"
        add(priorite);
        add(typeLimite);
    }

    public static int getNombreInstructions() {return nombreInstructions;}
    public void supprInstruction(){
        //to do
    }

    //public
}
