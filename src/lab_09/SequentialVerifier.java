package lab_09;

/**
 * Single sequential verifier. No multithreading – exactly as required in Lab
 * 10.
 */
public class SequentialVerifier {

    public ResultCollector verify(int[][] board) {
        ResultCollector result = new ResultCollector();

        // Check rows
        for (int r = 0; r < 9; r++) {
            boolean[] seen = new boolean[10];
            for (int c = 0; c < 9; c++) {
                int v = board[r][c];
                if (v == 0) {
                    result.markIncomplete();
                    continue;
                }
                if (seen[v]) {
                    result.markInvalid();
                }
                seen[v] = true;
            }
        }

        // Check columns
        for (int c = 0; c < 9; c++) {
            boolean[] seen = new boolean[10];
            for (int r = 0; r < 9; r++) {
                int v = board[r][c];
                if (v == 0) {
                    result.markIncomplete();
                    continue;
                }
                if (seen[v]) {
                    result.markInvalid();
                }
                seen[v] = true;
            }
        }

        // Check 3x3 boxes
        for (int br = 0; br < 9; br += 3) {
            for (int bc = 0; bc < 9; bc += 3) {
                boolean[] seen = new boolean[10];
                for (int r = br; r < br + 3; r++) {
                    for (int c = bc; c < bc + 3; c++) {
                        int v = board[r][c];
                        if (v == 0) {
                            result.markIncomplete();
                            continue;
                        }
                        if (seen[v]) {
                            result.markInvalid();
                        }
                        seen[v] = true;
                    }
                }
            }
        }

        return result;
    }
}
