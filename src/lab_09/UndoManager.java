package lab_09;

import java.io.IOException;
import java.util.List;

public class UndoManager {

    public static void undo(int[][] board) throws IOException {
        List<String> lines = ActionLogger.readAll();
        if (lines.isEmpty()) {
            return;
        }

        String last = lines.remove(lines.size() - 1);
        ActionLogger.overwrite(lines);

        last = last.replace("(", "").replace(")", "");
        String[] p = last.split(",");

        int x = Integer.parseInt(p[0].trim());
        int y = Integer.parseInt(p[1].trim());
        int prev = Integer.parseInt(p[3].trim());

        board[x][y] = prev;
    }

    public static void clear() throws IOException {
        ActionLogger.clear();
    }
}
