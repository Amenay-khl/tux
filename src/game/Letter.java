/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.advanced.EnvNode;

/**
 * Utilisé pour génerer les lettres à trouver.
 *
 * @author Yanis
 */
public class Letter extends EnvNode {

    private char letter;

    /**
     * Constructeur d'une Letter qui prend en paramètre une lettre en char et
     * ses coordonnées x et y.
     *
     * @param letter Une lettre (char)
     * @param x Coordonnée x de la lettre dans la salle
     * @param y Coordonnée y de la lettre dans la salle
     */
    public Letter(char letter, double x, double y) {
        this.letter = letter;
        setScale(3.0);
        setX(x);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(y); // positionnement au milieu de la profondeur de la room
        setModel("/models/letter/cube.obj");

        if (letter == '-') {
            setTexture("/models/letter/cube.png");
        } else {
            setTexture("/models/letter/" + letter + ".png");
        }
    }

    /**
     * Retourne la lettre en char.
     *
     * @return La lettre
     */
    public char getLetter() {
        return letter;
    }

}
