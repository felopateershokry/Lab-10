package lab_09;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI extends JFrame {

    private final JTextField[][] cells = new JTextField[9][9];
    private final boolean[][] fixed = new boolean[9][9];
    private JTextField selectedCell = null;

    private final JButton verifyBtn = new JButton("Verify");
    private final JButton checkBtn = new JButton("Check Move");
    private final JButton solveBtn = new JButton("Solve");
    private final JButton undoBtn = new JButton("Undo");
    private final JButton newBtn = new JButton("New Game");

    private final Controllable controller;

    public SudokuGUI(Controllable controller) throws Exception {
        this.controller = controller;

        setTitle("Sudoku");
        setSize(800, 560);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadInitialGame();

        setVisible(true);
    }

    // ================= STARTUP =================
    private void loadInitialGame() throws Exception {
        Catalog cat = controller.getCatalog();

        // 1️⃣ If unfinished game exists → ask user
        if (cat.hasCurrent) {

            int choice = JOptionPane.showOptionDialog(
                    this,
                    "You have an unfinished game.\nWhat do you want to do?",
                    "Resume Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new Object[]{"Resume", "New Game"},
                    "Resume"
            );

            // Resume old game
            if (choice == 0) {
                int[][] cur = ((SudokuController) controller).loadCurrentGame();
                loadBoard(cur);
                return;
            }

            // New game → clear old one
            controller.clearCurrentGame();
        }

        // 2️⃣ If solved board exists → ALWAYS ask difficulty
        if (cat.hasSolved) {
            char diff = askDifficulty();
            loadBoard(controller.getGame(diff));
            return;
        }

        // 3️⃣ No solved board → upload one
        uploadSolvedBoard();
    }

    private char askDifficulty() {
        Object[] options = {"EASY", "MEDIUM", "HARD"};
        int c = JOptionPane.showOptionDialog(
                this,
                "Select difficulty",
                "Difficulty",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
        return options[c].toString().charAt(0);
    }

    private void uploadSolvedBoard() throws Exception {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            System.exit(0);
        }

        int[][] solved = CsvReader.readBoard(
                fc.getSelectedFile().getAbsolutePath()
        );

        controller.driveGames(solved);

        char diff = askDifficulty();
        loadBoard(controller.getGame(diff));
    }

    // ================= UI =================
    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());

        // -------- Grid --------
        JPanel grid = new JPanel(new GridLayout(9, 9));
        Font font = new Font("Arial", Font.BOLD, 18);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {

                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(font);
                tf.setEditable(false);

                int top = (r % 3 == 0) ? 2 : 1;
                int left = (c % 3 == 0) ? 2 : 1;
                int bottom = (r == 8) ? 2 : 1;
                int right = (c == 8) ? 2 : 1;

                tf.setBorder(BorderFactory.createMatteBorder(
                        top, left, bottom, right, Color.BLACK
                ));

                tf.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectCell(tf);
                    }
                });

                cells[r][c] = tf;
                grid.add(tf);
            }
        }

        // -------- Number Pad --------
        JPanel numbers = new JPanel(new GridLayout(6, 2, 6, 6));
        numbers.setBorder(BorderFactory.createTitledBorder("Numbers"));

        for (int i = 1; i <= 9; i++) {
            int num = i;
            JButton b = new JButton(String.valueOf(i));
            b.addActionListener(e -> placeNumber(num));
            numbers.add(b);
        }

        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> clearCell());
        numbers.add(clearBtn);
        numbers.add(new JLabel());

        // -------- Bottom Buttons --------
        JPanel bottom = new JPanel();
        solveBtn.setEnabled(false);

        bottom.add(verifyBtn);
        bottom.add(checkBtn);
        bottom.add(solveBtn);
        bottom.add(undoBtn);
        bottom.add(newBtn);

        verifyBtn.addActionListener(e -> verifyBoard());
        checkBtn.addActionListener(e -> checkMove());
        solveBtn.addActionListener(e -> solveBoard());
        undoBtn.addActionListener(e -> undoMove());
        newBtn.addActionListener(e -> startNewGame());

        main.add(grid, BorderLayout.CENTER);
        main.add(numbers, BorderLayout.EAST);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);
    }

    // ================= BOARD =================
    private void loadBoard(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (board[r][c] == 0) {
                    cells[r][c].setText("");
                    fixed[r][c] = false;
                } else {
                    cells[r][c].setText(String.valueOf(board[r][c]));
                    fixed[r][c] = true;
                }
                cells[r][c].setBackground(Color.WHITE);
            }
        }
        checkSolveAvailability();
    }

    private int[][] readBoard() {
        int[][] b = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                b[r][c] = cells[r][c].getText().isEmpty()
                        ? 0
                        : Integer.parseInt(cells[r][c].getText());
            }
        }
        return b;
    }

    // ================= ACTIONS =================
    private void placeNumber(int num) {
        if (selectedCell == null) {
            return;
        }

        Point p = findCell(selectedCell);
        if (fixed[p.x][p.y]) {
            return;
        }

        int prev = selectedCell.getText().isEmpty()
                ? 0
                : Integer.parseInt(selectedCell.getText());

        selectedCell.setText(String.valueOf(num));

        try {
            controller.logUserAction(readBoard(), p.x, p.y, num, prev);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        checkSolveAvailability();
    }

    private void clearCell() {
        if (selectedCell == null) {
            return;
        }

        Point p = findCell(selectedCell);
        if (fixed[p.x][p.y]) {
            return;
        }

        int prev = selectedCell.getText().isEmpty()
                ? 0
                : Integer.parseInt(selectedCell.getText());

        selectedCell.setText("");

        try {
            controller.logUserAction(readBoard(), p.x, p.y, 0, prev);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }

        checkSolveAvailability();
    }

    private void undoMove() {
        try {
            int[][] board = readBoard();
            boolean ok = controller.undoLastAction(board);

            if (!ok) {
                JOptionPane.showMessageDialog(this, "Nothing to undo");
                return;
            }
            loadBoard(board);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void verifyBoard() {
        String res = controller.verifyGame(readBoard());
        JOptionPane.showMessageDialog(this, res);

        if ("VALID".equals(res)) {
            startNewGame();
        }
    }

    private void checkMove() {
        if (selectedCell == null) {
            JOptionPane.showMessageDialog(this, "Select a cell first.");
            return;
        }

        Point p = findCell(selectedCell);
        if (fixed[p.x][p.y]) {
            JOptionPane.showMessageDialog(this, "Fixed cell.");
            return;
        }

        if (selectedCell.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cell is empty.");
            return;
        }

        int val = Integer.parseInt(selectedCell.getText());

        try {
            boolean ok = controller.isCorrectMove(p.x, p.y, val);
            selectedCell.setBackground(ok ? Color.GREEN : Color.PINK);
            JOptionPane.showMessageDialog(this, ok ? "Correct ✔" : "Wrong ✖");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void solveBoard() {
        try {
            int[][] board = readBoard();
            controller.solveGame(board);
            loadBoard(board);
            JOptionPane.showMessageDialog(this, "Solved successfully ✔");
            startNewGame();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void startNewGame() {
        try {
            controller.clearCurrentGame();
        } catch (Exception ignored) {
        }

        dispose();
        try {
            new SudokuGUI(new SudokuController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HELPERS =================
    private void selectCell(JTextField tf) {
        for (var row : cells) {
            for (var c : row) {
                c.setBackground(Color.WHITE);
            }
        }
        selectedCell = tf;
        tf.setBackground(Color.CYAN);
    }

    private Point findCell(JTextField tf) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (cells[r][c] == tf) {
                    return new Point(r, c);
                }
            }
        }
        return null;
    }

    private void checkSolveAvailability() {
        int empty = 0;
        for (var row : cells) {
            for (var c : row) {
                if (c.getText().isEmpty()) {
                    empty++;
                }
            }
        }
        solveBtn.setEnabled(empty > 0 && empty <= 5);
    }
}
