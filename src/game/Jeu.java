/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

import env3d.Env;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.lwjgl.input.Keyboard;
import org.xml.sax.SAXException;

/**
 * Classe comportant les bases d'un Jeu.
 *
 * @author gladen
 */
public abstract class Jeu {

    enum MENU_VAL {

        MENU_SORTIE, MENU_CONTINUE, MENU_JOUE
    }

    private final Env env;
    private Tux tux;
    private final Room mainRoom;
    private final Room menuRoom;
    private Profil profil;
    private final Dico dico;

    /**
     * Menu utiliser pour intéragir avec l'utilisateur
     */
    protected EnvTextMap menuText; //text (affichage des texte du jeu)

    private ArrayList<Letter> lettres;

    /**
     * Le constructeur Jeu initialise une salle de jeu, un profil, une liste de
     * lettres, un dictionnaire et les textes à afficher.
     *
     * @throws SAXException
     * @throws IOException
     * @throws ParserConfigurationException
     */
    public Jeu() throws SAXException, IOException, ParserConfigurationException {
        // Crée un nouvel environnement
        env = new Env();

        // Instancie une Room
        mainRoom = new Room();

        // Instancie une autre Room pour les menus
        menuRoom = new Room();
        menuRoom.setTextureEast("textures/black.png");
        menuRoom.setTextureWest("textures/black.png");
        menuRoom.setTextureNorth("textures/black.png");
        menuRoom.setTextureBottom("textures/black.png");

        // Règle la camera
        env.setCameraXYZ(50, 60, 175);
        env.setCameraPitch(-20);

        // Désactive les contrôles par défaut
        env.setDefaultControl(false);

        // Instancie un profil par défaut
        profil = new Profil();

        // Liste comportant les lettres à trouver
        lettres = new ArrayList<Letter>();

        // Dictionnaire
        dico = new Dico("src/xml/dico.xml");
        dico.lireDictionnaire();

        // Instancie le menuText
        menuText = new EnvTextMap(env);

        // Textes affichés à l'écran
        menuText.addText("Voulez vous ?", "Question", 200, 300);
        menuText.addText("1. Commencer une nouvelle partie ?", "Jeu1", 250, 280);
        menuText.addText("2. Charger une partie existante ?", "Jeu2", 250, 260);
        menuText.addText("3. Sortir de ce jeu ?", "Jeu3", 250, 240);
        menuText.addText("4. Quitter le jeu ?", "Jeu4", 250, 220);
        menuText.addText("Choisissez un nom de joueur : ", "NomJoueur", 200, 300);
        menuText.addText("Nom déjà utilisé !\nChoisissez un nom de joueur : ", "NomJoueurCopy", 200, 300);
        menuText.addText("1. Charger un profil de joueur existant ?", "Principal1", 250, 280);
        menuText.addText("2. Créer un nouveau joueur ?", "Principal2", 250, 260);
        menuText.addText("3. Sortir du jeu ?", "Principal3", 250, 240);
        menuText.addText("4. Ajouter un mot au dictionnaire", "Principal4", 250, 220);
        menuText.addText("Choisissez un niveau : ", "Niveau", 200, 300);
        menuText.addText("Saisir un mot : ", "RecupMot", 200, 300);
        menuText.addText("Aucune partie existante pour ce profil !\nChoisissez un niveau : ", "NiveauAucunePartieExistante", 200, 300);
        menuText.addText("Voulez vous ouvrir votre profil en version HTML ?\nOui (Y) / Non (N) : ", "QuestionHTML", 100, 300);
        menuText.addText("Voulez vous sauvegarder votre profil et vos derniéres parties (XML) ?\nOui (Y) / Non (N) : ", "QuestionSave", 100, 300);
    }

    /**
     * Gère le menu principal
     *
     * @throws javax.xml.parsers.ParserConfigurationException
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     * @throws javax.xml.transform.TransformerException
     */
    public void execute() throws ParserConfigurationException, SAXException, IOException, TransformerException {

        System.out.println("");
        System.out.println("------------- Bienvenue ! -------------");
        MENU_VAL mainLoop;
        mainLoop = MENU_VAL.MENU_SORTIE;
        do {
            mainLoop = menuPrincipal();
        } while (mainLoop != MENU_VAL.MENU_SORTIE);

        if (profil.nbParties() > 0) {
            // restaure la room du menu
            env.setRoom(menuRoom);
            profil.afficherParties();
            saveToXml();
        }

        this.env.setDisplayStr("Au revoir !", 300, 30);
        System.out.println("");
        System.out.println("------------- Au revoir ! -------------");
        //On quitte le jeu
        env.exit();
    }

    // fourni
    private String getNomJoueur() {
        String nomJoueur = "";
        menuText.getText("NomJoueur").display();
        nomJoueur = menuText.getText("NomJoueur").lire(true);
        menuText.getText("NomJoueur").clean();
        return nomJoueur;
    }

    private MENU_VAL menuJeu() throws SAXException, ParserConfigurationException, IOException {

        MENU_VAL playTheGame;
        playTheGame = MENU_VAL.MENU_JOUE;
        Partie partie;
        do {
            // Déclaration des variables qui vont stocker les infos d'un mot (On aurais pu faire une classe)
            int niveau;
            String unMot;

            // Restaure la room du menu
            env.setRoom(menuRoom);

            // Affiche menu
            menuText.getText("Question").display();
            menuText.getText("Jeu1").display();
            menuText.getText("Jeu2").display();
            menuText.getText("Jeu3").display();
            menuText.getText("Jeu4").display();

            // Vérifie qu'une touche 1, 2, 3 ou 4 est pressée
            int touche = 0;
            while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4)) {
                touche = env.getKey();
                env.advanceOneFrame();
            }

            // Nettoie l'environnement du texte
            menuText.getText("Question").clean();
            menuText.getText("Jeu1").clean();
            menuText.getText("Jeu2").clean();
            menuText.getText("Jeu3").clean();
            menuText.getText("Jeu4").clean();

            // Vide la liste de lettre à chercher
            lettres.clear();

            // Et décide quoi faire en fonction de la touche pressée
            switch (touche) {
                // -----------------------------------------
                // Touche 1 : Commencer une nouvelle partie
                // -----------------------------------------                
                case Keyboard.KEY_1:
                    // choisi un niveau et charge un mot depuis le dico
                    try {
                        niveau = Integer.parseInt(getNiveau());
                        unMot = dico.getMotDepuisListeNiveau(niveau);
                        afficherMotATrouver(unMot);

                        // Initialise une liste des lettres à chercher
                        initLettres(unMot);

                        // Crée une nouvelle partie à la date actuelle
                        partie = new Partie(getTodayDate(), unMot, niveau);


                        // Restaure la room du jeu
                        env.setRoom(mainRoom);

                        System.out.println("---- Début d'une nouvelle partie ! ----");
                        // On lance la partie
                        joue(partie);

                        // On enregistre la partie dans le profil
                        profil.ajouterPartie(partie);

                        // On retourne au début du menu de jeu
                        playTheGame = MENU_VAL.MENU_JOUE;
                    } catch (Exception e) {
                        // Dans le cas où le niveau contient des caractéres.
                        System.out.println("Le niveau saisi contient des caractéres ! Partie annulé.");
                        playTheGame = MENU_VAL.MENU_JOUE;
                    }

                    break;

                // -----------------------------------------
                // Touche 2 : Charger une partie existante
                // -----------------------------------------                
                case Keyboard.KEY_2:
                    // Charge une partie existante au hasard
                    try {
                        Partie unePartie;

                        if (profil.nbParties() == 0) {
                            // Dans le cas où le profil n'a aucune parties on demande de lancer au moin une partie
                            niveau = Integer.parseInt(getNiveauAucunePartieExistante());
                            unMot = dico.getMotDepuisListeNiveau(niveau);
                            afficherMotATrouver(unMot);
                            partie = new Partie(getTodayDate(), unMot, niveau);
                        } else {
                            // Dans le cas où le profil a des parties on charge une partie au hasard
                            unePartie = profil.recupererPartie();
                            unMot = unePartie.getMot();
                            afficherMotATrouver(unMot);
                            partie = new Partie(getTodayDate(), unMot, unePartie.getNiveau());
                        }

                        // Initialise une liste des lettres à chercher
                        initLettres(unMot);

                        // Restaure la room du jeu
                        env.setRoom(mainRoom);

                        System.out.println("---- Début d'une nouvelle partie ! ----");
                        // On lance la partie
                        joue(partie);

                        // On enregistre la partie dans le profil
                        profil.ajouterPartie(partie);

                        // On retourne au début du menu de jeu
                        playTheGame = MENU_VAL.MENU_JOUE;

                    } catch (Exception e) {
                        // Dans le cas où le niveau contient des caractéres.
                        System.out.println("Le niveau saisi contient des caractéres ! Partie annulé.");
                        playTheGame = MENU_VAL.MENU_JOUE;
                    }

                    break;

                // -----------------------------------------
                // Touche 3 : Sortie de ce jeu
                // -----------------------------------------                
                case Keyboard.KEY_3:
                    playTheGame = MENU_VAL.MENU_CONTINUE;
                    break;

                // -----------------------------------------
                // Touche 4 : Quitter le jeu
                // -----------------------------------------                
                case Keyboard.KEY_4:
                    playTheGame = MENU_VAL.MENU_SORTIE;
            }
        } while (playTheGame == MENU_VAL.MENU_JOUE);
        return playTheGame;
    }

    private MENU_VAL menuPrincipal() throws SAXException, ParserConfigurationException, IOException, TransformerException {

        MENU_VAL choix = MENU_VAL.MENU_CONTINUE;
        String nomJoueur;

        // Restaure la room du menu
        env.setRoom(menuRoom);

        menuText.getText("Question").display();
        menuText.getText("Principal1").display();
        menuText.getText("Principal2").display();
        menuText.getText("Principal3").display();
        menuText.getText("Principal4").display();

        // Vérifie qu'une touche 1, 2 ou 3 est pressée
        int touche = 0;
        while (!(touche == Keyboard.KEY_1 || touche == Keyboard.KEY_2 || touche == Keyboard.KEY_3 || touche == Keyboard.KEY_4)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }

        menuText.getText("Question").clean();
        menuText.getText("Principal1").clean();
        menuText.getText("Principal2").clean();
        menuText.getText("Principal3").clean();
        menuText.getText("Principal4").clean();

        // Décide quoi faire en fonction de la touche pressée
        switch (touche) {
            // -------------------------------------
            // Touche 1 : Charger un profil existant
            // -------------------------------------
            case Keyboard.KEY_1:
                // Demande le nom du joueur existant
                nomJoueur = getNomJoueur();

                // Charge le profil de ce joueur si possible
                if (profil.charge(nomJoueur)) {
                    choix = menuJeu();
                } else {
                    // Si impossible alors on créer un joueur portant ce nom
                    profil = new Profil(nomJoueur, "12/07/2020");
                    choix = menuJeu();
                }
                break;

            // -------------------------------------
            // Touche 2 : Créer un nouveau joueur
            // -------------------------------------
            case Keyboard.KEY_2:
                // Crée un profil avec le nom d'un nouveau joueur
                int compteur = 0;
                do {
                    if (compteur == 0) {
                        // On demande la saisi du nom du joueur
                        nomJoueur = getNomJoueur();
                    } else {
                        // Si le nom est déjà utilisé, on redemande et averti l'utilisateur
                        nomJoueur = getNomJoueurCopy();
                    }
                    compteur++;
                } while (profil.charge(nomJoueur));

                // Création du profil
                profil = new Profil(nomJoueur, "12/07/2020");

                // On part dans le menu du jeu.
                choix = menuJeu();
                break;

            // -------------------------------------
            // Touche 4 : Ajouter un mot
            // -------------------------------------
            case Keyboard.KEY_4:
                // On récupére le mot 
                String mot = getMot();

                try { // On récupére le niveau
                    int niveau = Integer.parseInt(getNiveau());

                    // Ajout du mot grâce à la classe EditeurDico
                    EditeurDico edtDico = new EditeurDico();
                    edtDico.lireDOM("src/xml/dico.xml");
                    edtDico.ajouterMot(mot, niveau);
                    edtDico.ecrireDOM("src/xml/dico.xml");
                } catch (Exception e) {
                    System.out.println("Le niveau saisi contient des caractéres ! Ajout annulé.");
                }

                // On retourne au menu principal
                choix = menuPrincipal();
                break;

            // -------------------------------------
            // Touche 3 : Sortir du jeu
            // -------------------------------------
            case Keyboard.KEY_3:
                choix = MENU_VAL.MENU_SORTIE;
        }
        return choix;
    }

    /**
     * Méthode comportant l'appelle aux fonctions démarrePartie, appliqueRegles
     * et terminePartie. Boucle tant que le chronométre n'a pas atteint la
     * limite et que le joueur ne clique pas sur Echap. Fait avancer le moteur
     * de jeu (mise à jour de l'affichage, de l'écoute des événements
     * clavier...).
     *
     * @param partie Partie dans la quelle nous allons stocker les informations.
     */
    public void joue(Partie partie) {

        // Instancie un Tux
        tux = new Tux(env, mainRoom);
        env.addObject(tux);

        // Ici, on peut initialiser des valeurs pour une nouvelle partie
        démarrePartie(partie);

        // Boucle de jeu
        Boolean finished;
        finished = false;
        for (Letter l : lettres) {
            env.addObject(l);
        }

        while (!finished && appliqueRegles(partie)) {

            // Contrôles globaux du jeu (sortie, ...)
            //1 is for escape key
            if (env.getKey() == 1) {
                finished = true;
            }

            // Contrôles des déplacements de Tux (gauche, droite, ...)
            tux.déplace();

            // Ici, on applique les regles
            appliqueRegles(partie);

            // Fait avancer le moteur de jeu (mise à jour de l'affichage, de l'écoute des événements clavier...)
            env.advanceOneFrame();
        }

        // Ici on peut calculer des valeurs lorsque la partie est terminée
        terminePartie(partie);

        //probleme ECHAP
        afficheResultat(partie, env, menuRoom);
    }

    /**
     * Action à réaliser en début de partie.
     *
     * @param partie La partie en cour
     */
    protected abstract void démarrePartie(Partie partie);

    /**
     * Régle du jeu.
     *
     * @param partie La partie en cour
     * @return Boolean qui permet de savoir si la partie continue
     */
    protected abstract boolean appliqueRegles(Partie partie);

    /**
     * Action à réaliser en fin de partie.
     *
     * @param partie La partie en cour
     */
    protected abstract void terminePartie(Partie partie);

    /**
     * Affiche les statistiques d'une partie.
     *
     * @param partie La partie à afficher
     * @param env Pour mettre le menu à l'écran
     * @param menuRoom On passe le menu en paramètre pour ajouter les textes
     */
    protected abstract void afficheResultat(Partie partie, Env env, Room menuRoom);

    /**
     * Calcule la distance entre une lettre et le personnage.
     *
     * @param letter Une lettre
     * @return La distance entre le personnage et la lettre passé en paramétre.
     */
    protected double distance(Letter letter) {
        double xLettre = letter.getX();
        double zLettre = letter.getZ();
        double distance;
        distance = Math.sqrt((Math.pow(xLettre - tux.getX(), 2) + Math.pow(zLettre - tux.getZ(), 2)));
        return distance;
    }

    /**
     * Indique si le personnage touche une lettre.
     *
     * @param letter Une lettre
     * @return true si le personnage touche la lettre passé en paramétre, false
     * sinon.
     */
    protected boolean collision(Letter letter) {
        return distance(letter) <= 4;
    }

    /**
     * Retourne la liste des lettres à trouver.
     *
     * @return Une liste de Letter.
     */
    public ArrayList<Letter> getLettres() {
        return lettres;
    }

    /**
     * Retourne l'environnement de jeu.
     *
     * @return Un environnement
     */
    public Env getEnv() {
        return env;
    }

    private int rand() {
        return 15 + (int) (Math.random() * ((85 - 15) + 1));
    }

    private void initLettres(String unMot) {
        int xMot;
        int yMot;
        int compteur = 0;
        for (int i = 0; i < unMot.length(); i++) {
            if (compteur % 2 == 0) {
                xMot = rand() + (rand() % 10);
                yMot = rand() - (rand() % 10);
            } else {
                xMot = rand() - (rand() % 10);
                yMot = rand() + (rand() % 10);
            }
            compteur++;
            lettres.add(new Letter(unMot.charAt(i), xMot, yMot));
        }
    }

    private String getTodayDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }

    private String getNomJoueurCopy() {
        String nomJoueur = "";
        menuText.getText("NomJoueurCopy").display();
        nomJoueur = menuText.getText("NomJoueurCopy").lire(true);
        menuText.getText("NomJoueurCopy").clean();
        return nomJoueur;
    }

    /**
     * Méthode qui renvoi la liste des Letter identique à celle passé en
     * paramétre (permet de savoir si un mot comporte plusieurs fois la même
     * lettre).
     *
     * @param l Une Letter
     * @return Une liste des lettres identique à celle-ci
     */
    public ArrayList<Letter> retourneAllLetterEqual(Letter l) {
        ArrayList<Letter> lettresEqual = new ArrayList<>();
        for (Letter lettre : lettres) {
            if (lettre.getLetter() == l.getLetter()) {
                lettresEqual.add(lettre);
            }
        }
        return lettresEqual;
    }

    /**
     * Retourne l'indice de la lettre passé en paramétre dans la liste des
     * lettres recherchées.
     *
     * @param l Une Letter
     * @return Sa place dans la liste
     */
    public int getIndexOfALetter(Letter l) {
        int i = 0;
        while (i < lettres.size() && (lettres.get(i).equals(l) == false)) {
            i++;
        }
        return i;
    }
    
    //Demande à l'utilisateur de saisir un niveau
    private String getNiveau() {
        String niveau = "";
        menuText.getText("Niveau").display();
        niveau = menuText.getText("Niveau").lire(true);
        menuText.getText("Niveau").clean();
        return niveau;
    }

    //Demande à l'utilisateur de saisir un mot
    private String getMot() {
        String Mot = "";
        menuText.getText("RecupMot").display();
        Mot = menuText.getText("RecupMot").lire(true);
        menuText.getText("RecupMot").clean();
        return Mot;
    }

    //Demande à l'utilisateur de saisir un niveau lorsqu'il a aucune partie enregistrée 
    private String getNiveauAucunePartieExistante() {
        String niveau = "";
        menuText.getText("NiveauAucunePartieExistante").display();
        niveau = menuText.getText("NiveauAucunePartieExistante").lire(true);
        menuText.getText("NiveauAucunePartieExistante").clean();
        return niveau;
    }

    // Affiche le mot à trouver
    private void afficherMotATrouver(String mot) {
        // Restaure la room du menu
        env.setRoom(menuRoom);
        menuText.addText("Mot à trouver : " + mot + " - Appuyer sur entrée pour lancer la partie.", "Find", 100, 300);
        menuText.addText("Vous avez 20 secondes.", "TimeLeft", 230, 280);
        menuText.getText("Find").display();
        menuText.getText("TimeLeft").display();
        int touche = 0;
        while (!(touche == Keyboard.KEY_RETURN)) {
            touche = env.getKey();
            env.advanceOneFrame();
        }
        menuText.getText("Find").clean();
        menuText.getText("TimeLeft").clean();
    }

    // Demande à l'utilisateur si il souhaite ouvrir son profil en HTML ou l'enregistrer en XML
    private void saveToXml() throws ParserConfigurationException, TransformerException, IOException {
        String reponse = "";

        menuText.getText("QuestionHTML").display();
        reponse = menuText.getText("QuestionHTML").lire(true);
        if (reponse.equals("Y") || reponse.equals("y")) {
            profil.lancerHTML();
        } else {
            menuText.getText("QuestionHTML").clean();
            menuText.getText("QuestionSave").display();
            reponse = menuText.getText("QuestionSave").lire(true);
            if (reponse.equals("Y") || reponse.equals("y")) {
                profil.sauvegarder(profil.getNom() + ".xml");
            }
            menuText.getText("QuestionSave").clean();
        }
        menuText.getText("QuestionHTML").clean();

    }

}
