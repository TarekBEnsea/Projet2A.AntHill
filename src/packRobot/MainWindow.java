package packRobot;

import org.w3c.dom.Element;
import testxml.CreatXml;
import javax.xml.transform.TransformerException;
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
    private Panneau simulation1TAB ;//= new JPanel();
    private JPanel simulation2TAB ;//= new JPanel();
    private Thread simu1 = new Thread();
    private Thread simu2 ;//= new Thread();

    private int frameWidth;
    private int frameHeight;
    private JTabbedPane tabManager =new JTabbedPane();



    public MainWindow(String saveFilename){ //"monProgXml"
        super("AntHill Custom Simulation Project Valpha 0.1");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameWidth=(int) screenSize.getWidth();
        frameHeight=(int) screenSize.getHeight();
        Robot.initArea(this.frameWidth/2,this.frameHeight/2);

        initProgPane();
        initXMLpane(saveFilename);
    }

    private void initProgPane(){
        progUserTAB.setLayout(new BorderLayout());

        JScrollPane scrollProg = new JScrollPane(instructionsPan);
        scrollProg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        progUserTAB.add(scrollProg,BorderLayout.CENTER);
        instructionsPan.setLayout(new BoxLayout(instructionsPan,BoxLayout.PAGE_AXIS));
        InstructionXML instruc =new InstructionXML();
        instructionsPan.add(instruc);
        listeInstructions.add(instruc);

        JButton nvxInstruction = new JButton("ajout instruction");
        nvxInstruction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for(String s:listeInstructions.getLast().generateSynthTab()) System.out.print(s+", "); System.out.println("}");
                InstructionXML instruc= new InstructionXML();
                instructionsPan.add(instruc);
                listeInstructions.add(instruc);
                instructionsPan.validate();
            }
        });
        JButton lanceSimu = new JButton("Simulation");
        lanceSimu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simu1.interrupt();
                simulation1TAB = new Panneau();
                simulation1TAB.setSimulationType(SimulationType.DEFAULT);
                simu1 = new Thread(simulation1TAB);
                simu1.start();
                try{tabManager.remove(2);} catch(IndexOutOfBoundsException e1) {}
                tabManager.add("Sim1",simulation1TAB);
            }
        });
        JPanel boutonsCommandes = new JPanel();
        progUserTAB.add(boutonsCommandes,BorderLayout.PAGE_END);
        boutonsCommandes.setLayout(new GridLayout(1,3));
        boutonsCommandes.add(nvxInstruction);
        boutonsCommandes.add(lanceSimu);
    }

    public String[][] listerInstructions(){
        int i = 0;
        String[][] liste = new String[InstructionXML.getNombreInstructions()][];
        for(InstructionXML instr : listeInstructions){
            liste[i]=instr.generateSynthTab();
            i++;
        }
        return liste;
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
            public void actionPerformed(ActionEvent e) {xmlProgArea.setText(importText(saveFilename));}
        });
        boutonsXML[1] = new JButton("update");
        boutonsXML[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateText(listerInstructions(),saveFilename);
                xmlProgArea.setText(importText(saveFilename));
            }
        });
        boutonsXML[2] = new JButton("save XML");
        boutonsXML[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {exportText(saveFilename,xmlProgArea.getText());}
        });

        for (JButton button : boutonsXML) boutonsXMLPAN.add(button);
    }

    /**
     * Crée un fichier XML de comportement à partir des instructions données.
     * @param listerInstructions Informations pour remplir les champs. Chaque valeur est précédée par son nom.
     * @param saveFilename Nom du fichier XML de destination.
     */
    private void updateText(String[][] listerInstructions,String saveFilename){
        CreatXml ComportementTest = new CreatXml();
        for(String[] element : listerInstructions){
            Element fonction = null;
            for(int i=0; i < element.length; i+=2) {
                System.out.println(element[i]);
                if(element[i].equals("name")){
                    fonction = ComportementTest.newFonction(element[i+1]);
                }
                ComportementTest.newElement(element[i], element[i+1], fonction);
            }
        }
        try {
            ComportementTest.finishXML(saveFilename);
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
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

        tabManager.add("prog", progUserTAB);
        tabManager.add("XML", progXMLtextTAB);
        //tabManager.add("Sim1",simulation1TAB);

        add(tabManager);
        setSize(frameWidth/2,frameHeight/2);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow AntHill=new MainWindow("src/testxml/ComportementTest.xml");
        Robot.areaPrompt();

        AntHill.afficheFenetre();
    }
}
