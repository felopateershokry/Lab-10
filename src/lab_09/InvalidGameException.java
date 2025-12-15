package lab_09;

/**
 * Thrown when a game is invalid for solving (e.g., not exactly 5 empty cells)
 * or when no solution can be found.
 */
public class InvalidGameException extends Exception {

    public InvalidGameException(String message) {
        super(message);
    }
}
