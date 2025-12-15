package lab_09;

import java.io.IOException;

/**
 * Facade / Adapter between GUI (viewer) and controller logic. REQUIRED by Lab
 * 10 to integrate two incompatible interfaces.
 */
public class ControllerFacade implements Controllable {

    private final Viewable controller;

    public ControllerFacade(Viewable controller) {
        this.controller = controller;
    }

    @Override
    public boolean[] getCatalog() {
        Catalog c = controller.getCatalog();
        return new boolean[]{c.current, c.allModesExist};
    }

    @Override
    public int[][] getGame(char level) throws NotFoundException {

        // ✅ تحميل اللعبة غير المكتملة (incomplete)
        if (Character.toUpperCase(level) == 'I') {
            try {
                return CsvReader.readBoard("games/incomplete/game.csv");
            } catch (Exception e) {
                throw new NotFoundException("Incomplete game not found");
            }
        }

        DifficultyEnum diff;
        switch (Character.toUpperCase(level)) {
            case 'E':
                diff = DifficultyEnum.EASY;
                break;
            case 'M':
                diff = DifficultyEnum.MEDIUM;
                break;
            case 'H':
                diff = DifficultyEnum.HARD;
                break;
            default:
                throw new NotFoundException("Invalid difficulty");
        }

        return controller.getGame(diff).board;
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        try {
            int[][] solved = CsvReader.readBoard(sourcePath);
            controller.driveGames(new Game(solved));
        } catch (IOException e) {
            throw new SolutionInvalidException("Cannot read solved Sudoku file");
        }
    }

    @Override
    public boolean[][] verifyGame(int[][] game) {
        boolean[][] result = new boolean[9][9];
        String status = controller.verifyGame(new Game(game));

        // default: all cells valid
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                result[r][c] = true;
            }
        }

        // mark invalid cells if any
        if (status != null && status.startsWith("invalid")) {
            String[] parts = status.split("\\s+");
            for (int i = 1; i < parts.length; i++) {
                String[] rc = parts[i].split(",");
                if (rc.length == 2) {
                    int r = Integer.parseInt(rc[0]);
                    int c = Integer.parseInt(rc[1]);
                    result[r][c] = false;
                }
            }
        }

        return result;
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {
        int[] solution = controller.solveGame(new Game(game));

        int[][] out = new int[solution.length][3];
        int idx = 0;

        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                if (game[r][c] == 0) {
                    out[idx][0] = r;
                    out[idx][1] = c;
                    out[idx][2] = solution[idx];
                    idx++;
                }
            }
        }
        return out;
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        controller.logUserAction(userAction.toString());
    }
}
