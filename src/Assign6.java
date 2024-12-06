import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Assign6 {
    private final int numSimulations = 1000;
    private final int totalFrames = 100;
    private final int sequenceLen = 1000;
    private final int maxPageRef = 250;
    private final ExecutorService pool;

    public Assign6() throws InterruptedException {
        // Create thread pool with as many threads as processors available
        pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Random random = new Random();

        // Loop through simulation logic numSimulations times
        for(int i = 0; i < numSimulations; i++){
            // Declare page fault array
            int[] pageFaultsF = new int[totalFrames];
            int[] pageFaultsL = new int[totalFrames];
            int[] pageFaultsM = new int[totalFrames];

            // create randomized sequence of 1000 numbers
            int[] s = new int[sequenceLen];
            for(int j = 0; j < sequenceLen; j++){
                s[j] = random.nextInt(1, maxPageRef);
            }

            // Run 100 simulations with different maximum frame sizes
            for (int f = 0; f < totalFrames; f++){
                Runnable fifo = new TaskFIFO(s, f, maxPageRef, pageFaultsF);
                Runnable lru = new TaskLRU(s, f, maxPageRef, pageFaultsL);
                Runnable mru = new TaskMRU(s, f, maxPageRef, pageFaultsM);
                pool.execute(fifo);
                pool.execute(lru);
                pool.execute(mru);
            }
            pool.awaitTermination(10, TimeUnit.SECONDS);
        }
    }
}