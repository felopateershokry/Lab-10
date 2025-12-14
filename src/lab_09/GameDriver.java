package lab_09;

import java.util.*;

public class GameDriver {

    private int[][] solvedBoard;   // 🔥 الحل الكامل
    private final Random rand = new Random();

    // ================= GENERATE GAME =================
    public int[][] generateNewGame(Difficulty d) {

        // 1️⃣ Generate solved board
        solvedBoard = generateSolvedBoard();

        // 2️⃣ Copy solved → game board
        int[][] game = copyBoard(solvedBoard);

        // 3️⃣ Remove cells based on difficulty
        int toRemove = switch (d) {
            case EASY ->
                10;
            case MEDIUM ->
                25;
            case HARD ->
                20;
        };

        removeRandomCells(game, toRemove);
        return game;
    }

    // ================= VERIFY =================
    public String verifyGame(int[][] game) {
        ResultCollector rc = new ResultCollector();
        SequentialVerifier v = new SequentialVerifier(
                new SudokuBoard(game), rc
        );
        v.execute();

        if (rc.isIncomplete()) {
            return "INCOMPLETE";
        }
        if (!rc.isValid()) {
            return "INVALID";
        }
        return "VALID";
    }

    // ================= SOLVE =================
    public void solveGame(int[][] game) {
        if (solvedBoard == null) {
            throw new IllegalStateException("Solved board not available");
        }

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                game[r][c] = solvedBoard[r][c];
            }
        }
    }

    // ================= CHECK MOVE =================
    public boolean isCorrectMove(int r, int c, int val) {
        if (solvedBoard == null) {
            return false;
        }
        return solvedBoard[r][c] == val;
    }

    // ================= INTERNAL =================
    private int[][] generateSolvedBoard() {
        int[][] board = new int[9][9];
        fill(board, 0, 0);
        return board;
    }

    private boolean fill(int[][] board, int r, int c) {
        if (r == 9) {
            return true;
        }
        if (c == 9) {
            return fill(board, r + 1, 0);
        }

        List<Integer> nums = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            nums.add(i);
        }
        Collections.shuffle(nums);

        for (int n : nums) {
            if (isSafe(board, r, c, n)) {
                board[r][c] = n;
                if (fill(board, r, c + 1)) {
                    return true;
                }
                board[r][c] = 0;
            }
        }
        return false;
    }

    private boolean isSafe(int[][] b, int r, int c, int n) {
        for (int i = 0; i < 9; i++) {
            if (b[r][i] == n || b[i][c] == n) {
                return false;
            }
        }

        int br = (r / 3) * 3;
        int bc = (c / 3) * 3;
        for (int i = br; i < br + 3; i++) {
            for (int j = bc; j < bc + 3; j++) {
                if (b[i][j] == n) {
                    return false;
                }
            }
        }

        return true;
    }

    private void removeRandomCells(int[][] b, int count) {
        while (count > 0) {
            int r = rand.nextInt(9);
            int c = rand.nextInt(9);
            if (b[r][c] != 0) {
                b[r][c] = 0;
                count--;
            }
        }
    }

    private int[][] copyBoard(int[][] src) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(src[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}
