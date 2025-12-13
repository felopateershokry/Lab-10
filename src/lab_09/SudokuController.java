package lab_09;

public class SudokuController implements Controllable {

    private final GameDriver driver = new GameDriver();
    private final GameStorage storage = new GameStorage("games");
    private final UndoManager undo = new UndoManager("games");

    @Override
    public Catalog getCatalog() throws Exception {
        return storage.getCatalog();
    }

    // ✅ ONLY for generating NEW games
    @Override
    public int[][] getGame(char level) throws Exception {
        Difficulty d = switch (Character.toUpperCase(level)) {
            case 'E' ->
                Difficulty.EASY;
            case 'M' ->
                Difficulty.MEDIUM;
            case 'H' ->
                Difficulty.HARD;
            default ->
                throw new IllegalArgumentException("Invalid level");
        };

        int[][] game = driver.generateNewGame(d);
        storage.saveCurrentGame(game);
        undo.clearLog();
        return game;
    }

    // ✅ NEW: load unfinished current game
    public int[][] loadCurrentGame() throws Exception {
        return storage.loadCurrentGame();
    }

    @Override
    public void driveGames(int[][] solvedBoard) throws Exception {
        driver.driveGamesFromSolved(solvedBoard);
    }

    @Override
    public String verifyGame(int[][] game) {
        return driver.verifyGame(game);
    }

    @Override
    public int[] solveGame(int[][] game) throws Exception {
        driver.solveGame(game);
        storage.saveCurrentGame(game);
        undo.clearLog();
        return null;
    }

    @Override
    public void logUserAction(int[][] board, int x, int y, int val, int prev) throws Exception {
        undo.append(x, y, val, prev);
        storage.saveCurrentGame(board);
    }

    @Override
    public boolean undoLastAction(int[][] game) throws Exception {
        boolean ok = undo.undo(game);
        if (ok) {
            storage.saveCurrentGame(game);
        }
        return ok;
    }

    @Override
    public void clearCurrentGame() throws Exception {
        storage.deleteCurrentGame();
        undo.clearLog();
    }

    @Override
    public boolean isCorrectMove(int r, int c, int val) throws Exception {
        return driver.isCorrectMove(r, c, val);
    }
}
