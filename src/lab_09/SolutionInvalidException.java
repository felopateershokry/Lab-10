package lab_09;

/**
 * Thrown when the provided solved Sudoku source is invalid or incomplete.
 */
public class SolutionInvalidException extends Exception {

    public SolutionInvalidException(String message) {
        super(message);
    }
}
