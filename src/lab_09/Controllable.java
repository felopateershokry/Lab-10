package lab_09;
import java.io.IOException;
public interface Controllable {
    boolean[] getCatalog();
    int[][] getGame(char level) throws NotFoundException;
    void driveGames(String sourcePath) throws SolutionInvalidException;
    boolean[][] verifyGame(int[][] board);
    int[][] solveGame(int[][] game) throws InvalidGameException;
    void logUserAction(UserAction userAction) throws IOException;
}