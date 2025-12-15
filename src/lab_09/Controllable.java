package lab_09;

import java.io.IOException;

/**
 * Viewer-side interface. Represents the actions triggered by the GUI.
 * Signatures are EXACTLY as specified in Lab 10.
 */
public interface Controllable {

    // Returns two booleans:
    // [0] -> is there an unfinished game?
    // [1] -> are easy, medium, and hard games available?
    boolean[] getCatalog();

    // Loads a game by difficulty:
    // 'E' -> Easy, 'M' -> Medium, 'H' -> Hard, 'I' -> Incomplete
    int[][] getGame(char level) throws NotFoundException;

    // Takes the path to a solved Sudoku file and generates difficulty levels
    void driveGames(String sourcePath) throws SolutionInvalidException;

    // Verifies the given game.
    // Returns a boolean matrix indicating valid/invalid cells
    boolean[][] verifyGame(int[][] game);

    // Solves the game when exactly 5 cells are empty.
    // Returns {row, col, value} for each missing cell
    int[][] solveGame(int[][] game) throws InvalidGameException;

    // Logs a user action
    void logUserAction(UserAction userAction) throws IOException;
}
