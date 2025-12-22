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

<<<<<<< Updated upstream:src/lab_09/SudokuSolver.java
        int max = (int) Math.pow(9, 5);
        for (int counter = 0; counter < max; counter++) {
            int[] perm = factory.getPermutation(counter);
            int[][] test = copy(board);
=======
        final int totalPerms = (int) Math.pow(9, 5);
        int numThreads = 4; // مثال: 4 threads
        int chunk = totalPerms / numThreads;
        SudokuWorker[] workers = new SudokuWorker[numThreads];
        boolean solutionFound = false;

        for (int t = 0; t < numThreads; t++) {
            int start = t * chunk;
            int end = (t == numThreads - 1) ? totalPerms - 1 : (start + chunk - 1);
            workers[t] = new SudokuWorker(board, holes, start, end, solutionFound);
            workers[t].start();
        }
>>>>>>> Stashed changes:lab-10/src/lab_09/SudokuSolver.java

        for (SudokuWorker w : workers) {
            try {
                w.join();
                if (w.solution != null) {
                    return w.solution;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
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
