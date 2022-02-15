package testxml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ModifXml {

    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public void CreatXml(){
        try {
           DocumentBuilder builder = factory.newDocumentBuilder();
            Document xml = builder.newDocument(); //Création du fichier
            Element root = xml.createElement("Comportement"); //Création de notre élément racine

            //LES 5 LIGNES SUIVANTES SONT POUR UNE SEULE LIGNE DU FICHIER XML
            Element param0 = xml.createElement("param");
            root.appendChild(param0);
            param0.setAttribute("id", "0");
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
    }

