package lab_09;

import java.util.Random;

public class SudokuSolutionGenerator {

    private final Random rnd;

    public SudokuSolutionGenerator() {
        this.rnd = new Random(System.currentTimeMillis());
    }

    public int[][] generateSolved() {
        int[][] g = copy(SudokuBaseSolution.BASE);

        // do many random safe shuffles
        for (int i = 0; i < 40; i++) {
            int op = rnd.nextInt(5);
            switch (op) {
                case 0 ->
                    swapRowsInBand(g);
                case 1 ->
                    swapColsInStack(g);
                case 2 ->
                    swapRowBands(g);
                case 3 ->
                    swapColStacks(g);
                case 4 ->
                    permuteDigits(g);
            }
        }
        return g;
    }

    // ---------- Safe Operations ----------
    private void swapRowsInBand(int[][] g) {
        int band = rnd.nextInt(3);         // 0..2
        int r1 = band * 3 + rnd.nextInt(3);
        int r2 = band * 3 + rnd.nextInt(3);
        swapRows(g, r1, r2);
    }

    private void swapColsInStack(int[][] g) {
        int stack = rnd.nextInt(3);        // 0..2
        int c1 = stack * 3 + rnd.nextInt(3);
        int c2 = stack * 3 + rnd.nextInt(3);
        swapCols(g, c1, c2);
    }

    private void swapRowBands(int[][] g) {
        int b1 = rnd.nextInt(3);
        int b2 = rnd.nextInt(3);
        for (int i = 0; i < 3; i++) {
            swapRows(g, b1 * 3 + i, b2 * 3 + i);
        }
    }

    private void swapColStacks(int[][] g) {
        int s1 = rnd.nextInt(3);
        int s2 = rnd.nextInt(3);
        for (int i = 0; i < 3; i++) {
            swapCols(g, s1 * 3 + i, s2 * 3 + i);
        }
    }

    private void permuteDigits(int[][] g) {
        // Create a random mapping for digits 1..9
        int[] map = new int[10];
        int[] perm = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        // Fisher-Yates shuffle for perm
        for (int i = perm.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = perm[i];
            perm[i] = perm[j];
            perm[j] = tmp;
        }

        for (int d = 1; d <= 9; d++) {
            map[d] = perm[d - 1];
        }

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                g[r][c] = map[g[r][c]];
            }
        }
    }

    // ---------- Helpers ----------
    private void swapRows(int[][] g, int r1, int r2) {
        if (r1 == r2) {
            return;
        }
        int[] tmp = g[r1];
        g[r1] = g[r2];
        g[r2] = tmp;
    }

    private void swapCols(int[][] g, int c1, int c2) {
        if (c1 == c2) {
            return;
        }
        for (int r = 0; r < 9; r++) {
            int tmp = g[r][c1];
            g[r][c1] = g[r][c2];
            g[r][c2] = tmp;
        }
    }

    private int[][] copy(int[][] src) {
        int[][] out = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(src[i], 0, out[i], 0, 9);
        }
        return out;
    }
}
