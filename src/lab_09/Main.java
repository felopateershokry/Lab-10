package lab_09;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                // Start the application using MVC:
                // GUI -> Controller -> Driver/Storage
                Controllable controller = new SudokuController();
                new SudokuGUI(controller);

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
