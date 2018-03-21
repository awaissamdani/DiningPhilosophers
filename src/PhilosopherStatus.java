/**
 * Wrapper around integer variable with constants for statuses
 */
public class PhilosopherStatus {
    private int status;

    public static int STATUS_IDLE = 0;
    public static int STATUS_WAITING = 1;
    public static int STATUS_EATING = 2;

    public PhilosopherStatus() {
        status = STATUS_IDLE;
    }

    public void set(int status) {
        this.status = status;
    }

    public int get() {
        return status;
    }

    @Override
    public String toString() {
        return Integer.toString(status);
    }
}
