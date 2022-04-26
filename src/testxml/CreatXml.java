package testxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Classe qui permet la création et l'initialisation d'un fichier XML
 */
public class CreatXml {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    public Document xml;
    public Element root;

    /**
     * Constructeur de l'objet CreatXML crée un document XML à l'aide de la méthode DocBuilder
     */
    public CreatXml(){
        try {
           DocumentBuilder builder = factory.newDocumentBuilder();
            xml = builder.newDocument(); //Création du fichier
            root = xml.createElement("Comportement"); //Création de notre élément racine
            xml.appendChild(root);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Création d'une fonction dans le fichier XML dont on va attribuer un nom
     * @param name
     * @return
     */
    public Element newFonction(String name){
        Element fonction = xml.createElement(name);
        root.appendChild(fonction);
        return fonction;
    }

    /**
     * Ajout des éléments dans la fonction qui a été créer
     * @param name Le nom de la variable
     * @param value Sa valeur
     * @param fonction La fonction dans laquelle il se trouve
     */
    public void newElement(String name, String value, Element fonction){
        if(name.equals("id")){
            fonction.setAttribute("id", value);
        }
        else {
            Element node = xml.createElement(name);
            fonction.appendChild(node);
            node.appendChild(xml.createTextNode(value));
        }
    }
/*
    public void addGoToXY(String id, String sx, String sy, String sprio, String soutof) {
        Element fonction = xml.createElement("GotoXY");
        root.appendChild(fonction);
        fonction.setAttribute("id", id);

        Element name = xml.createElement("name");
        fonction.appendChild(name);
        name.appendChild(xml.createTextNode("GoToXY"));

        Element x = xml.createElement("x");
        fonction.appendChild(x);
        Element y = xml.createElement("y");
        Element priority = xml.createElement("priority");
        Element outofbound = xml.createElement("outofbound");
        fonction.appendChild(y);
        fonction.appendChild(priority);
        fonction.appendChild(outofbound);


        x.appendChild(xml.createTextNode(sx));//position en x de l'écran
        y.appendChild(xml.createTextNode(sy));//position en y de l'écran
        priority.appendChild(xml.createTextNode(sprio));//numéro de priorité
        outofbound.appendChild(xml.createTextNode(soutof));//0 ou 1
    }*/

    /**
     * Validation du fichier XML suivit par sa création
     * @param s
     * @throws TransformerException
     */
    public void finishXML(String s) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(xml);
        StreamResult resultat = new StreamResult(new File(s));
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(source, resultat);
    }
}

