package lab_09;

public interface Controllable {

    Catalog getCatalog() throws Exception;

    int[][] getGame(char level) throws Exception;

    void driveGames(int[][] solvedBoard) throws Exception;

    String verifyGame(int[][] game);

    int[] solveGame(int[][] game) throws Exception;

    void logUserAction(int[][] board, int x, int y, int val, int prev) throws Exception;

    boolean undoLastAction(int[][] game) throws Exception;

    void clearCurrentGame() throws Exception;

    // ✅ NEW
    boolean isCorrectMove(int r, int c, int val) throws Exception;
}
