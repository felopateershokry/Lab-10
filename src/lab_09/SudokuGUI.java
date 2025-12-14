package lab_09;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SudokuGUI extends JFrame {

    private final JTextField[][] cells = new JTextField[9][9];
    private final boolean[][] fixed = new boolean[9][9];
    private JTextField selectedCell = null;

    private final JButton verifyBtn = new JButton("Verify");
    private final JButton solveBtn = new JButton("Solve");
    private final JButton undoBtn = new JButton("Undo");
    private final JButton newBtn = new JButton("New Game");

    private final Controllable controller;

    // ================= COLORS =================
    private static final Color BG_FIXED = Color.WHITE;
    private static final Color BG_EMPTY = new Color(220, 220, 220);
    private static final Color BG_SELECTED = new Color(180, 205, 255);
    private static final Color BG_CORRECT = new Color(170, 235, 170); // GREEN
    private static final Color BG_WRONG = new Color(245, 170, 170);   // RED
    private static final Color BG_HIGHLIGHT = new Color(200, 200, 200);
    private static final Color BG_SAME_NUM = new Color(255, 255, 180);

    public SudokuGUI(Controllable controller) throws Exception {
        this.controller = controller;

        setTitle("Sudoku");
        setSize(820, 580);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initUI();
        loadInitialGame();

        setVisible(true);
    }

    // ================= STARTUP =================
    private void loadInitialGame() throws Exception {
        char diff = askDifficulty();
        loadBoard(controller.getGame(diff));
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

    // ================= UI =================
    private void initUI() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(Color.WHITE);

        // ---------- GRID ----------
        JPanel grid = new JPanel(new GridLayout(9, 9));
        grid.setBackground(Color.BLACK);

        Font cellFont = new Font("Segoe UI", Font.BOLD, 18);

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {

                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(cellFont);
                tf.setEditable(false);
                tf.setForeground(Color.BLACK);

                int top = (r % 3 == 0) ? 2 : 1;
                int left = (c % 3 == 0) ? 2 : 1;
                int bottom = (r == 8) ? 2 : 1;
                int right = (c == 8) ? 2 : 1;

                tf.setBorder(BorderFactory.createMatteBorder(
                        top, left, bottom, right, Color.BLACK));

                tf.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        selectCell(tf);
                    }
                });

                cells[r][c] = tf;
                grid.add(tf);
            }
        }

        // ---------- NUMBER PAD ----------
        JPanel numbersWrap = new JPanel(new BorderLayout());
        numbersWrap.setBackground(Color.WHITE);
        numbersWrap.setPreferredSize(new Dimension(300, 450));
        numbersWrap.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JLabel title = new JLabel("Numbers", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        numbersWrap.add(title, BorderLayout.NORTH);

        JPanel numbers = new JPanel(new GridLayout(4, 3, 10, 10));
        numbers.setBackground(Color.WHITE);

        Font btnFont = new Font("Segoe UI", Font.BOLD, 20);

        for (int i = 1; i <= 9; i++) {
            int num = i;
            JButton b = createNumberButton(String.valueOf(i), btnFont);
            b.addActionListener(e -> placeNumber(num));
            numbers.add(b);
        }

        JButton clearBtn = createNumberButton(
                "Clear",
                new Font("Segoe UI", Font.BOLD, 18)
        );
        clearBtn.addActionListener(e -> clearCell());

        numbers.add(new JLabel());
        numbers.add(clearBtn);
        numbers.add(new JLabel());

        numbersWrap.add(numbers, BorderLayout.CENTER);

        // ---------- BOTTOM ----------
        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);

        solveBtn.setEnabled(false);

        bottom.add(verifyBtn);
        bottom.add(solveBtn);
        bottom.add(undoBtn);
        bottom.add(newBtn);

        verifyBtn.addActionListener(e -> verifyBoard());
        solveBtn.addActionListener(e -> solveBoard());
        undoBtn.addActionListener(e -> undoMove());
        newBtn.addActionListener(e -> startNewGame());

        main.add(grid, BorderLayout.CENTER);
        main.add(numbersWrap, BorderLayout.EAST);
        main.add(bottom, BorderLayout.SOUTH);

        add(main);
    }

    private JButton createNumberButton(String text, Font font) {
        JButton b = new JButton(text);
        b.setFont(font);
        b.setFocusPainted(false);
        b.setBackground(Color.WHITE);
        b.setForeground(Color.BLACK);
        b.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        b.setPreferredSize(new Dimension(100, 70));

        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                b.setBackground(Color.BLACK);
                b.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent e) {
                b.setBackground(Color.WHITE);
                b.setForeground(Color.BLACK);
            }
        });
        return b;
    }

    // ================= BOARD =================
    private void loadBoard(int[][] board) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {

                if (board[r][c] == 0) {
                    cells[r][c].setText("");
                    cells[r][c].setBackground(BG_EMPTY);
                    fixed[r][c] = false;
                } else {
                    cells[r][c].setText(String.valueOf(board[r][c]));
                    cells[r][c].setBackground(BG_FIXED);
                    fixed[r][c] = true;
                }
            }
        }
        checkSolveAvailability();
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

        selectedCell.setText(String.valueOf(num));

        try {
            controller.logUserAction(readBoard(), p.x, p.y, num, 0);
            boolean correct = controller.isCorrectMove(p.x, p.y, num);

            highlightContext(p.x, p.y);
            selectedCell.setBackground(correct ? BG_CORRECT : BG_WRONG);

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

        selectedCell.setText("");
        selectedCell.setBackground(BG_EMPTY);
        highlightContext(p.x, p.y);
        checkSolveAvailability();
    }

    private void undoMove() {
        try {
            int[][] board = readBoard();
            if (!controller.undoLastAction(board)) {
                JOptionPane.showMessageDialog(this, "Nothing to undo");
                return;
            }
            loadBoard(board);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void verifyBoard() {
        JOptionPane.showMessageDialog(this, controller.verifyGame(readBoard()));
    }

    private void solveBoard() {
        try {
            int[][] board = readBoard();

            // 🔴 تحقق من كل القيم المدخلة يدويًا
            for (int r = 0; r < 9; r++) {
                for (int c = 0; c < 9; c++) {
                    if (!fixed[r][c] && board[r][c] != 0) {
                        if (!controller.isCorrectMove(r, c, board[r][c])) {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "You have incorrect entries.\nFix them before solving.",
                                    "Cannot Solve",
                                    JOptionPane.ERROR_MESSAGE
                            );
                            return;
                        }
                    }
                }
            }

            // ✅ لو وصل هنا يبقى كل المدخلات صح
            controller.solveGame(board);
            loadBoard(board);

            JOptionPane.showMessageDialog(
                    this,
                    "Solved successfully ✔",
                    "Solved",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void startNewGame() {
        dispose();
        try {
            new SudokuGUI(new SudokuController());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= HIGHLIGHT =================
    private void selectCell(JTextField tf) {
        selectedCell = tf;
        Point p = findCell(tf);
        highlightContext(p.x, p.y);
    }

    private void highlightContext(int r, int c) {
        resetHighlights();

        for (int i = 0; i < 9; i++) {
            highlightCell(r, i);
            highlightCell(i, c);
        }

        int br = (r / 3) * 3;
        int bc = (c / 3) * 3;
        for (int i = br; i < br + 3; i++) {
            for (int j = bc; j < bc + 3; j++) {
                highlightCell(i, j);
            }
        }

        String val = cells[r][c].getText();
        if (!val.isEmpty()) {
            highlightSame(val);
        }

        Color current = cells[r][c].getBackground();
        if (!current.equals(BG_CORRECT) && !current.equals(BG_WRONG)) {
            cells[r][c].setBackground(BG_SELECTED);
        }
    }

    private void highlightCell(int r, int c) {
        JTextField cell = cells[r][c];
        Color bg = cell.getBackground();

        if (bg.equals(BG_CORRECT) || bg.equals(BG_WRONG) || bg.equals(BG_SELECTED)) {
            return;
        }
        cell.setBackground(BG_HIGHLIGHT);
    }

    private void highlightSame(String value) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (value.equals(cells[r][c].getText())) {
                    cells[r][c].setBackground(BG_SAME_NUM);
                }
            }
        }
    }

    private void resetHighlights() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                Color bg = cells[r][c].getBackground();
                if (bg.equals(BG_CORRECT) || bg.equals(BG_WRONG)) {
                    continue;
                }
                cells[r][c].setBackground(fixed[r][c] ? BG_FIXED : BG_EMPTY);
            }
        }
    }

    // ================= HELPERS =================
    private int[][] readBoard() {
        int[][] b = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                b[r][c] = cells[r][c].getText().isEmpty()
                        ? 0 : Integer.parseInt(cells[r][c].getText());
            }
        }
        return b;
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
