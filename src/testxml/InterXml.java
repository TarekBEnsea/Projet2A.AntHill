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
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

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
/*
    public void readXml(){
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

    public void writeXml(String priocomp1, String priocomp2, String priocomp3, String priocomp4){
        Node Comp1 = document.getElementsByTagName("AvanceXY").item(0);
        NodeList list1 = Comp1.getChildNodes();
        Node Comp2 = document.getElementsByTagName("Recule").item(0);
        NodeList list2 = Comp2.getChildNodes();
        Node Comp3 = document.getElementsByTagName("turn").item(0);
        NodeList list3 = Comp3.getChildNodes();
        Node Comp4 = document.getElementsByTagName("Stop").item(0);
        NodeList list4 = Comp4.getChildNodes();
        try{

            for (int i = 0; i < list1.getLength(); i++) {
                Node node = list1.item(i);
                // Récupérer l'élément priority et modifier la valeur
                if ("priority".equals(node.getNodeName())) {
                    node.setTextContent(priocomp1);
                }
            }

            for (int i = 0; i < list2.getLength(); i++) {
                Node node = list2.item(i);
                // Récupérer l'élément priority et modifier la valeur
                if ("priority".equals(node.getNodeName())) {
                    node.setTextContent(priocomp2);
                }
            }

            for (int i = 0; i < list3.getLength(); i++) {
                Node node = list3.item(i);
                // Récupérer l'élément priority et modifier la valeur
                if ("priority".equals(node.getNodeName())) {
                    node.setTextContent(priocomp3);
                }
            }

            for (int i = 0; i < list4.getLength(); i++) {
                Node node = list4.item(i);
                // Récupérer l'élément priority et modifier la valeur
                if ("priority".equals(node.getNodeName())) {
                    node.setTextContent(priocomp4);
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
*/

    public Integer ReadCompState(String s, String Com){
        Element node = (Element) document.getElementsByTagName(s).item(0);
        System.out.println(s);
        String value = node.getElementsByTagName(Com).item(0).getTextContent();
        System.out.println(Com +" "+ value);
        return Integer.valueOf(value);
    }

    public void WriteCompVal(String s, String Com, String value){
        Node Comp = document.getElementsByTagName(s).item(0);
        NodeList list = Comp.getChildNodes();

        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            // Récupérer l'élément priority et modifier la valeur
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
