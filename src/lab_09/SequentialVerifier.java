package lab_09;

import java.util.*;

public class SequentialVerifier {

    public String verify(int[][] board) {

        Set<String> invalid = findInvalidCells(board);

        if (!invalid.isEmpty()) {
            StringBuilder sb = new StringBuilder("invalid");
            for (String s : invalid) {
                sb.append(" ").append(s);
            }
            return sb.toString();
        }

        for (int[] r : board) {
            for (int v : r) {
                if (v == 0) {
                    return "incomplete";
                }
            }
        }

        return "valid";
    }

    public static boolean isValid(int[][] board) {
        SequentialVerifier v = new SequentialVerifier();
        return v.verify(board).equals("valid");
    }

    private static Set<String> findInvalidCells(int[][] b) {
        Set<String> bad = new TreeSet<>();

        // rows
        for (int r = 0; r < 9; r++) {
            Map<Integer, List<Integer>> map = new HashMap<>();
            for (int c = 0; c < 9; c++) {
                int v = b[r][c];
                if (v == 0) {
                    continue;
                }
                map.computeIfAbsent(v, k -> new ArrayList<>()).add(c);
            }
            for (var e : map.entrySet()) {
                if (e.getValue().size() > 1) {
                    for (int c : e.getValue()) {
                        bad.add(r + "," + c);
                    }
                }
            }
        }

        // columns
        for (int c = 0; c < 9; c++) {
            Map<Integer, List<Integer>> map = new HashMap<>();
            for (int r = 0; r < 9; r++) {
                int v = b[r][c];
                if (v == 0) {
                    continue;
                }
                map.computeIfAbsent(v, k -> new ArrayList<>()).add(r);
            }
            for (var e : map.entrySet()) {
                if (e.getValue().size() > 1) {
                    for (int r : e.getValue()) {
                        bad.add(r + "," + c);
                    }
                }
            }
        }

        // boxes
        for (int br = 0; br < 9; br += 3) {
            for (int bc = 0; bc < 9; bc += 3) {
                Map<Integer, List<int[]>> map = new HashMap<>();
                for (int r = br; r < br + 3; r++) {
                    for (int c = bc; c < bc + 3; c++) {
                        int v = b[r][c];
                        if (v == 0) {
                            continue;
                        }
                        map.computeIfAbsent(v, k -> new ArrayList<>())
                                .add(new int[]{r, c});
                    }
                }
                for (var e : map.entrySet()) {
                    if (e.getValue().size() > 1) {
                        for (int[] rc : e.getValue()) {
                            bad.add(rc[0] + "," + rc[1]);
                        }
                    }
                }
            }
        }

        return bad;
    }
}
