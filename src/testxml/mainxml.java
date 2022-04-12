package testxml;


import javax.xml.transform.TransformerException;

public class mainxml {
    public static void main(String[] args) throws TransformerException {
        InterXml comportementsimple = new InterXml("src/testxml/ComportementSimple");
        //comportementsimple.ReadXmlNode("Comportement");
        //System.out.println(comportementsimple.ReadId("MouvXY"));
        System.out.println(comportementsimple.ReadCompStateId(2,"x"));
        comportementsimple.WriteCompVal(2,"x","31");
        System.out.println(comportementsimple.ReadCompStateId(2,"x"));
        /*
        CreatXml ComportementTest = new CreatXml();
        String[][] liste = {{"name", "MouvXY", "x", "20", "y", "50", "priority", "5", "id", "0"}, {"name", "MouvXY", "x", "20", "y", "100", "priority", "3", "id", "2"}};
        for(String[] element : liste){
            Element fonction = null;
            for(int i=0; i < element.length; i+=2) {
                System.out.println(element[i]);
                if(element[i].equals("name")){
                    fonction = ComportementTest.newFonction(element[i+1]);
                }
                ComportementTest.newElement(element[i], element[i+1], fonction);
            }
        }
        ComportementTest.finishXML("src/testxml/ComportementTest.xml");*/
    }
}
