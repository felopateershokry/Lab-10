package lab_09;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class UndoManager {

    private final Path logFile;

    public UndoManager(String rootFolder) {
        this.logFile = Paths.get(rootFolder, "current", "actions.log");
    }

    public void append(int x, int y, int val, int prev) throws IOException {
        Files.createDirectories(logFile.getParent());

        // immediate write, no buffering kept across calls
        try (FileOutputStream fos = new FileOutputStream(logFile.toFile(), true); OutputStreamWriter osw = new OutputStreamWriter(fos); BufferedWriter bw = new BufferedWriter(osw)) {

            bw.write(x + "," + y + "," + val + "," + prev);
            bw.newLine();
            bw.flush();
            fos.getFD().sync(); // extra safety: ensure write hits disk
        }
    }

    public boolean undo(int[][] game) throws IOException {
        if (!Files.exists(logFile)) {
            return false;
        }

        List<String> lines = Files.readAllLines(logFile);
        if (lines.isEmpty()) {
            return false;
        }

        String last = lines.get(lines.size() - 1).trim();
        String[] parts = last.split(",");
        if (parts.length != 4) {
            return false;
        }

        int x = Integer.parseInt(parts[0].trim());
        int y = Integer.parseInt(parts[1].trim());
        int val = Integer.parseInt(parts[2].trim());
        int prev = Integer.parseInt(parts[3].trim());

        // reverse the change
        game[x][y] = prev;

        // remove last log entry
        lines.remove(lines.size() - 1);
        Files.write(logFile, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        return true;
    }

    public void clearLog() throws IOException {
        Files.deleteIfExists(logFile);
    }
}
