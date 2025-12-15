package lab_09;

/**
 * Application entry point. Wires GUI with ControllerFacade (MVC + Facade as
 * required).
 */
public class Main {

    public static void main(String[] args) {

        // Controller-side implementation
        Viewable controller = new SudokuController();

        // Facade to adapt controller to GUI
        Controllable facade = new ControllerFacade(controller);

        // Launch GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            new SudokuGUI(facade);
        });
    }
}
