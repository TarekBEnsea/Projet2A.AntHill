package packRobot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class MainWindow extends JFrame{
    private JPanel progUserTAB = new JPanel();
        private JPanel instructionsPan = new JPanel();
        private LinkedList<InstructionXML> listeInstructions = new LinkedList<InstructionXML>();
    private JPanel progXMLtextTAB = new JPanel();
        private JPanel boutonsXMLPAN = new JPanel();
            //private JButton[] boutonsXML;
        private JTextArea xmlProgArea= new JTextArea();
    private JPanel simulation1TAB ;//= new JPanel();
    private JPanel simulation2TAB ;//= new JPanel();

    private int frameWidth;
    private int frameHeight;



    public MainWindow(String saveFilename){ //"monProgXml"
        super("AntHill Custom Simulation Project Valpha 0.1");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameWidth=(int) screenSize.getWidth();
        frameHeight=(int) screenSize.getHeight();
        Robot.initArea(this.frameWidth/4,this.frameHeight/4);

        initProgPane();
        initXMLpane(saveFilename);
    }

    private void initProgPane(){
        progUserTAB.setLayout(new BorderLayout());

        progUserTAB.add(instructionsPan,BorderLayout.CENTER);
        instructionsPan.setLayout(new BoxLayout(instructionsPan,BoxLayout.PAGE_AXIS));
        instructionsPan.add(new InstructionXML());
        JButton nvxInstruction = new JButton("ajout instruction");
        nvxInstruction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InstructionXML instruc= new InstructionXML();
                instructionsPan.add(instruc);
                listeInstructions.add(instruc);
                instructionsPan.validate();
            }
        });
        progUserTAB.add(nvxInstruction,BorderLayout.PAGE_END);
    }

    private void initXMLpane(String saveFilename){
        JScrollPane scrollXML = new JScrollPane(xmlProgArea);
        xmlProgArea.setText(importText(saveFilename));
        //scrollXML.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        //scrollXML.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

        progXMLtextTAB.setLayout(new BorderLayout());
        initBoutonsXML(saveFilename);
        progXMLtextTAB.add(boutonsXMLPAN,BorderLayout.NORTH);
        progXMLtextTAB.add(scrollXML,BorderLayout.CENTER);
    }
    private void initBoutonsXML(String saveFilename){
        JButton[] boutonsXML= new JButton[3];

        boutonsXML[0] = new JButton("load XML");
        boutonsXML[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {importText(saveFilename);}
        });
        boutonsXML[1] = new JButton("update");
        boutonsXML[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {}
        });
        boutonsXML[2] = new JButton("save XML");
        boutonsXML[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {exportText(saveFilename,xmlProgArea.getText());}
        });

        for (JButton button : boutonsXML) boutonsXMLPAN.add(button);
    }

    /**
     * Sauve un text dans un fichier.
     * @param nomFichier Nom sous lequel le fichier est enregistré.
     * @param contenu Texte enregistré dans le fichier.
     * @implNote Le pathname doit être modifié pour exporter en .jar.
     */
    private void exportText(String nomFichier,String contenu){
        try{
            File fichier = new File(nomFichier);//"src/testRobot/"+
            FileOutputStream fout= new FileOutputStream(fichier); //génere un flux sortant
            PrintStream pout = new PrintStream(fout);

            System.out.println("*debut*\n"+contenu+"\n*fin*");
            pout.print(contenu);
            pout.close(); //sauve et ferme le fichier
        }
        catch(Exception e){e.printStackTrace();}
    }

    /**
     * Crée un String à partir d'un fichier.
     * @param nomFichier
     * @return Contenu du fichier.
     */
    private String importText(String nomFichier){
        String contenu;
        try{
            File fichier = new File(nomFichier);//"src/testRobot/"+
            Scanner scan = new Scanner(fichier);
            scan.useDelimiter("\0");
            contenu=scan.next();
            return contenu;
        }
        catch(Exception e){
            e.printStackTrace();
            return "*Error*";
        }
    }

    public void afficheFenetre(){
        JTabbedPane tabManager=new JTabbedPane();

        tabManager.add("prog", progUserTAB);
        tabManager.add("XML", progXMLtextTAB);

        add(tabManager);
        setSize(frameWidth/2,frameHeight/2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow AntHill=new MainWindow("monProgXml");
        Robot.areaPrompt();

        AntHill.afficheFenetre();
    }
}
