package lab_09;

import java.util.Iterator;

public class PermutationIterator implements Iterator<int[]> {

    private int counter = 0;
    private static final int MAX = (int) Math.pow(9, 5);

    @Override
    public boolean hasNext() {
        return counter < MAX;
    }

    @Override
    public int[] next() {
        int[] perm = new int[5];
        int x = counter++;

        for (int i = 0; i < 5; i++) {
            perm[i] = (x % 9) + 1;
            x /= 9;
        }
        return perm;
    }
}
