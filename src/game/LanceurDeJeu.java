/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 * Classe permettant de lancer un jeu.
 *
 * @author Yanis
 */
public class LanceurDeJeu {

    /**
     * Déclare un Jeu, instancie un JeuDevineLeMotOrdre puis éxecute le jeu.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException, TransformerException {
        // Declare un JeuAncien
        Jeu jeu;
        //Instancie un nouveau jeu
        jeu = new JeuDevineLeMotOrdre();
        //Execute le jeu
        jeu.execute();
    }

}
