package lab_09;

import java.util.ArrayList;
import java.util.List;

/**
 * Sudoku solver using permutations ONLY. - Works only when exactly 5 cells are
 * empty - Uses Iterator + Flyweight concepts - No backtracking, no in-place
 * modification of the board
 */
public class SudokuSolver {

    public int[] solveCombination(int[][] board) throws InvalidGameException {

        List<int[]> emptyCells = new ArrayList<>();

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    emptyCells.add(new int[]{r, c});
                }
            }
        }

        if (emptyCells.size() != 5) {
            throw new InvalidGameException(
                    "Solver supports only boards with exactly 5 empty cells"
            );
        }

        PermutationIterator iterator = new PermutationIterator();

        while (iterator.hasNext()) {
            int[] combo = iterator.next(); // length = 5

            if (isValidWithOverlay(board, emptyCells, combo)) {
                return combo;
            }
        }

        throw new InvalidGameException("No valid solution found");
    }

    // Flyweight verification (overlay values without modifying board)
    private boolean isValidWithOverlay(
            int[][] base,
            List<int[]> empty,
            int[] combo
    ) {
        // rows
        for (int r = 0; r < 9; r++) {
            boolean[] seen = new boolean[10];
            for (int c = 0; c < 9; c++) {
                int v = getValue(base, empty, combo, r, c);
                if (v < 1 || v > 9 || seen[v]) {
                    return false;
                }
                seen[v] = true;
            }
        }

        // columns
        for (int c = 0; c < 9; c++) {
            boolean[] seen = new boolean[10];
            for (int r = 0; r < 9; r++) {
                int v = getValue(base, empty, combo, r, c);
                if (v < 1 || v > 9 || seen[v]) {
                    return false;
                }
                seen[v] = true;
            }
        }

        // 3x3 boxes
        for (int br = 0; br < 9; br += 3) {
            for (int bc = 0; bc < 9; bc += 3) {
                boolean[] seen = new boolean[10];
                for (int r = br; r < br + 3; r++) {
                    for (int c = bc; c < bc + 3; c++) {
                        int v = getValue(base, empty, combo, r, c);
                        if (v < 1 || v > 9 || seen[v]) {
                            return false;
                        }
                        seen[v] = true;
                    }
                }
            }
        }

        return true;
    }

    private int getValue(
            int[][] base,
            List<int[]> empty,
            int[] combo,
            int r,
            int c
    ) {
        for (int i = 0; i < empty.size(); i++) {
            int[] pos = empty.get(i);
            if (pos[0] == r && pos[1] == c) {
                return combo[i];
            }
        }
        return base[r][c];
    }
}
