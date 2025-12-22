package lab_09;

public class SudokuWorker extends Thread {

    private int[][] board;
    private int[][] holes;
    private int start, end;
    public int[] solution = null;
    private volatile boolean solutionFound;

    public SudokuWorker(int[][] board, int[][] holes, int start, int end, boolean solutionFound) {
        this.board = board;
        this.holes = holes;
        this.start = start;
        this.end = end;
        this.solutionFound = solutionFound;
    }

    @Override
    public void run() {
        int[][] testBoard = new int[9][9];
        for (int i = start; i <= end && !solutionFound; i++) {
            int[] perm = getPermutation(i);

            for (int r = 0; r < 9; r++) {
                System.arraycopy(board[r], 0, testBoard[r], 0, 9);
            }

            for (int j = 0; j < 5; j++) {
                testBoard[holes[j][0]][holes[j][1]] = perm[j];
            }

            if (SequentialVerifier.isValid(testBoard)) {
                solution = new int[15];
                int idx = 0;
                for (int j = 0; j < 5; j++) {
                    solution[idx++] = holes[j][0];
                    solution[idx++] = holes[j][1];
                    solution[idx++] = perm[j];
                }
                solutionFound = true;
                break;
            }
        }
    }

    private int[] getPermutation(int counter) {
        int[] perm = new int[5];
        int x = counter;
        for (int i = 0; i < 5; i++) {
            perm[i] = (x % 9) + 1;
            x /= 9;
        }
        return perm;
    }
}
