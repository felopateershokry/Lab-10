package lab_09;

import java.util.HashMap;
import java.util.Map;

public class SudokuSolver {

    private static class PermutationFactory {

        private final Map<Integer, int[]> cache = new HashMap<>();

        public int[] getPermutation(int counter) {
            return cache.computeIfAbsent(counter, c -> {
                int[] perm = new int[5];
                int x = c;
                for (int i = 0; i < 5; i++) {
                    perm[i] = (x % 9) + 1;
                    x /= 9;
                }
                return perm;
            });
        }
    }

    private final PermutationFactory factory = new PermutationFactory();

    public int[] solve(int[][] board) throws InvalidGameException {

        int[][] holes = new int[5][2];
        int k = 0;
        int zeroCount = 0;

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board[i][j] == 0) {
                    if (k < 5) {
                        holes[k++] = new int[]{i, j};
                    }
                    zeroCount++;
                }
            }
        }

        if (zeroCount != 5) {
            throw new InvalidGameException("Exactly 5 empty cells required");
        }

        int max = (int) Math.pow(9, 5);
        for (int counter = 0; counter < max; counter++) {
            int[] perm = factory.getPermutation(counter);
            int[][] test = copy(board);

            for (int i = 0; i < 5; i++) {
                test[holes[i][0]][holes[i][1]] = perm[i];
            }

            if (SequentialVerifier.isValid(test)) {
                int[] out = new int[15];
                int idx = 0;
                for (int i = 0; i < 5; i++) {
                    out[idx++] = holes[i][0];
                    out[idx++] = holes[i][1];
                    out[idx++] = perm[i];
                }
                return out;
            }
        }

        throw new InvalidGameException("No solution found");
    }

    private int[][] copy(int[][] b) {
        int[][] c = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(b[i], 0, c[i], 0, 9);
        }
        return c;
    }
}
