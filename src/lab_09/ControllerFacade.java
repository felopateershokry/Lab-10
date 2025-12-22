package lab_09;

import java.io.IOException;
import java.util.Arrays;

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
        DifficultyEnum d = switch (Character.toUpperCase(level)) {
            case 'E' ->
                DifficultyEnum.EASY;
            case 'M' ->
                DifficultyEnum.MEDIUM;
            case 'H' ->
                DifficultyEnum.HARD;
            default ->
                throw new NotFoundException("Invalid difficulty");
        };
        return controller.getGame(d).board;
    }

    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException {
        try {
            int[][] solved = CsvReader.read(sourcePath);
            controller.driveGames(new Game(solved));
        } catch (Exception e) {
            throw new SolutionInvalidException(e.getMessage());
        }
    }

    @Override
    public boolean[][] verifyGame(int[][] board) {
        boolean[][] ok = new boolean[9][9];
        for (boolean[] r : ok) {
            Arrays.fill(r, true);
        }

        String res = controller.verifyGame(new Game(board));
        if (res.startsWith("invalid")) {
            String[] coords = res.substring(7).trim().split(" ");
            for (String c : coords) {
                if (c.isEmpty()) {
                    continue;
                }
                String[] xy = c.split(",");
                ok[Integer.parseInt(xy[0])][Integer.parseInt(xy[1])] = false;
            }
        }
        return ok;
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGameException {
        int[] enc = controller.solveGame(new Game(game));
        int[][] solved = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(game[i], 0, solved[i], 0, 9);
        }

        for (int i = 0; i < enc.length; i += 3) {
            solved[enc[i]][enc[i + 1]] = enc[i + 2];
        }
        return solved;
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException {
        ActionLogger.log(userAction);
    }
}
