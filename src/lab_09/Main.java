package lab_09;

import java.io.File;
import java.nio.file.Path;
import javax.swing.*;

public class Main {

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
                Main mainInstance = new Main();

                Viewable controller = new SudokuController();
                ControllerFacade facade = new ControllerFacade(controller);
                GameStorage storage = new GameStorage();

                boolean[] cat = facade.getCatalog();
                System.out.println(String.valueOf(cat[0]) + String.valueOf(cat[1]));
                int[][] board = null;

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

                if (!cat[0] && !cat[1]) {
                    String path = mainInstance.chooseSourcePath();
                    storage.setSOLVED_PATH(Path.of(path));
                    int[][] solverBoard = storage.loadBoardFromPath(path);
                    controller.driveGames(new Game(solverBoard));
                }

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
