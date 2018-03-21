import java.util.concurrent.Semaphore;

/**
 * Fork is a binary semaphore - it permits one thread (philosopher) to use it
 */
public class Fork extends Semaphore {

    private ForkHolder forksHolder;
    private int idx;

    public Fork(int idx, ForkHolder forkHolder) {
        super(1); // 1 is number of permits, so we create binary semaphore
        this.idx = idx;
        this.forksHolder = forkHolder;
    }

    // sets holder to the current index, so the table knows who holds which fork (reference to holder is passed from table).
    public void acquireAndAcknowledge(int holder) throws InterruptedException {
        acquire();
        forksHolder.set(holder);
    }

    // release may be called even by threads that did not acquire the fork, so the holder must be checked first
    public void releaseAndAcknowledge(int holder) {
        if (forksHolder.get() == holder)
            forksHolder.set(ForkHolder.NO_HOLDER);
        release();
    }
}
