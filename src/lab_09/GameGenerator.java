package lab_09;

import java.util.List;

public class GameGenerator {

    public static int[][] generate(int[][] solved, int holes, RandomPairs rp) {
        int[][] board = copy(solved);
        List<int[]> pairs = rp.generateDistinctPairs(holes);

        for (int[] p : pairs) {
            board[p[0]][p[1]] = 0;
        }
        return board;
    }

    private static int[][] copy(int[][] b) {
        int[][] c = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(b[i], 0, c[i], 0, 9);
        }
        return c;
    }
}
