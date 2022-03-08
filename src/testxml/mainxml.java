package testxml;


import javax.xml.transform.TransformerException;

public class mainxml {
    public static void main(String[] args) throws TransformerException {
        InterXml comportementsimple = new InterXml("src/testxml/ComportementSimple");
        comportementsimple.ReadXmlNode("Comportement");
        //System.out.println(comportementsimple.ReadId("MouvXY"));
        System.out.println(comportementsimple.ReadCompStateId(2,"x"));
        CreatXml ComportementTest = new CreatXml();
        ComportementTest.addGoToXY("1","100","200","1", "0");
        ComportementTest.addGoToXY("2","100","200","1", "0");
        ComportementTest.finishXML("src/testxml/ComportementTest.xml");
    }
}

