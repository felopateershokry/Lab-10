package lab_09;

public class SudokuBoard {

    private final int[][] board;

    public SudokuBoard(int[][] b) {
        board = b;
    }

    public int get(int r, int c) {
        return board[r][c];
    }

    public void set(int r, int c, int v) {
        board[r][c] = v;
    }

    public int[][] raw() {
        return board;
    }
}
