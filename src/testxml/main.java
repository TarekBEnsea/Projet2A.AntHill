package testxml;



public class main{
    public static void main(String[] args) {
        InterXml comportementsimple = new InterXml("src/testxml/ComportementSimple");
        //comportementsimple.ReadXmlNode("Comportement");
        System.out.println(comportementsimple.ReadId("MouvXY"));
        System.out.println(comportementsimple.ReadCompStateId(2,"x"));

    }
}

