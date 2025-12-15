package lab_09;

import java.util.List;

/**
 * Generates Sudoku games of different difficulty levels by removing cells from
 * a solved source board. Uses RandomPairs with time-based seed as required in
 * Lab 10.
 */
public class SudokuSolutionGenerator {

    private final int[][] source; // reference to solved board

    public SudokuSolutionGenerator(int[][] solvedBoard) {
        // IMPORTANT: use reference, do NOT deep copy here
        this.source = solvedBoard;
    }

    /**
     * Generates a new board by removing `removeCount` cells. 10 -> Easy, 20 ->
     * Medium, 25 -> Hard
     */
    public int[][] generate(int removeCount) {

        // Make a working copy ONLY for the generated board
        int[][] board = new int[9][9];
        for (int r = 0; r < 9; r++) {
            System.arraycopy(source[r], 0, board[r], 0, 9);
        }

        RandomPairs rp = new RandomPairs();
        List<int[]> pairs = rp.generateDistinctPairs(removeCount);

        for (int[] p : pairs) {
            board[p[0]][p[1]] = 0;
        }

        return board;
    }
}
