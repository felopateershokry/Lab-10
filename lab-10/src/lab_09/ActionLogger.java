package lab_09;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ActionLogger {

    private static final Path LOG = Paths.get("storage/current/log.txt");

    public static void log(UserAction a) throws IOException {
        Files.createDirectories(LOG.getParent());
        Files.writeString(LOG, a.toString() + System.lineSeparator(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }

    public static List<String> readAll() throws IOException {
        if (!Files.exists(LOG)) {
            return List.of();
        }
        return Files.readAllLines(LOG);
    }

    public static void overwrite(List<String> lines) throws IOException {
        Files.write(LOG, lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }

    public static void clear() throws IOException {
        Files.writeString(LOG, "",
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
