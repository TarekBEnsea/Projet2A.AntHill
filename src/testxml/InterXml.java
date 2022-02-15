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

    public InterXml(String s) {
        this.file = s;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            this.document = builder.parse(s);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }
    // Lire la racine du document XML
    public void ReadRootdoc(){
        String rootTag = document.getDocumentElement().getTagName();
        System.out.println("root tag: " + rootTag);
    }
    // Lire les variables contenu dans un noeud
    public void ReadXmlNode(String s){
        Node comp = document.getElementsByTagName(s).item(0);
        NodeList listcomp = comp.getChildNodes();
        System.out.println("---" + s + "---");
        for(int i=0; i < (listcomp.getLength()-1)/2;i++){
            Node node = listcomp.item(2*i+1);
            System.out.println(node.getNodeName());
        }
    }

    public Integer ReadId(String s){

        Node fonction = document.getElementsByTagName(s).item(0);
        NamedNodeMap attr = fonction.getAttributes();
        Node node = attr.getNamedItem("id");
        String value = node.getTextContent();
        return Integer.valueOf(value);
    }
    public int ReadCompStateId(int Id, String s){
        Node comp = document.getElementsByTagName("Comportement").item(0);
        NodeList listfonction = comp.getChildNodes();
        Node node = null;
        int index = 0;
        for(int i=0; i < (listfonction.getLength()-1)/2;i++){
            index = 2*i+1;
            node = listfonction.item(index);
            if(ReadId(node.getNodeName()) == Id ){break;}
        }
         NodeList list = comp.getChildNodes();
        Element n1 = (Element) list.item(index);
        String value = n1.getElementsByTagName(s).item(0).getTextContent();
        return Integer.valueOf(value);
    }
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

    //Lire la valeur d'une variable dans un noeud
    public Integer ReadCompState(String s, String Com){
        Element node = (Element) document.getElementsByTagName(s).item(0);
        //System.out.println(s);
        String value = node.getElementsByTagName(Com).item(0).getTextContent();
        //System.out.println(Com +" "+ value);
        return Integer.valueOf(value);
    }
    // Modifier une variable d'état comprise dans un noeud
    public void WriteCompVal(String s, String Com, String value){
        Node Comp = document.getElementsByTagName(s).item(0);
        NodeList list = Comp.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            // Récupérer l'élément du comportement et modifier la valeur
            if (Com.equals(node.getNodeName())) {
                node.setTextContent(value);
            }
        }
        try {

        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        DOMSource src = new DOMSource(document);
        StreamResult res = new StreamResult(new File(file));
        transformer.transform(src, res);
    } catch (Exception e) {
        e.printStackTrace();
    }

    }

}
