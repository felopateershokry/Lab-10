package lab_09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class GameStorage {

    private static final Path ROOT = Paths.get("storage");
    private static final Path CURRENT = ROOT.resolve("current");
    private static final Path EASY = ROOT.resolve("easy");
    private static final Path MEDIUM = ROOT.resolve("medium");
    private static final Path HARD = ROOT.resolve("hard");

    private Path SOLVED_PATH = ROOT.resolve("solved.csv");

    public void setSOLVED_PATH(Path SOLVED_PATH) {
        this.SOLVED_PATH = SOLVED_PATH;
    }

    public GameStorage() throws IOException {
        Files.createDirectories(CURRENT);
        Files.createDirectories(EASY);
        Files.createDirectories(MEDIUM);
        Files.createDirectories(HARD);
    }

    public boolean hasCurrentGame() {
        return Files.exists(CURRENT.resolve("game.csv"));
    }

    public boolean hasEasyMediumHard() {
        try {
            return Files.list(EASY).findAny().isPresent()
                    && Files.list(MEDIUM).findAny().isPresent()
                    && Files.list(HARD).findAny().isPresent();
        } catch (IOException e) {
            return false;
        }
    }

    public void saveSolvedPath(String path) throws IOException {
        Files.writeString(SOLVED_PATH, path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    public Path getSOLVED_PATH() {
        return SOLVED_PATH;
    }

    public int[][] loadBoardFromPath(String path) {
        int[][] board = new int[9][9];

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            for (int r = 0; r < 9; r++) {
                String line = br.readLine();
                if (line == null) {
                    break;
                }

                String[] parts = line.split(",");
                for (int c = 0; c < 9; c++) {
                    board[r][c] = Integer.parseInt(parts[c].trim());
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading CSV: " + e.getMessage());
        }
        return board;
    }

    public String loadSolvedPath() throws IOException {
        if (!Files.exists(SOLVED_PATH)) {
            return null;
        }
        return Files.readString(SOLVED_PATH).trim();
    }

    public void saveCurrentGame(int[][] board) throws IOException {
        writeCSV(CURRENT.resolve("game.csv"), board);
    }

    public int[][] loadCurrentGame() throws IOException {
        return readCSV(CURRENT.resolve("game.csv"));
    }

    public static void deleteCurrent() throws IOException {
        Files.deleteIfExists(CURRENT.resolve("game.csv"));
        Files.deleteIfExists(CURRENT.resolve("log.txt"));
    }

    public int[][] loadGame(DifficultyEnum d) throws IOException {
        Path dir = dirOf(d);
        List<Path> files = Files.list(dir).toList();
        if (files.isEmpty()) {
            return null;
        }

        Path chosen = files.get(new Random().nextInt(files.size()));
        Files.copy(chosen, CURRENT.resolve("game.csv"),
                StandardCopyOption.REPLACE_EXISTING);

        return readCSV(CURRENT.resolve("game.csv"));
    }

    public void saveGenerated(DifficultyEnum d, int[][] board, int idx) throws IOException {
        writeCSV(dirOf(d).resolve("game" + idx + ".csv"), board);
    }

    private Path dirOf(DifficultyEnum d) {
        return switch (d) {
            case EASY ->
                EASY;
            case MEDIUM ->
                MEDIUM;
            case HARD ->
                HARD;
        };
    }

    private void writeCSV(Path p, int[][] b) throws IOException {
        List<String> lines = new ArrayList<>();
        for (int[] r : b) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < 9; j++) {
                sb.append(r[j]);
                if (j < 8) {
                    sb.append(",");
                }
            }
            lines.add(sb.toString());
        }
        Files.write(p, lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    private int[][] readCSV(Path p) throws IOException {
        List<String> lines = Files.readAllLines(p);
        int[][] b = new int[9][9];
        for (int i = 0; i < 9; i++) {
            String[] s = lines.get(i).split(",");
            for (int j = 0; j < 9; j++) {
                b[i][j] = Integer.parseInt(s[j].trim());
            }
        }
        return b;
    }
}
