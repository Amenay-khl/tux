/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import xml.XMLUtil;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Permet le stockage d'un profil (parties,dateNaissance,nom,avatar).
 *
 * @author Yanis
 */
public class Profil {

    private String nom;
    //avatar et dateNaissance sont le même pour tout les profils.
    private String avatar;
    private String dateNaissance;
    private ArrayList<Partie> parties;

    /**
     * Document qui stock l'arbre DOM du profil.
     */
    public Document _doc;
    private int oldNombreParties = 0; //nombre de partie déjà présente (changement uniquement lors d'un chargement de profil)

    /**
     * Constructeur par défault du profil (initialisation de la listes des
     * parties réalisées).
     */
    public Profil() {
        parties = new ArrayList<>();
    }

    /**
     * Second constructeur qui crée un DOM à partir d'un fichier XML (chargement
     * d'un profil).
     *
     * @param nomFichier Le nom du fichier XML
     */
    public Profil(String nomFichier) {
        _doc = fromXML(nomFichier);

        this.nom = _doc.getChildNodes().item(0).getChildNodes().item(1).getTextContent();
        this.avatar = _doc.getChildNodes().item(0).getChildNodes().item(3).getTextContent();
        this.dateNaissance = xmlDateToProfileDate(_doc.getChildNodes().item(0).getChildNodes().item(5).getTextContent());
        parties = new ArrayList<Partie>();

        NodeList partiesXML = _doc.getElementsByTagName("pr:partie");
        for (int i = 0; i < partiesXML.getLength(); i++) {
            if (partiesXML.item(i).getChildNodes().item(1).getNodeName().equals("pr:temps")) {
                ajouterPartie(new Partie((Element) partiesXML.item(i)));
            } else {
                ajouterPartie(new Partie(Profil.xmlDateToProfileDate(((Element) partiesXML.item(i)).getAttribute("date")), partiesXML.item(i).getChildNodes().item(1).getTextContent(), Integer.parseInt(((Element) partiesXML.item(i).getChildNodes().item(1)).getAttribute("niveau"))));
            }
        }
    }

    /**
     * Troisieme constructeur qui permet de créer un profil à partir des
     * informations passées en paramétre (création d'un nouveau profil).
     *
     * @param nom Nom du profil
     * @param dateNaissance Date de naissance du joueur
     * @throws ParserConfigurationException
     */
    public Profil(String nom, String dateNaissance) throws ParserConfigurationException {
        this.nom = nom;
        this.dateNaissance = dateNaissance;
        parties = new ArrayList<>();

        // creates a document DOM
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder;
        docBuilder = docFactory.newDocumentBuilder();
        _doc = docBuilder.newDocument();

        // creates the root element with namespaces
        Element rootElement = _doc.createElementNS("http://myGame/tux", "pr:profil");
        rootElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:pr", "http://myGame/tux");
        rootElement.setAttribute("xmlns:pr", "http://myGame/tux");
        rootElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        rootElement.setAttribute("xsi:schemaLocation", "http://myGame/tux ../xml/profil.xsd");

        // append root element as a child
        _doc.appendChild(rootElement);
        Node racine = _doc.getChildNodes().item(0);

        Element nomElt = _doc.createElement("pr:nom");
        nomElt.setTextContent(nom);

        Element avatarElt = _doc.createElement("pr:avatar");
        avatarElt.setTextContent("avatar.jpg");

        Element anniversaireElt = _doc.createElement("pr:anniversaire");
        anniversaireElt.setTextContent(profileDateToXmlDate(dateNaissance));

        Element partiesElt = _doc.createElement("pr:parties");

        racine.appendChild(nomElt);
        racine.appendChild(avatarElt);
        racine.appendChild(anniversaireElt);
        racine.appendChild(partiesElt);
    }

    /**
     * Ajoute une Partie à la liste des parties du joueur.
     *
     * @param p La partie à ajouter.
     */
    public void ajouterPartie(Partie p) {
        parties.add(p);
    }

    /**
     * Retourne la Partie positionné à l'indice i de la liste.
     *
     * @param i Indice dans la liste de la Partie
     * @return La partie
     */
    public Partie getPartie(int i) {
        return parties.get(i);
    }

    /**
     * Retourne le niveau de la derniére Partie joué.
     *
     * @return Un niveau
     */
    public int getDernierNiveau() {
        return parties.get(parties.size() - 1).getNiveau();
    }

    /**
     * Sauvegarde le profil en fichier XML.
     *
     * @param filename Nom du fichier.
     * @throws ParserConfigurationException
     */
    public void sauvegarder(String filename) throws ParserConfigurationException {
        NodeList tmp_list = _doc.getElementsByTagName("pr:parties");
        Node partiesNode = tmp_list.item(0);
        for (int i = oldNombreParties; i < parties.size(); i++) {
            Element nvellePartie = _doc.createElement("pr:partie");
            nvellePartie.setAttribute("date", profileDateToXmlDate(parties.get(i).getDate()));

            Element mot = _doc.createElement("pr:mot");
            mot.setTextContent(parties.get(i).getMot());
            mot.setAttribute("niveau", Integer.toString(parties.get(i).getNiveau()));
            if (parties.get(i).getTemps() == (parties.get(i).getDureeChrono() / 1000)) {
                nvellePartie.setAttribute("trouvé", Integer.toString(parties.get(i).getTrouvé()) + "%");
            } else {
                Element temps = _doc.createElement("pr:temps");
                temps.setTextContent(Integer.toString(parties.get(i).getTemps()));
                nvellePartie.appendChild(temps);
            }

            nvellePartie.appendChild(mot);

            partiesNode.appendChild(nvellePartie);
        }
        toXML(filename);
    }

    /**
     * Ouvre le profil du joueur en HTML. Pour cela nous générons d'abord un
     * fichier XML grâce à la méthode sauvegarder(String filename).
     *
     * @throws IOException
     * @throws TransformerException
     * @throws ParserConfigurationException
     */
    public void lancerHTML() throws IOException, TransformerException, ParserConfigurationException {
        sauvegarder(getNom() + ".xml");
        xml.FileUtil.stringToFile(XMLUtil.DocumentTransform.fromXSLTransformation(new StreamSource(new File("src/joueur/profil.xsl")), fromXML("src/joueur/" + getNom() + ".xml")), "src/joueur/html/profil_" + getNom() + ".html");
        xml.BrowserUtil.launch("src/joueur/html/profil_" + getNom() + ".html");

    }

    /**
     * Charge les données du profil d'un joueur depuis un fichier XML.
     *
     * @param nom Le nom du joueur.
     * @return true si le profil a pu être chargé | false dans le cas contraire
     */
    public boolean charge(String nom) {
        _doc = fromXML("src/joueur/" + nom + ".xml");

        if (_doc != null) {
            parties = new ArrayList<Partie>();
            this.nom = _doc.getChildNodes().item(0).getChildNodes().item(1).getTextContent();
            this.avatar = _doc.getChildNodes().item(0).getChildNodes().item(3).getTextContent();
            this.dateNaissance = xmlDateToProfileDate(_doc.getChildNodes().item(0).getChildNodes().item(5).getTextContent());

            NodeList partiesXML = _doc.getElementsByTagName("pr:partie");
            for (int i = 0; i < partiesXML.getLength(); i++) {
                if (partiesXML.item(i).getChildNodes().item(1).getNodeName().equals("pr:temps")) {
                    ajouterPartie(new Partie((Element) partiesXML.item(i)));
                } else {
                    ajouterPartie(new Partie(Profil.xmlDateToProfileDate(((Element) partiesXML.item(i)).getAttribute("date")), partiesXML.item(i).getChildNodes().item(1).getTextContent(), Integer.parseInt(((Element) partiesXML.item(i).getChildNodes().item(1)).getAttribute("niveau"))));
                }
            }
            oldNombreParties = parties.size();
            System.out.println("Ce joueur a joué : " + parties.size() + " partie(s).");
            return true;
        }
        return false;
    }

    /**
     * Retourne une partie de la liste aléatoirement.
     *
     * @return Une Partie.
     */
    public Partie recupererPartie() {
        int nombreAleatoire;
        Partie partieRecup;
        int compteur = 0;
        do {
            nombreAleatoire = 0 + (int) (Math.random() * (parties.size() - 0));
            partieRecup = parties.get(nombreAleatoire);
            compteur++;
        } while (partieRecup.getMot().equals("error") && compteur < 20);
        if (compteur >= 20) {
            partieRecup = new Partie("01/01/2000", "error", -1);
        }
        return partieRecup;
    }

    /**
     * Cree un DOM à partir d'un fichier XML.
     *
     * @param nomFichier Le nom du fichier XML.
     * @return Un Document DOM du fichier.
     */
    public Document fromXML(String nomFichier) {
        try {
            return XMLUtil.DocumentFactory.fromFile(nomFichier);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Sauvegarde un DOM en fichier XML.
     *
     * @param nomFichier Nom du fichier.
     */
    public void toXML(String nomFichier) {
        try {
            String fileName = "src/joueur/" + nomFichier;
            XMLUtil.DocumentTransform.writeDoc(_doc, fileName);
        } catch (Exception ex) {
            Logger.getLogger(Profil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Takes a date in XML format (i.e. ????-??-??) and returns a date in
     * profile format: dd/mm/yyyy
     *
     * @param xmlDate Une date en format XML
     * @return Une date en format Profil
     */
    public static String xmlDateToProfileDate(String xmlDate) {
        String date;
        // récupérer le jour
        date = xmlDate.substring(xmlDate.lastIndexOf("-") + 1, xmlDate.length());
        date += "/";
        // récupérer le mois
        date += xmlDate.substring(xmlDate.indexOf("-") + 1, xmlDate.lastIndexOf("-"));
        date += "/";
        // récupérer l'année
        date += xmlDate.substring(0, xmlDate.indexOf("-"));

        return date;
    }

    /**
     * Takes a date in profile format: dd/mm/yyyy and returns a date in XML
     * format (i.e. ????-??-??)
     *
     * @param profileDate Une date en format Profil
     * @return Une date en format XML
     */
    public static String profileDateToXmlDate(String profileDate) {
        String date;
        // Récupérer l'année
        date = profileDate.substring(profileDate.lastIndexOf("/") + 1, profileDate.length());
        date += "-";
        // Récupérer  le mois
        date += profileDate.substring(profileDate.indexOf("/") + 1, profileDate.lastIndexOf("/"));
        date += "-";
        // Récupérer le jour
        date += profileDate.substring(0, profileDate.indexOf("/"));

        return date;
    }

    /**
     * Affiche toutes les parties d'un profil.
     */
    public void afficherParties() {
        System.out.println("Voici les parties de ce profil : ");
        for (int i = 0; i < parties.size(); i++) {
            System.out.println("Partie " + (i + 1) + " : " + parties.get(i).toString());
        }
    }

    /**
     * Retourne le nombre de partie(s) jouée(s) par un profil.
     *
     * @return Le nombre de parties jouée(s)
     */
    public int nbParties() {
        return parties.size();
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return Le nom du joueur.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le nombre de partie(s) déjà jouée(s) au chargement d'un profil.
     *
     * @return Le nombre de partie(s) déjà jouée(s)
     */
    public int getOldNombreParties() {
        return oldNombreParties;
    }

}
