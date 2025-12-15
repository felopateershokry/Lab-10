package lab_09;

/**
 * Collects verification results. NO thread-safe constructs (as required by Lab
 * 10).
 */
public class ResultCollector {

    private boolean valid = true;
    private boolean incomplete = false;

    public void markInvalid() {
        valid = false;
    }

    public void markIncomplete() {
        incomplete = true;
    }

    public boolean isValid() {
        return valid;
    }

    public boolean isIncomplete() {
        return incomplete;
    }
}
