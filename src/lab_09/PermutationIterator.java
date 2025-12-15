package lab_09;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator over all permutations of length 5, where each value is in range
 * [1..9].
 *
 * Total combinations = 9^5 ≈ 59049 Generated on-the-fly (no memory overhead).
 */
public class PermutationIterator implements Iterator<int[]> {

    private final int[] current = {1, 1, 1, 1, 1};
    private boolean hasNext = true;

    @Override
    public boolean hasNext() {
        return hasNext;
    }

    @Override
    public int[] next() {
        if (!hasNext) {
            throw new NoSuchElementException();
        }

        int[] result = current.clone();
        increment();
        return result;
    }

    private void increment() {
        for (int i = current.length - 1; i >= 0; i--) {
            if (current[i] < 9) {
                current[i]++;
                return;
            } else {
                current[i] = 1;
            }
        }
        hasNext = false;
    }
}
