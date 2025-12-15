package lab_09;

import java.io.*;
import java.nio.file.*;

/**
 * Handles immediate logging to the undo log file. Lab 10 requirement: - Log
 * entries must be written immediately. - Each entry is: (x, y, val, prev) ->
 * stored as "x,y,val,prev" - Stored in the same folder as the current played
 * game: "games/incomplete"
 */
public class ActionLogger {

    private final Path logFile;

    public ActionLogger(String rootFolder) {
        this.logFile = Paths.get(rootFolder, "incomplete", "actions.log");
    }

    public void append(int x, int y, int val, int prev) throws IOException {
        Files.createDirectories(logFile.getParent());

        try (FileOutputStream fos = new FileOutputStream(logFile.toFile(), true); OutputStreamWriter osw = new OutputStreamWriter(fos); BufferedWriter bw = new BufferedWriter(osw)) {

            bw.write(x + "," + y + "," + val + "," + prev);
            bw.newLine();
            bw.flush();
            fos.getFD().sync(); // write immediately
        }
    }

    public Path getLogFile() {
        return logFile;
    }
}
