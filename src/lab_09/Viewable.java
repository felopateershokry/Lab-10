package lab_09;

import java.io.IOException;

/**
 * Controller-side interface. Exposes controller functionality to the viewer
 * (GUI). Signatures are EXACTLY as specified in Lab 10.
 */
public interface Viewable {

    // Returns the game catalog status
    Catalog getCatalog();

    // Returns a random game of the specified difficulty
    Game getGame(DifficultyEnum level) throws NotFoundException;

    // Takes a solved source game and generates Easy/Medium/Hard games
    void driveGames(Game sourceGame) throws SolutionInvalidException;

    // Verifies the given game:
    // "valid", "incomplete", or
    // "invalid x1,y1 x2,y2 ..."
    String verifyGame(Game game);

    // Solves the game when exactly 5 cells are empty
    // Returns the correct combination for the missing numbers
    int[] solveGame(Game game) throws InvalidGameException;

    // Logs a user action (string representation)
    void logUserAction(String userAction) throws IOException;
}
