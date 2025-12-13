package lab_09;

public class GameDriver {

    private final GameStorage storage = new GameStorage("games");
    private final DifficultyGenerator generator = new DifficultyGenerator();

    public int[][] generateNewGame(Difficulty level) throws Exception {
        int[][] solved = storage.loadSolvedBoard();
        return generator.generate(solved, level);
    }

    public void driveGamesFromSolved(int[][] solved) throws Exception {
        verifySolvedBoard(new SudokuBoard(solved));
        storage.saveSolvedBoard(solved);
    }

    public String verifyGame(int[][] game) {
        SudokuBoard board = new SudokuBoard(game);
        ResultCollector rc = new ResultCollector();
        new SequentialVerifier(board, rc).execute();

        if (rc.isIncomplete()) {
            return "INCOMPLETE";
        }
        if (!rc.isValid()) {
            return "INVALID";
        }
        return "VALID";
    }

    public void solveGame(int[][] game) throws Exception {
        SudokuSolver solver = new SudokuSolver();
        boolean ok = solver.solve(new SudokuBoard(game));
        if (!ok) {
            throw new InvalidGameException("No valid solution exists");
        }
    }

    // ✅ NEW
    public boolean isCorrectMove(int r, int c, int val) throws Exception {
        int[][] solved = storage.loadSolvedBoard();
        return solved[r][c] == val;
    }

    private void verifySolvedBoard(SudokuBoard board) throws Exception {
        ResultCollector rc = new ResultCollector();
        new SequentialVerifier(board, rc).execute();

        if (rc.isIncomplete() || !rc.isValid()) {
            throw new SolutionInvalidException("Solved board is INVALID");
        }
    }
}
