import java.util.concurrent.Callable;

public class Philosopher implements Callable<Integer> {

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
    public Integer call() throws Exception {
        while (true) {
            int eatingTime = (int) (Math.random() * 1000); // randomly chosen
            int thinkingTime = (int) (Math.random() * 100);

            try {
                status.set(PhilosopherStatus.STATUS_IDLE);
                // our philosophers eat much more than think, so the deadlock is very likely to happen
                // Thread.sleep(idleTime);

                status.set(PhilosopherStatus.STATUS_WAITING);
                leftFork.acquireAndAcknowledge(idx);
                rightFork.acquireAndAcknowledge(idx);

                status.set(PhilosopherStatus.STATUS_EATING);
                Thread.sleep(eatingTime); // eating is represented by sleeping

                leftFork.releaseAndAcknowledge(idx);
                rightFork.releaseAndAcknowledge(idx);

            } catch (InterruptedException e) {
                System.out.println("Philosopher " + idx + ": Someone has stolen my forks!");
                leftFork.releaseAndAcknowledge(idx);
                rightFork.releaseAndAcknowledge(idx);
                status.set(PhilosopherStatus.STATUS_IDLE);
                return idx;
            }
        }
    }
}
