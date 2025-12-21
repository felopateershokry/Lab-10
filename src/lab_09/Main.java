package lab_09;

import java.io.File;
import java.nio.file.Path;
import javax.swing.*;

public class Main {

    // Make this method non-static, and use it with an instance in the main method
    private String chooseSourcePath() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            return selectedFile.getAbsolutePath();
        }
        return null;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                // Creating an instance of Main to call the non-static method chooseSourcePath
                Main mainInstance = new Main();

                // Create other necessary instances
                Viewable controller = new SudokuController();
                ControllerFacade facade = new ControllerFacade(controller);
                GameStorage storage = new GameStorage();

                boolean[] cat = facade.getCatalog();
                System.out.println(String.valueOf(cat[0]) + String.valueOf(cat[1]));
                int[][] board = null;

                // ===== Continue unfinished game =====
                if (cat[0]) {
                    int c = JOptionPane.showConfirmDialog(
                            null,
                            "Continue unfinished game?",
                            "Sudoku",
                            JOptionPane.YES_NO_OPTION);

                    if (c == JOptionPane.YES_OPTION) {
                        board = storage.loadCurrentGame();
                    } else {
                        GameStorage.deleteCurrent();
                    }
                }

                // ===== Generate games ONCE =====
                if (!cat[0] && !cat[1]) {
                    // Use the instance to call the non-static chooseSourcePath method
                    String path = mainInstance.chooseSourcePath();
                    storage.setSOLVED_PATH(Path.of(path));
                    int[][] solverBoard = storage.loadBoardFromPath(path);
                    controller.driveGames(new Game(solverBoard));
                }

                // ===== Choose difficulty =====
                if (board == null) {
                    Object[] opts = {"Easy", "Medium", "Hard"};
                    int d = JOptionPane.showOptionDialog(
                            null,
                            "Choose difficulty",
                            "Sudoku",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opts,
                            opts[0]);

                    char level = (d == 1) ? 'M' : (d == 2) ? 'H' : 'E';
                    board = facade.getGame(level);
                    storage.saveCurrentGame(board);
                }

                new SudokuGUI(facade, board);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
