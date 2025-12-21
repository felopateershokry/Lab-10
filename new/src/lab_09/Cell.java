package lab_09;

public final class Cell {

    private static final Cell[] CACHE = new Cell[10];

    static {
        for (int i = 0; i <= 9; i++) {
            CACHE[i] = new Cell(i);
        }
    }

    private final int value;

    private Cell(int value) {
        this.value = value;
    }

    public static Cell of(int value) {
        if (value < 0 || value > 9) {
            throw new IllegalArgumentException("Cell value must be 0..9");
        }
        return CACHE[value];
    }

    public int getValue() {
        return value;
    }
}
