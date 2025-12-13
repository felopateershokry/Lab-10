package lab_09;

public interface Viewable {

    Catalog getCatalog() throws Exception;

    int[][] getGame(Difficulty level) throws Exception;

    void driveGames(int[][] solvedBoard) throws Exception;

    String verifyGame(int[][] game);

    int[] solveGame(int[][] game) throws Exception;
}
