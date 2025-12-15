package lab_09;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * Viewer-side GUI. Interacts ONLY with Controllable (ControllerFacade). Startup
 * flow and Solve-button rule are EXACTLY as Lab 10.
 */
public class SudokuGUI extends JFrame {

    private final Controllable controller;
    private final JTextField[][] cells = new JTextField[9][9];

    private final JButton verifyBtn = new JButton("Verify");
    private final JButton solveBtn = new JButton("Solve");
    private final JButton undoBtn = new JButton("Undo");

    public SudokuGUI(Controllable controller) {
        this.controller = controller;
        initUI();
        startupFlow();
    }

    private void initUI() {
        setTitle("Sudoku - Lab 10");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel grid = new JPanel(new GridLayout(9, 9));
        Font font = new Font("Arial", Font.BOLD, 18);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(font);
                cells[r][c] = tf;
                grid.add(tf);
            }
        }

        JPanel controls = new JPanel();
        controls.add(verifyBtn);
        controls.add(solveBtn);
        controls.add(undoBtn);

        solveBtn.setEnabled(false); // enabled ONLY when 5 empty cells

        verifyBtn.addActionListener(e -> verify());
        solveBtn.addActionListener(e -> solve());
        undoBtn.addActionListener(e -> undo());

        add(grid, BorderLayout.CENTER);
        add(controls, BorderLayout.SOUTH);

        setSize(450, 500);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ================= Startup Flow (PDF EXACT) =================
    private void startupFlow() {
        boolean[] catalog = controller.getCatalog();
        boolean hasIncomplete = catalog[0];
        boolean hasAllLevels = catalog[1];

        try {
            if (hasIncomplete) {
                loadGame(controller.getGame('I'));
                return;
            }

            if (hasAllLevels) {
                char d = askDifficulty();
                loadGame(controller.getGame(d));
                return;
            }

            askSolvedAndGenerate();
            char d = askDifficulty();
            loadGame(controller.getGame(d));

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void askSolvedAndGenerate() throws SolutionInvalidException {
        JFileChooser fc = new JFileChooser();
        int res = fc.showOpenDialog(this);
        if (res != JFileChooser.APPROVE_OPTION) {
            throw new SolutionInvalidException("Solved Sudoku file is required");
        }
        File f = fc.getSelectedFile();
        controller.driveGames(f.getAbsolutePath());
    }

    private char askDifficulty() {
        Object[] options = {"Easy", "Medium", "Hard"};
        int res = JOptionPane.showOptionDialog(
                this,
                "Choose difficulty",
                "Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        return switch (res) {
            case 0 ->
                'E';
            case 1 ->
                'M';
            default ->
                'H';
        };
    }

    // ================= Game Actions =================
    private void loadGame(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c].setText(board[r][c] == 0 ? "" : String.valueOf(board[r][c]));
                cells[r][c].setBackground(Color.WHITE);
            }
        }
        updateSolveButton();
    }

    private void verify() {
        boolean[][] ok = controller.verifyGame(readBoard());
        boolean full = true;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                cells[r][c].setBackground(ok[r][c] ? Color.WHITE : Color.PINK);
                if (cells[r][c].getText().isEmpty()) {
                    full = false;
                }
            }
        }

        if (full) {
            JOptionPane.showMessageDialog(this, "Board full – verification done.");
        }
    }

    private void solve() {
        try {
            int[][] sol = controller.solveGame(readBoard());
            for (int[] s : sol) {
                cells[s[0]][s[1]].setText(String.valueOf(s[2]));
            }
            updateSolveButton();
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void undo() {
        // Undo logic handled in controller via storage/log
        verify();
    }

    private int[][] readBoard() {
        int[][] b = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                String t = cells[r][c].getText().trim();
                b[r][c] = t.isEmpty() ? 0 : Integer.parseInt(t);
            }
        }
        updateSolveButton();
        return b;
    }

    private void updateSolveButton() {
        int empty = 0;
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (cells[r][c].getText().isEmpty()) {
                    empty++;
                }
            }
        }

        solveBtn.setEnabled(empty == 5); // Lab 10 rule
    }

    private void showError(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
