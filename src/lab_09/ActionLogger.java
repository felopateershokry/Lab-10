package lab_09;

import java.io.*;
import java.util.*;

public class ActionLogger {

    private final File file = new File("games/current/actions.log");

    public void log(int r, int c, int newV, int oldV) throws IOException {
        file.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(file, true)) {
            fw.write(r + "," + c + "," + newV + "," + oldV + "\n");
        }
    }

    public int[] undo() throws IOException {
        if (!file.exists()) {
            return null;
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String l;
            while ((l = br.readLine()) != null) {
                lines.add(l);
            }
        }

        if (lines.isEmpty()) {
            return null;
        }

        String last = lines.remove(lines.size() - 1);
        try (FileWriter fw = new FileWriter(file)) {
            for (String s : lines) {
                fw.write(s + "\n");
            }
        }

        String[] p = last.split(",");
        return new int[]{
            Integer.parseInt(p[0]),
            Integer.parseInt(p[1]),
            Integer.parseInt(p[3])
        };
    }

    public void clear() {
        if (file.exists()) {
            file.delete();
        }
    }
}
