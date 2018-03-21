import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Table {

    private final int noOfPhilosophers = 5;
    private List<Philosopher> philosophers = new ArrayList<>();
    private List<PhilosopherStatus> statuses = new ArrayList<>(); // statuses of philosophers (eating, waiting, idle)
    private List<Future<Integer>> futures = new ArrayList<>(); // futures where the results of computation are saved - we need them to be able to interrupt the philosopher

    private final int noOfForks = noOfPhilosophers;
    private List<Fork> forks = new ArrayList<>();
    private List<ForkHolder> forksHolders = new ArrayList<>(); // indices of philosphers holding fork at particular index

    // lists of indices that represent how the deadlock situation looks like - each philosopher holds one fork
    private List<Integer> deadlockHoldersLeft = new ArrayList<>();
    private List<Integer> deadlockHoldersRight = new ArrayList<>();

    public void startDinner() throws InterruptedException {
        for (int i = 0; i < noOfForks; i++) {
            ForkHolder forkHolder = new ForkHolder();
            Fork fork = new Fork(i, forkHolder);
            forks.add(fork);
            forksHolders.add(forkHolder);
            deadlockHoldersLeft.add(i);
            deadlockHoldersRight.add((i-1) % noOfPhilosophers);
        }

        ExecutorService threadPool = Executors.newFixedThreadPool(noOfPhilosophers); // number of threads is equal to number of philosophers

        // initialize forks, philosophers and so on
        for (int i = 0; i < noOfPhilosophers; i++) {
            Fork leftFork = forks.get(i);
            Fork rightFork = forks.get((i + 1) % noOfPhilosophers);

            PhilosopherStatus status = new PhilosopherStatus();
            Philosopher philosopher = new Philosopher(leftFork, rightFork, i, status);
            philosophers.add(philosopher);
            statuses.add(status);

            futures.add(threadPool.submit(philosopher));
        }

        while (true) {
            // we print indices of philosophers that are currently eating
            List<Integer> eating = new ArrayList<>();
            for (int i = 0; i < statuses.size(); i++) {
                if(statuses.get(i).get() == PhilosopherStatus.STATUS_EATING) {
                    eating.add(i);
                }
            }
            System.out.println("eating: " + eating);

            // if deadlock happens, we randomly chose a victim and take his forks by interrupting a thread and starting it again without forks
            if (forksHolders.toString().equals(deadlockHoldersLeft.toString()) ||
                    forksHolders.toString().equals(deadlockHoldersRight.toString())) {
                int victim = (int) (Math.random() * philosophers.size());
                futures.get(victim).cancel(true);
                futures.set(victim, threadPool.submit(philosophers.get(victim)));
            }

            Thread.sleep(500); // short sleep so the console doesn't get crazy
        }
    }
}
