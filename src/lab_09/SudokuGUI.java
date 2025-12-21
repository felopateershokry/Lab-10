package lab_09;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SudokuGUI extends JFrame {

    private final JTextField[][] cells = new JTextField[9][9];
    private final boolean[][] fixed = new boolean[9][9];
    private final int[][] board = new int[9][9];

    private JTextField selectedCell = null;
    private int selR = -1, selC = -1;

    private final JButton newGameBtn = new JButton("New Game");
    private final JButton verifyBtn = new JButton("Verify");
    private final JButton solveBtn = new JButton("Solve");
    private final JButton undoBtn = new JButton("Undo");

    private final Controllable controller;
    private final GameStorage storage;

    // ===== COLORS =====
    private static final Color BG_FIXED = new Color(235, 235, 235);
    private static final Color BG_EMPTY = Color.WHITE;
    private static final Color BG_SELECTED = new Color(180, 205, 255);
    private static final Color BG_HIGHLIGHT = new Color(225, 235, 255);
    private static final Color BG_ERROR = new Color(255, 200, 200);

    public SudokuGUI(Controllable ctrl, int[][] initialBoard) throws Exception {

        this.controller = ctrl;
        this.storage = new GameStorage();

        setTitle("Sudoku");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(760, 640);
        setLayout(new BorderLayout(12, 12));

        // ================= GRID =================
        JPanel grid = new JPanel(new GridLayout(9, 9));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        Font cellFont = new Font("Segoe UI", Font.BOLD, 18);

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {

                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(cellFont);
                cell.setEditable(false);
                cell.setFocusable(false);

                int top = (i % 3 == 0) ? 2 : 1;
                int left = (j % 3 == 0) ? 2 : 1;
                int bottom = (i == 8) ? 2 : 1;
                int right = (j == 8) ? 2 : 1;

                cell.setBorder(BorderFactory.createMatteBorder(
                        top, left, bottom, right, new Color(140, 140, 140)));

                int r = i, c = j;
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (fixed[r][c]) {
                            return;
                        }
                        selectCell(r, c);
                    }
                });

                cells[i][j] = cell;
                grid.add(cell);
            }
        }

        // ================= NUMBER PAD =================
        JPanel padContainer = new JPanel(new BorderLayout());
        padContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel padTitle = new JLabel("Numbers", SwingConstants.CENTER);
        padTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        padTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JPanel pad = new JPanel(new GridLayout(3, 3, 10, 10));
        pad.setOpaque(false);
        Font padFont = new Font("Segoe UI", Font.BOLD, 20);

        for (int n = 1; n <= 9; n++) {
            int val = n;
            JButton btn = new JButton(String.valueOf(n));
            btn.setFont(padFont);
            btn.setFocusPainted(false);
            btn.setPreferredSize(new Dimension(55, 55));
            btn.setBackground(new Color(230, 240, 255));
            btn.setBorder(BorderFactory.createLineBorder(new Color(150, 170, 200)));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            btn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    btn.setBackground(new Color(200, 220, 255));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    btn.setBackground(new Color(230, 240, 255));
                }
            });

            btn.addActionListener(e -> applyNumber(val));
            pad.add(btn);
        }

        JPanel padWrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        padWrap.setOpaque(false);
        padWrap.add(pad);

        padContainer.add(padTitle, BorderLayout.NORTH);
        padContainer.add(padWrap, BorderLayout.CENTER);

        // ================= BOTTOM BAR =================
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 6));
        bottom.add(newGameBtn);
        bottom.add(verifyBtn);
        bottom.add(solveBtn);
        bottom.add(undoBtn);

        newGameBtn.addActionListener(e -> newGame());
        verifyBtn.addActionListener(e -> verify());
        solveBtn.addActionListener(e -> solve());
        undoBtn.addActionListener(e -> undo());

        add(grid, BorderLayout.CENTER);
        add(padContainer, BorderLayout.EAST);
        add(bottom, BorderLayout.SOUTH);

        loadBoard(initialBoard);
        updateSolveButton();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // ================= LOGIC =================
    private void selectCell(int r, int c) {
        clearHighlights();
        selR = r;
        selC = c;
        selectedCell = cells[r][c];

        for (int i = 0; i < 9; i++) {
            if (!fixed[r][i]) {
                cells[r][i].setBackground(BG_HIGHLIGHT);
            }
            if (!fixed[i][c]) {
                cells[i][c].setBackground(BG_HIGHLIGHT);
            }
        }
        selectedCell.setBackground(BG_SELECTED);
    }

    private void clearHighlights() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j].setBackground(fixed[i][j] ? BG_FIXED : BG_EMPTY);
            }
        }
    }

    private void applyNumber(int value) {
        if (selectedCell == null) {
            return;
        }

        int prev = board[selR][selC];
        if (prev == value) {
            return;
        }

        try {
            controller.logUserAction(new UserAction(selR, selC, value, prev));
            board[selR][selC] = value;
            selectedCell.setText(String.valueOf(value));

            storage.saveCurrentGame(board);

            clearHighlights();
            selectedCell = null;
            selR = selC = -1;

            updateSolveButton();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error writing value: " + e.getMessage());
        }
    }

    private void loadBoard(int[][] b) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                board[i][j] = b[i][j];
                fixed[i][j] = (b[i][j] != 0);

                cells[i][j].setText(b[i][j] == 0 ? "" : String.valueOf(b[i][j]));
                cells[i][j].setBackground(fixed[i][j] ? BG_FIXED : BG_EMPTY);
            }
        }
        selectedCell = null;
        selR = selC = -1;
    }

    private void newGame() {
        try {
            Object[] opts = {"Easy", "Medium", "Hard"};
            int d = JOptionPane.showOptionDialog(
                    this,
                    "Choose difficulty",
                    "New Game",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opts,
                    opts[0]
            );
            if (d == JOptionPane.CLOSED_OPTION) {
                return;
            }

            char level = (d == 1) ? 'M' : (d == 2) ? 'H' : 'E';

            UndoManager.clear();
            controller.driveGames(storage.getSOLVED_PATH().toString());
            int[][] newBoard = controller.getGame(level);
            storage.saveCurrentGame(newBoard);

            loadBoard(newBoard);
            clearHighlights();
            updateSolveButton();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "New Game failed: " + e.getMessage());
        }
    }

    private void verify() {
        try {
            boolean[][] ok = controller.verifyGame(board);

            boolean hasError = false;
            boolean incomplete = false;

            clearHighlights();

            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (!ok[i][j]) {
                        cells[i][j].setBackground(BG_ERROR);
                        hasError = true;
                    }
                    if (board[i][j] == 0) {
                        incomplete = true;
                    }
                }
            }

            if (!hasError && !incomplete) {
                JOptionPane.showMessageDialog(this, "Congratulations! Puzzle solved.");
                try {
                    GameStorage.deleteCurrent();
                } catch (Exception ignored) {
                }
            } else if (!hasError) {
                JOptionPane.showMessageDialog(this, "Board is valid but incomplete.");
            } else {
                JOptionPane.showMessageDialog(this, "Board contains errors.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Verify failed: " + e.getMessage());
        }
    }

    private void solve() {
        try {
            int[][] solved = controller.solveGame(board);
            loadBoard(solved);
            storage.saveCurrentGame(board);
            JOptionPane.showMessageDialog(this, "Solved!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Solve works only when exactly 5 cells are empty.");
        }
    }

    private void undo() {
        try {
            UndoManager.undo(board);
            storage.saveCurrentGame(board);
            loadBoard(board);
            updateSolveButton();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Undo failed: " + e.getMessage());
        }
    }

    private void updateSolveButton() {
        int empty = 0;
        for (int[] r : board) {
            for (int v : r) {
                if (v == 0) {
                    empty++;
                }
            }
        }
        solveBtn.setEnabled(empty == 5);
    }
}
