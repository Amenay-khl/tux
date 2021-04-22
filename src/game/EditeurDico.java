
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * Permet d'éditer le dictionnaire utilisé par notre jeu (ajout de mots).
 *
 * @author Yanis
 */
public class EditeurDico {

    private Document doc;

    /**
     * Le constructeur ne nécessite aucun paramètre et réalise aucune action.
     */
    public EditeurDico() {
    }

    /**
     * Construit un arbre DOM du fichier passé en paramétre.
     *
     * @param fichier Nom du fichier (String).
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    public void lireDOM(String fichier) throws SAXException, ParserConfigurationException, IOException {
        // analyse du document
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder p = dbFactory.newDocumentBuilder();
        // récupération de la structure objet du document
        doc = p.parse(fichier);
    }

    /**
     * Transforme notre arbre DOM en fichier XML.
     *
     * @param fichier Nom du fichier en sortie.
     * @throws TransformerException
     */
    public void ecrireDOM(String fichier) throws TransformerException {
        // prepare DOM for output
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        // output DOM XML to console 
        StreamResult console = new StreamResult(System.out);
        transformer.transform(source, console);
        // output DOM XML to a file
        StreamResult file = new StreamResult(new File(fichier));
        transformer.transform(source, file);
    }

    /**
     * Permet d'ajouter un mot à notre dictionnaire. Le mot et le niveau passe
     * une suite de tests pour vérifier sa conformité au XMLSchema.
     *
     * @param mot Mot à ajouter (String)
     * @param niveau Niveau du mot (int)
     */
    public void ajouterMot(String mot, int niveau) {
        if (niveau < 1 || niveau > 5) {
            System.out.println("Impossible d'ajouter le mot : " + mot + " le niveau est incorrect.");
        } else {
            if (Pattern.matches("([a-zA-Z]){2,30}[-]([a-zA-Z]){2,30}|([a-zA-Z]{2,30})", mot)) {
                mot = mot.toLowerCase();
                NodeList list = doc.getElementsByTagName("ns1:mot");
                boolean copy = false;
                int i = 0;
                while (i < list.getLength() && !(list.item(i).getTextContent().equals(mot))) {
                    i++;
                }
                if (i < list.getLength()) {
                    copy = true;
                    System.out.println("Mot déjà présent dans le dico.");
                }
                if (!copy) {
                    Element newMot = doc.createElement("ns1:mot");
                    newMot.setAttribute("niveau", Integer.toString(niveau));
                    newMot.setTextContent(mot.toLowerCase());
                    doc.getChildNodes().item(1).appendChild(newMot);
                }
            } else {
                System.out.println("Pattern incorrect.");
            }
        }
    }
}
