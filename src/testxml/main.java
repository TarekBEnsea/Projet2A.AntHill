package testxml;

public class main {
    public static void main(String[] args) {

        InterXml comportementsimple = new InterXml("src/testxml/ComportementSimple");

        comportementsimple.ReadRootdoc();
        comportementsimple.ReadXmlNode("Comportement");
        comportementsimple.ReadXmlNode("AvanceXY");
        comportementsimple.ReadCompState("AvanceXY","time");
        comportementsimple.WriteCompVal("AvanceXY","time","10000");
        comportementsimple.ReadCompState("AvanceXY","time");

    }

}

