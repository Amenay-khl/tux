/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * Permet de définir les paramétres d'une Room (taille, texture, etc..).
 *
 * @author Yanis
 */
public class Room {

    private int depth;
    private int height;
    private int width;
    private String textureBottom;
    private String textureNorth;
    private String textureEast;
    private String textureWest;
    private String textureTop;
    private String textureSouth;

    /**
     * Constructeur par défaut d'une room. Initialise la taille, et les
     * textures.
     *
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws IOException
     */
    public Room() throws SAXException, ParserConfigurationException, IOException {

        // analyse du document
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder p = dbFactory.newDocumentBuilder();
        // récupération de la structure objet du document
        Document doc = p.parse("src/xml/plateau.xml");
        Node racine = doc.getChildNodes().item(1);

        this.depth = Integer.parseInt(racine.getChildNodes().item(1).getChildNodes().item(5).getTextContent());;
        this.height = Integer.parseInt(racine.getChildNodes().item(1).getChildNodes().item(1).getTextContent());
        this.width = Integer.parseInt(racine.getChildNodes().item(1).getChildNodes().item(3).getTextContent());
        this.textureBottom = racine.getChildNodes().item(3).getChildNodes().item(1).getTextContent();
        this.textureNorth = racine.getChildNodes().item(3).getChildNodes().item(3).getTextContent();
        this.textureEast = racine.getChildNodes().item(3).getChildNodes().item(5).getTextContent();
        this.textureWest = racine.getChildNodes().item(3).getChildNodes().item(7).getTextContent();
    }

    /**
     * Retourne la profondeur.
     *
     * @return La profondeur
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Retourne la hauteur.
     *
     * @return La hauteur
     */
    public int getHeigth() {
        return height;
    }

    /**
     * Retourne la largeur.
     *
     * @return La largeur
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retourne la texture de la face bas de la Room.
     *
     * @return La texture de la face bas de la Room
     */
    public String getTextureBottom() {
        return textureBottom;
    }

    /**
     * Retourne la texture de la face nord de la Room.
     *
     * @return La texture de la face nord de la Room
     */
    public String getTextureNorth() {
        return textureNorth;
    }

    /**
     * Retourne la texture de la face Est de la Room.
     *
     * @return La texture de la face Est de la Room
     */
    public String getTextureEast() {
        return textureEast;
    }

    /**
     * Retourne la texture de la face Ouest de la Room.
     *
     * @return La texture de la face Ouest de la Room
     */
    public String getTextureWest() {
        return textureWest;
    }

    /**
     * Retourne la texture de la face Haut de la Room.
     *
     * @return La texture de la face Haut de la Room
     */
    public String getTextureTop() {
        return textureTop;
    }

    /**
     * Retourne la texture de la face Sud de la Room.
     *
     * @return La texture de la face Sud de la Room
     */
    public String getTextureSouth() {
        return textureSouth;
    }

    /**
     * Défini la profondeur de la Room.
     *
     * @param depth La profondeur de la Room
     */
    public void setDepth(int depth) {
        this.depth = depth;
    }

    /**
     * Défini l'hauteur de la Room.
     *
     * @param height L'hauteur de la Room
     */
    public void setHeigth(int height) {
        this.height = height;
    }

    /**
     * Défini la largeur de la Room.
     *
     * @param width La largeur de la Room
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Permet de définir la texture de la face Bas de la Room.
     *
     * @param textureBottom La texture de la face Bas de la Room
     */
    public void setTextureBottom(String textureBottom) {
        this.textureBottom = textureBottom;
    }

    /**
     * Permet de définir la texture de la face Nord de la Room.
     *
     * @param textureNorth La texture de la face Nord de la Room
     */
    public void setTextureNorth(String textureNorth) {
        this.textureNorth = textureNorth;
    }

    /**
     * Permet de définir la texture de la face Est de la Room.
     *
     * @param textureEast La texture de la face Est de la Room
     */
    public void setTextureEast(String textureEast) {
        this.textureEast = textureEast;
    }

    /**
     * Permet de définir la texture de la face Ouest de la Room.
     *
     * @param textureWest La texture de la face Ouest de la Room
     */
    public void setTextureWest(String textureWest) {
        this.textureWest = textureWest;
    }

    /**
     * Permet de définir la texture de la face Haut de la Room.
     *
     * @param textureTop La texture de la face Haut de la Room
     */
    public void setTextureTop(String textureTop) {
        this.textureTop = textureTop;
    }

    /**
     * Permet de définir la texture de la face Sud de la Room.
     *
     * @param textureSouth La texture de la face Sud de la Room
     */
    public void setTextureSouth(String textureSouth) {
        this.textureSouth = textureSouth;
    }

}