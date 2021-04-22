package game;

/**
 * Permet de mettre en place un chronométre.
 *
 * @author Yanis
 */
public class Chronometre {

    private long begin;
    private long end;
    private long current;
    private int limite;

    /**
     * Initialisation du chronométre avec un temp limite.
     *
     * @param limite Durée maximum de notre chronométre.
     */
    public Chronometre(int limite) {
        //intialisation
        start();
        this.limite = limite;
    }

    /**
     * Attribue la valeur de début du chronométre à l'attribut "begin".
     */
    public void start() {
        begin = System.currentTimeMillis();
    }

    /**
     * Attribue la valeur de fin du chronométre à l'attribut "end".
     */
    public void stop() {
        end = System.currentTimeMillis();
    }

    /**
     * Retourne la différence entre le début et la fin du chronométre.
     *
     * @return long - différence (en milliseconde)
     */
    public long getTime() {
        return end - begin;
    }

    /**
     * Retourne la différence entre le début et la fin du chronométre.
     *
     * @return long - différence (en milliseconde)
     */
    public long getMilliseconds() {
        return end - begin;
    }

    /**
     * Retourne la différence entre le début et la fin du chronométre.
     *
     * @return int - différence (en seconde)
     */
    public int getSeconds() {
        return (int) ((end - begin) / 1000.0);
    }

    /**
     * Retourne la différence entre le début et la fin du chronométre.
     *
     * @return double - différence (en minute)
     */
    public double getMinutes() {
        return (end - begin) / 60000.0;
    }

    /**
     * Retourne la différence entre le début et la fin du chronométre.
     *
     * @return long - différence (en heure)
     */
    public double getHours() {
        return (end - begin) / 3600000.0;
    }

    /**
     * Permet de savoir si il reste du temps (par rapport à notre limite).
     *
     * @return boolean - true si il reste du temp | false si il ne reste plus de
     * temp
     */
    public boolean remainsTime() {
        current = System.currentTimeMillis();
        int timeSpent;
        timeSpent = (int) ((current - begin) / 1000.0);
        return (timeSpent < limite / 1000);
    }

}
