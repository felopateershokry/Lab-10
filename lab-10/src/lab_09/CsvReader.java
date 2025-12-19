package lab_09;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class CsvReader {

    public static int[][] read(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        if (lines.size() < 9) {
            throw new IOException("CSV must contain 9 lines");
        }

        int[][] board = new int[9][9];
        for (int i = 0; i < 9; i++) {
            String[] values = lines.get(i).split(",");
            if (values.length < 9) {
                throw new IOException("Each line must contain 9 values");
            }

            for (int j = 0; j < 9; j++) {
                board[i][j] = Integer.parseInt(values[j].trim());
            }
        }
        return board;
    }
}
