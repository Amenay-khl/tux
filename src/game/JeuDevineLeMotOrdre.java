/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.lwjgl.input.Keyboard;
import org.xml.sax.SAXException;

/**
 * Classe du jeu "Jeu devine le mot dans l'ordre", implémente les méthodes
 * démarrePartie, appliqueRegles, terminePartie.
 *
 * @author Yanis
 */
public class JeuDevineLeMotOrdre extends Jeu {

    int affichage = 0;
    int duree_chrono = 20000;
    private int nbLettresRestantes;
    private Chronometre chrono;

    /**
     * Constructeur du jeu "Jeu devine le mot dans l'ordre", celui-ci fait juste
     * appelle au super constructeur (Jeu).
     *
     * @throws SAXException
     * @throws IOException
     * @throws javax.xml.parsers.ParserConfigurationException
     */
    public JeuDevineLeMotOrdre() throws SAXException, IOException, ParserConfigurationException {

    }

    @Override
    protected void démarrePartie(Partie partie) {
        chrono = new Chronometre(duree_chrono);
        partie.setDureeChrono(duree_chrono);
        nbLettresRestantes = partie.getMot().length();
        affichage = 0;
    }

    @Override
    protected boolean appliqueRegles(Partie partie) {
        boolean joue = true;
        if (nbLettresRestantes == 0) {
            if (affichage == 0) {
                joue = false;
                System.out.println("Victoire !");
                System.out.println("Lettre restante : " + nbLettresRestantes + ", temps écoulé : " + chrono.getTime() + " millisecondes.");
                System.out.println("---------- Partie terminée ! ----------\n");
                affichage++;
            }
        } else if (chrono.remainsTime()) {
            if (tuxTrouveLettreV2(getEnv())) {
                nbLettresRestantes--;
            }
            if (nbLettresRestantes == 0) {
                chrono.stop();
            }
        } else {
            if (affichage == 0) {
                chrono.stop();
                System.out.println("Perdu !");
                System.out.println("Partie finie lettre(s) restante(s) : " + nbLettresRestantes + ", temps écoulé : " + chrono.getTime() + " millisecondes.");
                System.out.println("---------- Partie terminée ! ----------\n");
                affichage++;
                joue = false;
            }
        }
        return joue;
    }

    @Override
    protected void terminePartie(Partie partie) {
        //si trouvé alors enregistrer le temps
        if (chrono.getTime() < 0) {
            chrono.stop();
            partie.setTemps(-1);
        } else {
            partie.setTemps(chrono.getSeconds());
        }
        partie.setTrouve(nbLettresRestantes);
    }

    //Ancienne version pour trouver les lettres
    private boolean tuxTrouveLettre(Env env) {
        boolean trouve = false;
        if (distance(super.getLettres().get(0)) <= 6) {
            trouve = true;
            env.removeObject(super.getLettres().get(0));
            super.getLettres().remove(0);
        }
        return trouve;
    }

    private boolean tuxTrouveLettreV2(Env env) {
        boolean trouve = false;
        int i = 0;
        for (Letter l : retourneAllLetterEqual(super.getLettres().get(0))) {
            i++;
            if (distance(l) <= 6) {
                trouve = true;
                if (getIndexOfALetter(l) != 0) {
                    Letter tmp = getLettres().get(getIndexOfALetter(l));
                    getLettres().set(getIndexOfALetter(l), getLettres().get(0));
                    getLettres().set(0, tmp);
                }
                env.removeObject(super.getLettres().get(0));
                super.getLettres().remove(0);
            }
        }
        return trouve;
    }

    @Override
    protected void afficheResultat(Partie partie, Env env, Room menuRoom) {
        // restaure la room du menu
        env.setRoom(menuRoom);
        if (partie.getTrouvé() == 100) {
            menuText.addText("Victoire !", "Resultat", 300, 310);
            menuText.addText("Temps écoulé : " + partie.getTemps() + " secondes.", "ResultatTemp", 240, 280);
            menuText.addText("Pourcentage du mot trouvé : " + partie.getTrouvé() + "%.", "ResultatPourcentage", 215, 250);
        } else {
            menuText.addText("Perdu !", "Resultat", 300, 310);
            if (partie.getTemps() < 0) {
                menuText.addText("Partie annulée.", "ResultatTemp", 275, 280);
            } else {
                menuText.addText("Temps écoulé : " + partie.getTemps() + " secondes.", "ResultatTemp", 240, 280);
            }
            menuText.addText("Pourcentage du mot trouvé : " + partie.getTrouvé() + "%.", "ResultatPourcentage", 215, 250);
        }

        menuText.addText("Appuyer sur Enter pour continuer !", "Continuer", 210, 220);
        menuText.getText("Resultat").display();
        menuText.getText("ResultatTemp").display();
        menuText.getText("ResultatPourcentage").display();
        menuText.getText("Continuer").display();

        int touche = 0;
        while (!(touche == Keyboard.KEY_RETURN)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Continuer").clean();
        menuText.getText("Resultat").clean();
        menuText.getText("ResultatTemp").clean();
        menuText.getText("ResultatPourcentage").clean();
        menuText.getText("Resultat").destroy();
        menuText.getText("ResultatTemp").destroy();
        menuText.getText("ResultatPourcentage").destroy();
    }

}
