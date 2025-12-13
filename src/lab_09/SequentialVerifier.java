package lab_09;

import java.util.*;

public class SequentialVerifier {

    private final SudokuBoard board;
    private final ResultCollector rc;

    public SequentialVerifier(SudokuBoard b, ResultCollector r) {
        board = b;
        rc = r;
    }

    public void execute() {
        for (int i = 0; i < 9; i++) {
            checkRow(i);
            checkCol(i);
            checkBox(i);
        }
    }

    private void checkRow(int r) {
        boolean[] seen = new boolean[10];
        for (int c = 0; c < 9; c++) {
            int v = board.get(r, c);
            if (v == 0) {
                rc.markIncomplete();
            } else if (seen[v]) {
                rc.markInvalid();
            } else {
                seen[v] = true;
            }
        }
    }

    private void checkCol(int c) {
        boolean[] seen = new boolean[10];
        for (int r = 0; r < 9; r++) {
            int v = board.get(r, c);
            if (v == 0) {
                rc.markIncomplete();
            } else if (seen[v]) {
                rc.markInvalid();
            } else {
                seen[v] = true;
            }
        }
    }

    private void checkBox(int b) {
        boolean[] seen = new boolean[10];
        int br = (b / 3) * 3;
        int bc = (b % 3) * 3;

        for (int r = br; r < br + 3; r++) {
            for (int c = bc; c < bc + 3; c++) {
                int v = board.get(r, c);
                if (v == 0) {
                    rc.markIncomplete();
                } else if (seen[v]) {
                    rc.markInvalid();
                } else {
                    seen[v] = true;
                }
            }
        }
    }
}
