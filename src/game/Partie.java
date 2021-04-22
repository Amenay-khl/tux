/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 * Stocke les informations d'une partie.
 *
 * @author Yanis
 */
public class Partie {

    private String date;
    private String mot;
    private int niveau;
    private int trouve;
    private int temps;
    private int dureeChrono;

    /**
     * Constructeur d'une partie avec 3 paramétres.
     *
     * @param date Date de la partie
     * @param mot Mot recherché dans la partie
     * @param niveau Niveau du mot recherché
     */
    public Partie(String date, String mot, int niveau) {
        this.date = date;
        this.mot = mot;
        setNiveau(niveau);
    }

    /**
     * Constructeur d'une partie avec 1 seul paramètre.
     *
     * @param partieElt Un element Partie issu d'un XML
     */
    public Partie(Element partieElt) {
        this(Profil.xmlDateToProfileDate(partieElt.getAttribute("date")), partieElt.getChildNodes().item(3).getTextContent(), Integer.parseInt(((Element) partieElt.getChildNodes().item(3)).getAttribute("niveau")));
    }

    /**
     * Cette méthode crée le bloc XML représentant une partie à partir du
     * paramètre doc(pour créer les éléments du XML) et renvoie ce bloc en tant
     * que Element.
     *
     * @param doc Un document
     * @return Un Element partie.
     */
    public Element getPartie(Document doc) {
        Element partie;
        partie = doc.createElement("partie");
        partie.setAttribute("date", getDate());

        Element temps = doc.createElement("temps");
        temps.setTextContent(Integer.toString(getTemps()));

        Element mot = doc.createElement("mot");
        mot.setTextContent(getMot());
        mot.setAttribute("niveau", Integer.toString(getNiveau()));

        partie.appendChild(temps);
        partie.appendChild(mot);

        return partie;
    }

    /**
     * Enregistre le pourcentage de lettre trouvé du mot trouvé.
     *
     * @param nbLettresRestantes Nombre de lettre à trouver restante.
     */
    public void setTrouve(int nbLettresRestantes) {
        if (nbLettresRestantes == 0) {
            trouve = 100;
        } else if (nbLettresRestantes == mot.length()) {
            trouve = 0;
        } else {
            double percentage = (double) nbLettresRestantes / mot.length();
            percentage = 100 - (percentage * 100);
            trouve = (int) percentage;
        }
    }

    /**
     * Enregistre la limite du chronométre.
     *
     * @param duree La limite du chronométre en seconde.
     */
    public void setDureeChrono(int duree) {
        dureeChrono = duree;
    }

    /**
     * Retourne la limite du chronométre.
     *
     * @return La limite du chronométre en seconde.
     */
    public int getDureeChrono() {
        return dureeChrono;
    }

    /**
     * Enregistre le temp mis pour trouver le mot.
     *
     * @param temps Le temps mis.
     */
    public void setTemps(int temps) {
        if (temps == 0) {
            temps += 1;
        }
        this.temps = temps;
    }

    private void setNiveau(int niveau) {
        if (niveau > 5 || niveau < 1) {
            this.niveau = -1;
        } else {
            this.niveau = niveau;
        }
    }

    /**
     * Retourne le niveau d'une partie.
     *
     * @return Un niveau.
     */
    public int getNiveau() {
        return niveau;
    }

    /**
     * Retourne le mot d'une partie.
     *
     * @return Un mot.
     */
    public String getMot() {
        return mot;
    }

    /**
     * Retourne le temps mis durant partie.
     *
     * @return Le temps mis.
     */
    public int getTemps() {
        return temps;
    }

    /**
     * Retourne le pourcentage de lettre trouvé du mot.
     *
     * @return le pourcentage de lettre trouvé du mot
     */
    public int getTrouvé() {
        return trouve;
    }

    /**
     * Retoune la date d'une partie.
     *
     * @return La date d'une partie.
     */
    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Partie{" + "date=" + date + ", mot=" + mot + ", niveau=" + niveau + '}';
    }
}
