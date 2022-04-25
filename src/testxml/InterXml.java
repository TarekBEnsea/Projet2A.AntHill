package testxml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class InterXml {

    private Document document = null;
    private String file = null;

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    /**
     * Appel du contructeur InterXml en créant un document Parse
     * @param s le chemin dans lequel le fichier est créé
     */
    public InterXml(String s) {
        this.file = s;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(s);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne la racine du fichier XML
     */
// Lire la racine du document XML
    public void ReadRootdoc(){
        String rootTag = document.getDocumentElement().getTagName();
        System.out.println("root tag: " + rootTag);
    }

    /**
     * Relis les sous noeuds d'une balise contenu dans un fichier XML
     * @param s le nom du noeud dont on veut lire les variables
     */
    public void ReadXmlNode(String s){
        Node comp = document.getElementsByTagName(s).item(0);
        NodeList listcomp = comp.getChildNodes();
        System.out.println("---" + s + "---");
        for(int i=0; i < (listcomp.getLength()-1)/2;i++){
            Node node = listcomp.item(2*i+1);
            System.out.println(node.getNodeName());
        }
    }

    /**
     *Crée un tableau qui contient tout les Ids du fichier XML
     * @param s
     * @return
     */
    public ArrayList<Integer> ReadIds(String s){

        NodeList fonction = document.getElementsByTagName(s);
        ArrayList<Integer> ids = new ArrayList<>();
        for(int i = 0; i < fonction.getLength(); i++){
            Node node = fonction.item(i);
            NamedNodeMap attr = node.getAttributes();
            Node id = attr.getNamedItem("id");
            String value = id.getTextContent();
            ids.add(Integer.valueOf(value));
            ids.add(i);
        }
        return ids;
    }

    /**
     * Permet de lire l'Id d'un comportement dont on connait le nom dans un fichier XML qui possède la racine Comportement
     * @param Id l'Id de la fonction
     * @param s le nom de la vairable de la fonction
     * @return
     */
    public String ReadCompStateId(int Id, String s){
        Node comp = document.getElementsByTagName("Comportement").item(0);
        NodeList listfonction = comp.getChildNodes();
        Node node = null;
        int index = 0;
        for(int i=0; i < (listfonction.getLength()-1)/2;i++){
            index = 2*i+1;
            node = listfonction.item(index);
            ArrayList<Integer> ids = ReadIds(node.getNodeName());
            //System.out.println("Nom " + node.getNodeName() + " Ids " + ids);
            for(int j = 0; j < ids.size(); j+=2){
                //System.out.println("Index : " + j);
                if(ids.get(j) == Id){
                    //System.out.println("Nom fonction: "+node.getNodeName());
                    Element node1 =(Element) document.getElementsByTagName(node.getNodeName()).item(ids.get(j+1));
                    String value = node1.getElementsByTagName(s).item(0).getTextContent();
                    return value;
                }
            }
        }
        return "-5";
    }

    /**
     * Cette fonction retourne une list de tout les noeuds contenu dans un fichier XML
     * @param s prend l'élément racine du fichier XML
     * @return la liste des noeuds
     */
    public ArrayList<String> ReturnXmlNode(String s){
        Node comp = document.getElementsByTagName(s).item(0);
        NodeList listcomp = comp.getChildNodes();
        ArrayList<String> list = new ArrayList<>();
        for(int i=0; i < (listcomp.getLength()-1)/2;i++){
            Node node = listcomp.item(2*i+1);
            list.add(node.getNodeName());
        }
        return list;
    }

    /**
     * Retourne la valeur contenu dans la variable d'un noeud
     * @param s le nom du noeud
     * @param Com le nom de la variable
     */
    public Integer ReadCompState(String s, String Com){
        Element node = (Element) document.getElementsByTagName(s).item(0);
        //System.out.println(s);
        String value = node.getElementsByTagName(Com).item(0).getTextContent();
        //System.out.println(Com +" "+ value);
        return Integer.valueOf(value);
    }
    // Modifier une variable d'état comprise dans un noeud

    /**
     * Modifie la valeur contenu dans une fonction dont on conait l'Id
     * @param Id correspondant à la fonction
     * @param Com la variable de la fonction à modifier
     * @param value la valeur à modifier
     */
    public void WriteCompVal(int Id, String Com, String value){
        Node comp = document.getElementsByTagName("Comportement").item(0);
        NodeList listfonction = comp.getChildNodes();
        Node node = null;
        int index = 0;
        for(int i=0; i < (listfonction.getLength()-1)/2;i++) {
            index = 2 * i + 1;
            node = listfonction.item(index);
            ArrayList<Integer> ids = ReadIds(node.getNodeName());
            //System.out.println("Nom " + node.getNodeName() + " Ids " + ids);
            for (int j = 0; j < ids.size(); j += 2) {
                //System.out.println("Index : " + j);
                if (ids.get(j) == Id) {
                    //System.out.println("Nom fonction: "+node.getNodeName());
                    Node node1 = document.getElementsByTagName(node.getNodeName()).item(ids.get(j + 1));
                    NodeList list = node1.getChildNodes();
                    for (int k = 0; k < list.getLength(); k++) {
                        Node node2 = list.item(k);
                        // Récupérer l'élément du comportement et modifier la valeur
                        if (Com.equals(node2.getNodeName())) {
                            node2.setTextContent(value);
                        }
                    }
                }
            }
        }


        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            DOMSource src = new DOMSource(document);
            StreamResult res = new StreamResult(new File(file));
            transformer.transform(src, res);
        }
        catch (Exception e) {
        e.printStackTrace();
        }
    }
}
