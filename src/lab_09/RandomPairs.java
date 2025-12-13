package lab_09;

import java.util.*;

public class RandomPairs {

    private static final int MAX_COORD = 80;
    private final Random random;

    public RandomPairs(long seed) {
        this.random = new Random(seed);
    }

    public List<int[]> generateDistinctPairs(int n) {
        Set<Integer> used = new HashSet<>();
        List<int[]> result = new ArrayList<>();

        while (result.size() < n) {
            int x = random.nextInt(MAX_COORD + 1);
            int y = random.nextInt(MAX_COORD + 1);
            int key = x * 100 + y;

            if (used.add(key)) {
                result.add(new int[]{x, y});
            }
        }
        return result;
    }
}
