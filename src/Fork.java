import java.util.concurrent.Semaphore;

public class Fork extends Semaphore {

    private ForkHolder forksHolder;
    private int idx;

    public Fork(int permits, int idx, ForkHolder forksHolder) {
        super(permits);
        this.idx = idx;
        this.forksHolder = forksHolder;
    }

    public void acquireAndAcknowledge(int holder) throws InterruptedException {
        acquire();
        forksHolder.set(holder);
    }

    public void releaseAndAcknowledge() throws InterruptedException {
        release();
        forksHolder.set(ForkHolder.NO_HOLDER);
    }
}
