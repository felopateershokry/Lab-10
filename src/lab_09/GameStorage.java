package lab_09;

import java.io.*;

public class GameStorage {

    private final File base;

    public GameStorage(String root) {
        base = new File(root);
        base.mkdirs();
    }

    public void saveSolvedBoard(int[][] board) throws Exception {
        save(new File(base, "solved.csv"), board);
    }

    public int[][] loadSolvedBoard() throws Exception {
        return CsvReader.readBoard(new File(base, "solved.csv").getPath());
    }

    public void saveCurrentGame(int[][] board) throws Exception {
        save(new File(base, "current.csv"), board);
    }

    public int[][] loadCurrentGame() throws Exception {
        return CsvReader.readBoard(new File(base, "current.csv").getPath());
    }

    public void deleteCurrentGame() {
        new File(base, "current.csv").delete();
    }

    private void save(File f, int[][] b) throws Exception {
        try (PrintWriter pw = new PrintWriter(f)) {
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    pw.print(b[r][c]);
                    if (c < 8) {
                        pw.print(",");
                    }
                }
                pw.println();
            }
        }
    }

    public Catalog getCatalog() {
        return new Catalog(
                new File(base, "current.csv").exists(),
                new File(base, "solved.csv").exists()
        );
    }
}
