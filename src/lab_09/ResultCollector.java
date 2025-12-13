package lab_09;

import java.util.concurrent.atomic.AtomicBoolean;

public class ResultCollector {

    private final AtomicBoolean valid = new AtomicBoolean(true);
    private final AtomicBoolean incomplete = new AtomicBoolean(false);

    public void markInvalid() {
        valid.set(false);
    }

    public void markIncomplete() {
        incomplete.set(true);
    }

    public boolean isValid() {
        return valid.get();
    }

    public boolean isIncomplete() {
        return incomplete.get();
    }
}
