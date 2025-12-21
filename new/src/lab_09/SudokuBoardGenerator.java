package lab_09;

import java.util.*;

public class SudokuBoardGenerator {

    private final Random rnd = new Random();

    public int[][] generateSolvedBoard() {
        int[][] board = new int[9][9];
        fill(board, 0, 0);
        return board;
    }

    public int[][] generatePuzzle(DifficultyEnum diff) {
        int[][] solved = generateSolvedBoard();
        int[][] puzzle = deepCopy(solved);

        int remove = switch (diff) {
            case EASY ->
                10;
            case MEDIUM ->
                20;
            case HARD ->
                25;
        };

        removeCells(puzzle, remove);
        return puzzle;
    }

    private boolean fill(int[][] b, int r, int c) {
        if (r == 9) {
            return true;
        }

        int nextR = (c == 8) ? r + 1 : r;
        int nextC = (c + 1) % 9;

        List<Integer> nums = new ArrayList<>();
        for (int n = 1; n <= 9; n++) {
            nums.add(n);
        }
        Collections.shuffle(nums, rnd);

        for (int n : nums) {
            if (isSafe(b, r, c, n)) {
                b[r][c] = n;
                if (fill(b, nextR, nextC)) {
                    return true;
                }
                b[r][c] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int[][] b, int r, int c, int v) {
        for (int i = 0; i < 9; i++) {
            if (b[r][i] == v || b[i][c] == v) {
                return false;
            }
        }

        int br = (r / 3) * 3, bc = (c / 3) * 3;
        for (int i = br; i < br + 3; i++) {
            for (int j = bc; j < bc + 3; j++) {
                if (b[i][j] == v) {
                    return false;
                }
            }
        }

        return true;
    }

    private void removeCells(int[][] b, int count) {
        List<int[]> cells = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells.add(new int[]{i, j});
            }
        }

        Collections.shuffle(cells, rnd);

        int removed = 0;
        for (int[] p : cells) {
            if (removed >= count) {
                break;
            }
            if (b[p[0]][p[1]] != 0) {
                b[p[0]][p[1]] = 0;
                removed++;
            }
        }
    }

    private int[][] deepCopy(int[][] src) {
        int[][] c = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(src[i], 0, c[i], 0, 9);
        }
        return c;
    }
}
