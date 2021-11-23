package testxml;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class main {
    public static void main(String[] args) {

        InterXml comportementsimple = new InterXml("src/testxml/ComportementSimple");

        comportementsimple.ReadRootdoc();
        comportementsimple.ReadXmlNode("Comportement");
        comportementsimple.ReadXmlNode("AvanceXY");
        comportementsimple.ReadCompState("AvanceXY","time");

    }

}

