package lab_09;

import java.io.*;

public class CsvReader {

    public static int[][] readBoard(String path) throws IOException {
        int[][] board = new int[9][9];

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            for (int r = 0; r < 9; r++) {
                String[] parts = br.readLine().split(",");
                for (int c = 0; c < 9; c++) {
                    board[r][c] = Integer.parseInt(parts[c].trim());
                }
            }
        }
        return board;
    }
}
