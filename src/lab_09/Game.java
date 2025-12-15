package lab_09;

/**
 * Controller-side ONLY. Represents a Sudoku game. IMPORTANT: board is passed by
 * reference (no deep copy), exactly as required by Lab 10.
 */
public class Game {

    public final int[][] board;

    public Game(int[][] board) {
        // DO NOT copy the board by value
        this.board = board;
    }
}
