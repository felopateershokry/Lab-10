package lab_09;

import lab_09.Catalog;
import java.io.IOException;

/**
 * Controller-side implementation. Implements Viewable exactly as required in
 * Lab 10.
 */
public class SudokuController implements Viewable {

    private final GameStorage storage;
    private final SequentialVerifier verifier;
    private final SudokuSolver solver;

    public SudokuController() {
        this.storage = new GameStorage("games");
        this.verifier = new SequentialVerifier();
        this.solver = new SudokuSolver();
    }

    @Override
    public Catalog getCatalog() {
        boolean hasIncomplete = storage.hasIncompleteGame();
        boolean hasAll = storage.hasAllDifficulties();
        return new Catalog(hasIncomplete, hasAll);
    }

    @Override
    public Game getGame(DifficultyEnum level) throws NotFoundException {
        int[][] board = storage.load(level);
        return new Game(board);
    }

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        ResultCollector result = verifier.verify(sourceGame.board);

        if (!result.isValid() || result.isIncomplete()) {
            throw new SolutionInvalidException("Source Sudoku is invalid or incomplete");
        }

        SudokuSolutionGenerator generator
                = new SudokuSolutionGenerator(sourceGame.board);

        storage.save(DifficultyEnum.EASY, generator.generate(10));
        storage.save(DifficultyEnum.MEDIUM, generator.generate(20));
        storage.save(DifficultyEnum.HARD, generator.generate(25));
    }

    @Override
    public String verifyGame(Game game) {
        ResultCollector r = verifier.verify(game.board);

        if (!r.isValid()) {
            return "invalid";
        }
        if (r.isIncomplete()) {
            return "incomplete";
        }
        return "valid";
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGameException {
        return solver.solveCombination(game.board);
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        storage.log(userAction);
    }
}
