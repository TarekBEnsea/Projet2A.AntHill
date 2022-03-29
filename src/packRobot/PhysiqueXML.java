package packRobot;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhysiqueXML extends JPanel {
    private JComboBox fonctionJ;
    private JLabel[] FonctionParams;

    public PhysiqueXML(){
        fonctionJ = new JComboBox(new String[]{"Taille"});
        initFonction();
    }

    private void initFonction() {
        fonctionJ.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FonctionParams = new JLabel[]{new JLabel("Longueur:"), new JLabel("Largeur:")};
                FonctionParams[0].setName("Longueur");
                FonctionParams[1].setName("Largeur");
            }
        });
    }
}

    