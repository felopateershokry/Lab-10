package lab_09;

import java.util.Random;

public class DifficultyGenerator {

    public int[][] makePuzzle(int[][] solved, DifficultyEnum level) {

        int empty;
        switch (level) {
            case EASY ->
                empty = 10;
            case MEDIUM ->
                empty = 25;
            case HARD ->
                empty = 20;
            default ->
                throw new IllegalArgumentException("Unknown difficulty");
        }

        int[][] puzzle = copy(solved);
        Random rnd = new Random(System.currentTimeMillis());

        int removed = 0;
        while (removed < empty) {
            int r = rnd.nextInt(9);
            int c = rnd.nextInt(9);
            if (puzzle[r][c] != 0) {
                puzzle[r][c] = 0;
                removed++;
            }
        }

        return puzzle;
    }

    private int[][] copy(int[][] src) {
        int[][] out = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(src[i], 0, out[i], 0, 9);
        }
        return out;
    }
}
