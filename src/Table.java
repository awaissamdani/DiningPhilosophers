import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Table {

    private final int noOfPhilosophers = 5;
    private List<Philosopher> philosophers = new ArrayList<>();
    private List<PhilosopherStatus> statuses = new ArrayList<>();

    private final int noOfForks = noOfPhilosophers;
    private List<Fork> forks = new ArrayList<>();
    private List<ForkHolder> forksHolders = new ArrayList<>();

    public void startDinner() throws InterruptedException {
        for (int i = 0; i < noOfForks; i++) {
            ForkHolder forkHolder = new ForkHolder();
            Fork fork = new Fork(1, i, forkHolder);
            forks.add(fork);
            forksHolders.add(forkHolder);
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(noOfPhilosophers);

        for (int i = 0; i < noOfPhilosophers; i++) {
            Fork leftFork = forks.get(i);
            Fork rightFork = forks.get((i+1) % noOfPhilosophers);

            PhilosopherStatus status = new PhilosopherStatus();
            Philosopher philosopher = new Philosopher(leftFork, rightFork, i, status);
            philosophers.add(philosopher);
            statuses.add(status);

            threadPool.submit(philosopher);
        }

        while(true) {
            System.out.println("statuses: " + statuses);
            System.out.println("holders: " + forksHolders);

            int[] deadlockHolders = {0,1,2,3,4};
            if(forksHolders.toString().equals(Arrays.toString(deadlockHolders))) {
                System.out.println("DEADLOCK");
                threadPool.shutdownNow();
                break;
            }
            Thread.sleep(500);
        }
    }
}
