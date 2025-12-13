package lab_09;

import java.util.Random;

public class DifficultyGenerator {

    public int[][] generate(int[][] solved, Difficulty level) {

        int remove;
        switch (level) {
            case EASY ->
                remove = 10;
            case MEDIUM ->
                remove = 20;
            case HARD ->
                remove = 30;
            default ->
                throw new IllegalArgumentException();
        }

        int[][] board = copy(solved);
        Random rnd = new Random(System.currentTimeMillis());

        int removed = 0;
        while (removed < remove) {
            int r = rnd.nextInt(9);
            int c = rnd.nextInt(9);

            if (board[r][c] != 0) {
                board[r][c] = 0;
                removed++;
            }
        }

        return board;
    }

    private int[][] copy(int[][] src) {
        int[][] c = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(src[i], 0, c[i], 0, 9);
        }
        return c;
    }
}
