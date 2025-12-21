package lab_09;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class ResultCollector {

    private final List<String> lines = new ArrayList<>();

    public void add(String line) {
        lines.add(line);
    }

    public void save(String path) throws IOException {
        Files.write(Paths.get(path), lines,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);
    }
}
