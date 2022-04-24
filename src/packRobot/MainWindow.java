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

/**
 * Classe principale pour l'interface graphique du projet.
 */
public class MainWindow extends JFrame{
    /** Onglet pour le renseignement des différentes instructions.
     * Permet d'ajouter, supprimer des instructions et de lancer une simulation.
     * @see InstructionXML*/
    private JPanel progUserTAB = new JPanel();
    /** Panneau contenant les instructions*/
        private JPanel instructionsPan = new JPanel();
        /** Liste des instructions (éléments de comportement)*/
        private LinkedList<InstructionXML> listeInstructions = new LinkedList<InstructionXML>();
    /** Onglet contenant le fichier XML généré.
     * Peut être mis-à-jour en renseignant les instruction dans l'onglet progUserTAB
     * Peut être complété à la main avant de lancer la simulation.*/
    private JPanel progXMLtextTAB = new JPanel();
        /**Panel contenant les boutons de gestion du fichier XML */
        private JPanel boutonsXMLPAN = new JPanel();
            //private JButton[] boutonsXML;
        /**Zone de visualisation du fichier XML*/
        private JTextArea xmlProgArea= new JTextArea();
    /** Onglet pour renseigner les propriétés physiques des Robots et les conditions de simulation*/
    private JPanel PhysicUserTAB;
    /** Onglet contenant la simulation d'essaims de robots*/
    private Panneau simulation1TAB ;//= new JPanel();
    //private Panneau simulation2TAB ;//= new JPanel();
    /** Thread sur lequel tourne la simulation.
     * Comprend le rafraichissement des positions et de l'affichage*/
    private Thread simu1 = new Thread();
    //private Thread simu2 ;//= new Thread();
    /** à true si une simulation est active, à false sinon.*/
    private boolean runningSimulation=false;

    /** Largeur initiale de la fenêtre de l'interface*/
    private int frameWidth;
    /** Hauteur initiale de la fenêtre de l'interface*/
    private int frameHeight;
    /** Gestionnaire d'onglets de l'interface*/
    private JTabbedPane tabManager =new JTabbedPane();


    /**
     * Crée une nouvelle fenêtre avec 4 onglets pour régler et visualiser une simulation d'essaims de robots.
     * @param saveFilename Chemin et nom du fichier XML sous lequel seront enregistrés les paramètres de simulation.
     */
    public MainWindow(String saveFilename){ //"monProgXml"
        super("AntHill Vbeta1.0");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameWidth=(int) screenSize.getWidth()*3/4;
        frameHeight=(int) screenSize.getHeight()*3/4;
        Robot.initArea(this.frameWidth-22,this.frameHeight-68);
        Robot.initArea((int)(1.5*this.frameWidth),(int)(1.5*this.frameHeight));

        initProgPane();
        initXMLpane(saveFilename);
        PhysicUserTAB= new PhysiqueXML();
    }

    /**
     * Initialise les éléments de l'onglet progUserTAB.
     */
    private void initProgPane(){
        progUserTAB.setLayout(new BorderLayout());

        JScrollPane scrollProg = new JScrollPane(instructionsPan);
        //scrollProg.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        progUserTAB.add(scrollProg,BorderLayout.CENTER);
        instructionsPan.setLayout(new BoxLayout(instructionsPan,BoxLayout.PAGE_AXIS));
        InstructionXML instruc =new InstructionXML();
        suppButtonSetActioner(instruc);
        instruc.setAlignmentX(Component.RIGHT_ALIGNMENT);
        instructionsPan.add(instruc);
        listeInstructions.add(instruc);

        JButton nvxInstruction = new JButton("ajout instruction");
        nvxInstruction.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //for(String s:listeInstructions.getLast().generateSynthTab()) System.out.print(s+", "); System.out.println("}");
                InstructionXML instruc= new InstructionXML();
                suppButtonSetActioner(instruc);
                instruc.setAlignmentX(Component.RIGHT_ALIGNMENT);
                instructionsPan.add(instruc);
                listeInstructions.add(instruc);
                scrollProg.validate();
            }
        });
        JButton resetSimu = new JButton("Reset Simulation");
        resetSimu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                simu1.interrupt();
                simulation1TAB = new Panneau();
                simulation1TAB.setSimulationType(SimulationType.XMLCONTROLED);
                try{tabManager.remove(2);} catch(IndexOutOfBoundsException e1) {}// remove(2) si on n'affiche pas PhysicUserTAB, remove(3) sinon
                tabManager.add("Sim1",simulation1TAB);
                runningSimulation=false;
                simulation1TAB.activate_placement();
            }
        });
        JButton lanceSimu = new JButton("Simulation");
        lanceSimu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!runningSimulation){
                    simu1 = new Thread(simulation1TAB);
                    simu1.start();
                }
                else System.out.println("nope");
                runningSimulation=true;
                simulation1TAB.desactivate_placement();
            }
        });
        JPanel boutonsCommandes = new JPanel();
        progUserTAB.add(boutonsCommandes,BorderLayout.PAGE_END);
        boutonsCommandes.setLayout(new GridLayout(1,3));
        boutonsCommandes.add(nvxInstruction);
        boutonsCommandes.add(resetSimu);
        boutonsCommandes.add(lanceSimu);
    }

    /**
     * Génère la liste des instruction sous un format adapté pour générer le fichier XML correpondant.
     * @return tableau des instructions. Chaque élément contient un tableau de String correspondant aux nom et valeurs des champs du fichier XML.
     */
    public String[][] listerInstructions(){
        int i = 0;
        String[][] liste = new String[InstructionXML.getNombreInstructions()][];
        for(InstructionXML instr : listeInstructions){
            liste[i]=instr.generateSynthTab();
            i++;
        }
        return liste;
    }

    /**
     * Initialise les éléments de l'onglet progUserTAB.
     * @param saveFilename Chemin et nom du fichier XML sous lequel seront enregistrés les paramètres de simulation.
     */
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

    /**
     * Initialise les boutons de l'onglet progXMLtextTAB pour la gestion de fichier.
     * Permet de charger le fichier depuis le disque de l'ordinateur ou l'onglet progUserTAB, et sauver sur le disque.
     * @param saveFilename Chemin et nom du fichier XML sous lequel seront enregistrés les paramètres de simulation.
     */
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
     * Initialise le bouton de suppression d'instruction pour l'objet InstructionXML ciblée.
     * @param instruc Instruction à supprimer.
     */
    private void suppButtonSetActioner(InstructionXML instruc){
        instruc.getSuppInstructionButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int id=instruc.delAndUpdate();
                for(InstructionXML instrXML:listeInstructions){
                    try {instrXML.decrID();
                    } catch (Exception ex) {ex.printStackTrace();}
                }
                instructionsPan.remove(id);
                listeInstructions.remove(id);
                validate();
            }
        });
    }
    /**
     * Crée un fichier XML de comportement à partir des instructions données et le sauvegarde dans le dossier du projet.
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
     * Crée un fichier XML de caractéristiques physiques de Robots et
     * de conditions de simulation à partir des champs remplis dans l'onglet associé et le sauvegarde dans le dossier du projet.
     * @param listeParametres Informations pour remplir les champs. Chaque valeur est précédée par son nom.
     * @deprecated son utilisation est instable
     */
    private void updateXMLphys(String[][] listeParametres){
        CreatXml RobotPhysique = new CreatXml();
        for(String[] element : listeParametres){
            Element fonction = null;
            for(int i=0; i < element.length; i+=2) {
                System.out.println(element[i]);
                if(element[i].equals("name")){
                    fonction = RobotPhysique.newFonction(element[i+1]);
                }
                RobotPhysique.newElement(element[i], element[i+1], fonction);
            }
        }
        try {
            RobotPhysique.finishXML("src/RobotPhysqiue.xml");
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

            //System.out.println("*debut*\n"+contenu+"\n*fin*");
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

    /**
     * Réunit les différents onglets et affiche la fenêtre d'interface graphique.
     * @see JTabbedPane
     */
    public void afficheFenetre(){

        tabManager.add("Program", progUserTAB);
        tabManager.add("XML", progXMLtextTAB);
        //tabManager.add("Paramètres",new JScrollPane(PhysicUserTAB));
        //tabManager.add("Sim1",simulation1TAB);
        ((JButton)((JPanel) progUserTAB.getComponent(1)).getComponent(1)).doClick(); //JButton resetSimu -> ajoute le panneau de simulation

        add(tabManager);
        setSize(frameWidth,frameHeight);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Fonction principale du projet.
     * @param args nothing here...
     */
    public static void main(String[] args) {
        MainWindow AntHill=new MainWindow("src/testxml/ComportementTest.xml");
        Robot.areaPrompt();

        AntHill.afficheFenetre();
    }

}
