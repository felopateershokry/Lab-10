package lab_09;

/**
 * Thrown when a requested game or resource is not found (e.g., no game for a
 * selected difficulty).
 */
public class NotFoundException extends Exception {

    public NotFoundException(String message) {
        super(message);
    }
}
