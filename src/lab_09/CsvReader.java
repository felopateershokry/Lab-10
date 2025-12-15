package lab_09;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing Sudoku boards from/to CSV files. Used
 * by controller and storage layers.
 */
public class CsvReader {

    // Reads a 9x9 Sudoku board from a CSV file
    public static int[][] readBoard(String path) throws IOException {
        List<int[]> rows = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split(",");
                if (parts.length != 9) {
                    throw new IOException("Invalid Sudoku row length");
                }
                int[] row = new int[9];
                for (int i = 0; i < 9; i++) {
                    row[i] = Integer.parseInt(parts[i].trim());
                }
                rows.add(row);
            }
        }

        if (rows.size() != 9) {
            throw new IOException("Invalid Sudoku board size");
        }

        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            board[i] = rows.get(i);
        }
        return board;
    }

    // Writes a 9x9 Sudoku board to a CSV file
    public static void writeBoard(String path, int[][] board) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    bw.write(Integer.toString(board[r][c]));
                    if (c < 8) {
                        bw.write(",");
                    }
                }
                bw.newLine();
            }
        }
    }
}
