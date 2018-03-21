
public class Philosopher implements Runnable {

    private Fork leftFork;
    private Fork rightFork;
    private int idx;
    private PhilosopherStatus status;

    public Philosopher(Fork leftFork, Fork rightFork, int idx, PhilosopherStatus status) {
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.idx = idx;
        this.status = status;
    }

    @Override
    public void run() {
        while (true) {
            int processTime = (int) (Math.random() * 1000);
            int idleTime = (int) (Math.random() * 100);

            try {
                status.set(PhilosopherStatus.STATUS_IDLE);
                //Thread.sleep(idleTime); // if commented out, the deadlock is very likely to happen

                status.set(PhilosopherStatus.STATUS_WAITING);
                leftFork.acquireAndAcknowledge(idx);
                rightFork.acquireAndAcknowledge(idx);

                status.set(PhilosopherStatus.STATUS_EATING);
                Thread.sleep(processTime);

                leftFork.releaseAndAcknowledge();
                rightFork.releaseAndAcknowledge();
            } catch (InterruptedException e) {
                break; // the dinner is over
            }
        }
    }

}
