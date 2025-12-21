package lab_09;

import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {

    // ✅ returns true if solved, false if no solution exists
    public boolean solve(SudokuBoard board) throws InvalidGameException {

        List<int[]> missing = new ArrayList<>();
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board.get(r, c) == 0) {
                    missing.add(new int[]{r, c});
                }
            }
        }

        if (missing.size() > 5) {
            throw new InvalidGameException("Solver supports max 5 empty cells");
        }

        return tryFill(0, missing, board);
    }

    private boolean tryFill(int idx, List<int[]> cells, SudokuBoard board) {
        if (idx == cells.size()) {
            ResultCollector rc = new ResultCollector();
            new SequentialVerifier(board, rc).execute();
            return rc.isValid() && !rc.isIncomplete();
        }

        int[] p = cells.get(idx);
        int r = p[0], c = p[1];

        for (int v = 1; v <= 9; v++) {
            board.set(r, c, v);

            // Optional small prune: if becomes invalid early, skip
            ResultCollector rc = new ResultCollector();
            new SequentialVerifier(board, rc).execute();
            if (!rc.isValid()) {
                board.set(r, c, 0);
                continue;
            }

            if (tryFill(idx + 1, cells, board)) {
                return true;
            }
        }

        board.set(r, c, 0);
        return false;
    }
}
