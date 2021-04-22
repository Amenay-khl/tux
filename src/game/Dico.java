/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Dictionnaire comportant les mots chargés depuis un XML. Les mots sont rangés
 * dans des listes par niveau.
 *
 * @author Yanis
 */
public class Dico extends DefaultHandler {

    /**
     * Enumération des niveaux possibles : 1, 2, 3, 4 et 5 pour le parsage SAX.
     */
    public enum Niveau {

        /**
         * Mot de niveau 1
         */
        UN,
        /**
         * Mot de niveau 1
         */
        DEUX,
        /**
         * Mot de niveau 3
         */
        TROIS,
        /**
         * Mot de niveau 4
         */
        QUATRE,
        /**
         * Mot de niveau 5
         */
        CINQ,
        /**
         * L'élément n'est pas un mot
         */
        STOP;
    }
    private Niveau level;

    private ArrayList<String> listeNiveau1;
    private ArrayList<String> listeNiveau2;
    private ArrayList<String> listeNiveau3;
    private ArrayList<String> listeNiveau4;
    private ArrayList<String> listeNiveau5;
    private StringBuffer buffer;
    private boolean inMot = false;

    private String cheminFichierDico;

    /**
     * Constructeur du dictionnaire.
     *
     * @param cheminFichierDico Chemin du XML qui stock les mots.
     */
    public Dico(String cheminFichierDico) {
        super();
        this.cheminFichierDico = cheminFichierDico;
        listeNiveau1 = new ArrayList<String>();
        listeNiveau2 = new ArrayList<String>();
        listeNiveau3 = new ArrayList<String>();
        listeNiveau4 = new ArrayList<String>();
        listeNiveau5 = new ArrayList<String>();
    }

    /**
     * Retourne le chemin du fichier XML dictionnaire utilisé.
     *
     * @return Chemin du dictionnaire XML
     */
    public String getCheminFichierDico() {
        return cheminFichierDico;
    }

    private int vérifieNiveau(int niveau) {
        int res = niveau;
        if (niveau < 1 || niveau > 5) {
            res = 0;
            //System.out.println("Niveau incorrect !");
        }
        return res;
    }

    private String getMotDepuisListe(ArrayList<String> list) {
        String mot;
        if (list.size() > 0) {
            int nombreAleatoire = 0 + (int) (Math.random() * ((list.size() - 0)));
            mot = list.get(nombreAleatoire);
        } else {
            mot = "vide";
        }
        return mot;
    }

    /**
     * Retourne un mot en fonction du niveau passé en paramétre (aléatoire).
     *
     * @param niveau int représentant le niveau du mot souhaité
     * @return Un mot aléatoire du niveau passé en paramétre, pour les niveaux
     * incorrect on retourne "error"
     */
    public String getMotDepuisListeNiveau(int niveau) {
        String str;
        switch (vérifieNiveau(niveau)) {
            case 1:
                str = getMotDepuisListe(listeNiveau1);
                break;
            case 2:
                str = getMotDepuisListe(listeNiveau2);
                break;
            case 3:
                str = getMotDepuisListe(listeNiveau3);
                break;
            case 4:
                str = getMotDepuisListe(listeNiveau4);
                break;
            case 5:
                str = getMotDepuisListe(listeNiveau5);
                break;
            default:
                str = "error";
                System.out.println("Niveau inconnu ! Mot par défaut : error.\n");
                break;
        }
        return str;
    }

    /**
     * Permet d'ajouter un mot dans une liste (seulement si le niveau est
     * correct).
     *
     * @param niveau Niveau du mot (int)
     * @param mot Le mot à ajouter (String)
     */
    public void ajouteMotADico(int niveau, String mot) {
        switch (vérifieNiveau(niveau)) {
            case 1:
                listeNiveau1.add(mot);
                break;
            case 2:
                listeNiveau2.add(mot);
                break;
            case 3:
                listeNiveau3.add(mot);
                break;
            case 4:
                listeNiveau4.add(mot);
                break;
            case 5:
                listeNiveau5.add(mot);
                break;
            default:
                System.out.println("NIVEAU INCORRECT - AJOUT DU MOT ANNULE");
                break;
        }
    }

    private void vide() {
        listeNiveau1.clear();
        listeNiveau2.clear();
        listeNiveau3.clear();
        listeNiveau4.clear();
        listeNiveau5.clear();
    }

    /**
     * Permet de lire un fichier XML avec un parseur DOM et d'initialiser notre
     * dictionnaire.
     *
     * @param path Le chemin du fichier XML (String)
     * @param filename Le nom du fichier XML (String)
     * @throws SAXException
     * @throws IOException
     */
    public void lireDictionnaireDOM(String path, String filename) throws SAXException, IOException {
        // crée un parser de type DOM
        DOMParser parser = new DOMParser();
        // parse le document XML correspondant au fichier filename dans le chemin path
        parser.parse(path + filename);
        // récupère l'instance de document
        Document doc = parser.getDocument();
        // récupère la liste des éléments nommés ns1:mot
        NodeList motList = doc.getElementsByTagName("ns1:mot");
        // vide les listes de mots
        vide();
        for (int i = 0; i < motList.getLength(); i++) {
            int niveau = Integer.parseInt(((Element) motList.item(i)).getAttribute("niveau"));
            String mot = ((Element) motList.item(i)).getTextContent();
            ajouteMotADico(niveau, mot);
        }
    }

    /**
     * Permet de lire un fichier XML avec un parseur SAX et d'initialiser notre
     * dictionnaire. Le fichier XML est le fichier passé en paramétre du
     * constructeur.
     *
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    public void lireDictionnaire() throws SAXException, ParserConfigurationException, IOException {
        DefaultHandler gestionnaire = this;
        SAXParserFactory fabrique = SAXParserFactory.newInstance();
        SAXParser parseur = fabrique.newSAXParser();

        File fichier = new File(cheminFichierDico);

        parseur.parse(fichier, gestionnaire);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        buffer = new StringBuffer();
        if (qName.equals("ns1:mot")) {
            switch (Integer.parseInt(attributes.getValue(0))) {
                case 1:
                    level = Niveau.UN;
                    break;
                case 2:
                    level = Niveau.DEUX;
                    break;
                case 3:
                    level = Niveau.TROIS;
                    break;
                case 4:
                    level = Niveau.QUATRE;
                    break;
                case 5:
                    level = Niveau.CINQ;
                    break;
                default:
                    level = Niveau.STOP;
                    break;
            }
        } else if (qName.equals("ns1:dictionnaire")) {
            level = Niveau.STOP;
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        int niveau;
        if (level.equals(Niveau.STOP)) {
        } else {
            switch (level) {
                case UN:
                    niveau = 1;
                    break;
                case DEUX:
                    niveau = 2;
                    break;
                case TROIS:
                    niveau = 3;
                    break;
                case QUATRE:
                    niveau = 4;
                    break;
                case CINQ:
                    niveau = 5;
                    break;
                default:
                    niveau = 1;
                    break;
            }
            ajouteMotADico(niveau, buffer.toString());
            level = Niveau.STOP;
        }

        buffer = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String lecture = new String(ch, start, length);
        if (buffer != null) {
            buffer.append(lecture);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        System.out.println("-----Début parseur SAX-----");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("Il y a " + listeNiveau1.size() + " mots de niveau 1.");
        System.out.println("Il y a " + listeNiveau2.size() + " mots de niveau 2.");
        System.out.println("Il y a " + listeNiveau3.size() + " mots de niveau 3.");
        System.out.println("Il y a " + listeNiveau4.size() + " mots de niveau 4.");
        System.out.println("Il y a " + listeNiveau5.size() + " mots de niveau 5.");
        System.out.println("------Fin parseur SAX------");
    }

}
