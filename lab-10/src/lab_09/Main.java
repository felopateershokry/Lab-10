package lab_09;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                Viewable controller = new SudokuController();
                ControllerFacade facade = new ControllerFacade(controller);
                GameStorage storage = new GameStorage();

                boolean[] cat = facade.getCatalog();
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
                if (board == null && !cat[1]) {

                    String solvedPath = storage.loadSolvedPath();

                    if (solvedPath == null) {
                        JFileChooser chooser = new JFileChooser();
                        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                            return;
                        }

                        solvedPath = chooser.getSelectedFile().getAbsolutePath();
                        storage.saveSolvedPath(solvedPath);
                    }

                    facade.driveGames(solvedPath);
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
