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

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("src/testxml/ComportementSimple");
            readCompsimp(document);
            writeCompsimp(document,"src/testxml/ComportementSimple");
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private static void readCompsimp(Document document) {
        //        get Document root tag name
        String rootTag = document.getDocumentElement().getTagName();
        System.out.println("root tag: " + rootTag);

        Element node0 = (Element) document.getElementsByTagName("AvanceXY").item(0);
        System.out.println("---AvanceXY---");
//            get Tag value
        String time = node0.getElementsByTagName("time").item(0).getTextContent();
        String priority = node0.getElementsByTagName("priority").item(0).getTextContent();
            System.out.println("time: " + time);
            System.out.println("priority: " + priority);

        Element node1 = (Element) document.getElementsByTagName("Recule").item(0);
        System.out.println("---Recule---");
//            get Tag value
        String time1 = node1.getElementsByTagName("time").item(0).getTextContent();
        String priority1 = node1.getElementsByTagName("priority").item(0).getTextContent();
        System.out.println("time: " + time1);
        System.out.println("priority: " + priority1);

        Element node2 = (Element) document.getElementsByTagName("turn").item(0);
        System.out.println("---Turn---");
//            get Tag value
        String time2 = node2.getElementsByTagName("time").item(0).getTextContent();
        String priority2 = node2.getElementsByTagName("priority").item(0).getTextContent();
        String angle = node2.getElementsByTagName("angle").item(0).getTextContent();
        System.out.println("time: " + time2);
        System.out.println("angle: " + angle);
        System.out.println("priority: " + priority2);

        Element node3 = (Element) document.getElementsByTagName("Stop").item(0);
        System.out.println("---Stop---");
//            get Tag value
        String time3 = node3.getElementsByTagName("time").item(0).getTextContent();
        String priority3 = node3.getElementsByTagName("priority").item(0).getTextContent();
        System.out.println("time: " + time3);
        System.out.println("priority: " + priority3);
    }

    private static void writeCompsimp(Document document, String file) {
        Node Comp1 = document.getElementsByTagName("AvanceXY").item(0);
        NodeList list1 = Comp1.getChildNodes();
        Node Comp2 = document.getElementsByTagName("Recule").item(0);
        NodeList list2 = Comp2.getChildNodes();
        try{

        for (int i = 0; i < list1.getLength(); i++) {
            Node node = list1.item(i);
            // Récupérer l'élément priority et modifier la valeur
            if ("priority".equals(node.getNodeName())) {
                node.setTextContent("3");
            }
        }

            for (int i = 0; i < list2.getLength(); i++) {
                Node node = list2.item(i);
                // Récupérer l'élément priority et modifier la valeur
                if ("priority".equals(node.getNodeName())) {
                    node.setTextContent("2");
                }
            }

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

