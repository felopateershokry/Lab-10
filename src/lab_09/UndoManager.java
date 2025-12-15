package lab_09;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Handles Undo functionality exactly as required in Lab 10. Uses the log file
 * stored in games/incomplete/actions.log
 */
public class UndoManager {

    private final Path logFile;

    public UndoManager(String rootFolder) {
        this.logFile = Paths.get(rootFolder, "incomplete", "actions.log");
    }

    /**
     * Performs undo: - removes last log entry - applies inverse operation on
     * the board
     */
    public boolean undo(int[][] board) throws IOException {
        if (!Files.exists(logFile)) {
            return false;
        }

        List<String> lines = Files.readAllLines(logFile);
        if (lines.isEmpty()) {
            return false;
        }

        String last = lines.remove(lines.size() - 1).trim();
        String[] parts = last.split(",");

        if (parts.length != 4) {
            return false;
        }

        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int prev = Integer.parseInt(parts[3]);

        board[x][y] = prev;

        Files.write(
                logFile,
                lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
        return true;
    }

    /**
     * Clears the undo log (used when a puzzle is solved correctly)
     */
    public void clear() throws IOException {
        Files.deleteIfExists(logFile);
    }
}
