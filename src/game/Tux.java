/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;
import env3d.advanced.EnvNode;
import org.lwjgl.input.Keyboard;

/**
 * Permet la génération d'un TUX.
 *
 * @author Yanis
 */
public class Tux extends EnvNode {

    private Env env;
    private Room room;

    /**
     * Constructeur qui instancie un nouveau Tux dans un Environnement et une
     * Room.
     *
     * @param env Un environnement
     * @param room Une room
     */
    public Tux(Env env, Room room) {
        this.env = env;
        this.room = room;
        setScale(4.0);
        setX(50);// positionnement au milieu de la largeur de la room
        setY(getScale() * 1.1); // positionnement en hauteur basé sur la taille de Tux
        setZ(50); // positionnement au milieu de la profondeur de la room
        setTexture("models/tux/tux_special.png");
        setModel("models/tux/tux.obj");
    }

    /**
     * Permet le déplacement du Tux dans l'environnement grâce à une lecture des
     * touches préssées.
     */
    public void déplace() {
        if (env.getKeyDown(Keyboard.KEY_Z) || env.getKeyDown(Keyboard.KEY_UP)) { // Fleche 'haut' ou Z
            // Haut
            this.setRotateY(180);
            if (this.getZ() > 4) {
                this.setZ(this.getZ() - 1.0);
            }

        }
        if (env.getKeyDown(Keyboard.KEY_Q) || env.getKeyDown(Keyboard.KEY_LEFT)) { // Fleche 'gauche' ou Q
            // Gauche
            this.setRotateY(270);
            if (this.getX() > 4) {
                this.setX(this.getX() - 1.0);
            }
        }
        if (env.getKeyDown(Keyboard.KEY_D) || env.getKeyDown(Keyboard.KEY_RIGHT)) { // Fleche 'droite' ou D
            // Droite
            this.setRotateY(90);
            if (this.getX() < this.room.getWidth() - 4) {
                this.setX(this.getX() + 1.0);
            }
        }
        if (env.getKeyDown(Keyboard.KEY_S) || env.getKeyDown(Keyboard.KEY_DOWN)) { // Fleche 'bas' ou S
            // Bas
            this.setRotateY(0);
            if (this.getZ() < this.room.getDepth() - 4) {
                this.setZ(this.getZ() + 1.0);
            }
        }
    }

}
