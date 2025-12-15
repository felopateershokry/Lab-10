package lab_09;

/**
 * Viewer-side ONLY. Represents a single user action for logging and undo.
 * Required by Lab 10 (must NOT be used on controller side).
 */
public class UserAction {

    public final int x;
    public final int y;
    public final int val;
    public final int prev;

    public UserAction(int x, int y, int val, int prev) {
        this.x = x;
        this.y = y;
        this.val = val;
        this.prev = prev;
    }

    @Override
    public String toString() {
        // Format required by Lab 10: (x, y, val, prev)
        return x + "," + y + "," + val + "," + prev;
    }
}
