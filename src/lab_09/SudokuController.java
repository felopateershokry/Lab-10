package lab_09;

import java.io.IOException;

public class SudokuController implements Viewable {

    private final SequentialVerifier verifier = new SequentialVerifier();
    private final GameStorage storage;

    public SudokuController() throws IOException {
        storage = new GameStorage();
    }

    @Override
    public Catalog getCatalog() {
        return new Catalog(storage.hasCurrentGame(),
                storage.hasEasyMediumHard());
    }

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        try {
            int[][] b = storage.loadGame(level);
            if (b == null) {
                throw new NotFoundException("No game available");
            }
            return new Game(b);
        } catch (IOException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    @Override
    public void driveGames(Game source) throws SolutionInvalidException {
        String res = verifier.verify(source.board);
        if (!res.equals("valid")) {
            throw new SolutionInvalidException("Solved sudoku must be VALID");
        }

        try {
            RandomPairs rp = new RandomPairs();
            int[][] easy = GameGenerator.generate(source.board, 10, rp);
            int[][] medium = GameGenerator.generate(source.board, 20, rp);
            int[][] hard = GameGenerator.generate(source.board, 25, rp);

            storage.saveGenerated(DifficultyEnum.EASY, easy, 0);
            storage.saveGenerated(DifficultyEnum.MEDIUM, medium, 0);
            storage.saveGenerated(DifficultyEnum.HARD, hard, 0);

        } catch (Exception e) {
            throw new SolutionInvalidException(e.getMessage());
        }
    }

    @Override
    public String verifyGame(Game game) {
        return verifier.verify(game.board);
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        return new SudokuSolver().solve(game.board);
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
    }
}
