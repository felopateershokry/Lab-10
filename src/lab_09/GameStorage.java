package lab_09;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

public class GameStorage {

    private final Path root;

    public GameStorage(String rootFolder) {
        this.root = Paths.get(rootFolder);
        initFolders();
    }

    private void initFolders() {
        try {
            Files.createDirectories(root.resolve("easy"));
            Files.createDirectories(root.resolve("medium"));
            Files.createDirectories(root.resolve("hard"));
            Files.createDirectories(root.resolve("incomplete"));
        } catch (IOException ignored) {
        }
    }

    // ✅ PDF: incomplete folder either empty OR exactly 2 files (game + log)
    public boolean hasIncompleteGame() {
        File dir = root.resolve("incomplete").toFile();
        File[] files = dir.listFiles();
        return files != null && files.length == 2 && Files.exists(root.resolve("incomplete").resolve("game.csv"));
    }

    // ✅ لازم game.csv موجود في كل مستوى
    public boolean hasAllDifficulties() {
        return Files.exists(root.resolve("easy").resolve("game.csv"))
                && Files.exists(root.resolve("medium").resolve("game.csv"))
                && Files.exists(root.resolve("hard").resolve("game.csv"));
    }

    public int[][] load(DifficultyEnum level) throws NotFoundException {
        Path file = root.resolve(level.name().toLowerCase()).resolve("game.csv");
        if (!Files.exists(file)) {
            throw new NotFoundException("Game not found: " + level);
        }
        try {
            return CsvReader.readBoard(file.toString());
        } catch (IOException e) {
            throw new NotFoundException("Cannot read game file: " + level);
        }
    }

    // تحميل incomplete
    public int[][] loadIncomplete() throws NotFoundException {
        Path file = root.resolve("incomplete").resolve("game.csv");
        if (!Files.exists(file)) {
            throw new NotFoundException("Incomplete game not found");
        }
        try {
            return CsvReader.readBoard(file.toString());
        } catch (IOException e) {
            throw new NotFoundException("Cannot read incomplete game");
        }
    }

    public void save(DifficultyEnum level, int[][] board) {
        Path file = root.resolve(level.name().toLowerCase()).resolve("game.csv");
        try {
            CsvReader.writeBoard(file.toString(), board);
        } catch (IOException ignored) {
        }
    }

    // حفظ current/incomplete
    public void saveIncomplete(int[][] board) {
        Path file = root.resolve("incomplete").resolve("game.csv");
        try {
            CsvReader.writeBoard(file.toString(), board);
        } catch (IOException ignored) {
        }
    }

    public void log(String entry) throws IOException {
        Path log = root.resolve("incomplete").resolve("actions.log");
        Files.writeString(log, entry + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
