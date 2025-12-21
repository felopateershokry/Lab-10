package lab_09;

public class UserAction {

    public final int x, y, value, prev;

    public UserAction(int x, int y, int value, int prev) {
        this.x = x;
        this.y = y;
        this.value = value;
        this.prev = prev;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + value + "," + prev + ")";
    }
}
