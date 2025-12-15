package lab_09;

/**
 * Controller-side only. Represents the game catalog status as required by Lab
 * 10.
 */
public class Catalog {

    // True if there is an unfinished (incomplete) game.
    public boolean current;

    // True if there is at least one game for EACH difficulty: easy, medium, hard.
    public boolean allModesExist;

    public Catalog(boolean current, boolean allModesExist) {
        this.current = current;
        this.allModesExist = allModesExist;
    }
}
