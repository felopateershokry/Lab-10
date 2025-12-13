package lab_09;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class PermutationIterator implements Iterator<int[]> {

    private final int[] digits = {1, 2, 3, 4, 5, 6, 7, 8, 9};
    private final int[] idx = new int[5];
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

        int[] out = new int[5];
        for (int i = 0; i < 5; i++) {
            out[i] = digits[idx[i]];
        }

        increment();
        return out;
    }

    private void increment() {
        for (int i = 4; i >= 0; i--) {
            if (idx[i] < 8) {
                idx[i]++;
                return;
            }
            idx[i] = 0;
        }
        hasNext = false;
    }
}
