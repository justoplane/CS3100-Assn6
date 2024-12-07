import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Assign6 {
    private static final int numSimulations = 1000;
    private static final int totalFrames = 100;
    private static final int sequenceLen = 1000;
    private static final int maxPageRef = 250;
    private static ArrayList<Integer> numAnomalies = new ArrayList<Integer>();
    private static ArrayList<Integer> maxDelta = new ArrayList<Integer>();

    public Assign6() {
    }
    public static void main(String[] args) {
        // Create thread pool with as many threads as processors available
        Random random = new Random();

        // Create output stream to store belady's calculation report
        ByteArrayOutputStream outputStreamFIFO = new ByteArrayOutputStream();
        PrintStream beladyPrintStreamFIFO = new PrintStream(outputStreamFIFO);
        ByteArrayOutputStream outputStreamLRU = new ByteArrayOutputStream();
        PrintStream beladyPrintStreamLRU = new PrintStream(outputStreamLRU);
        ByteArrayOutputStream outputStreamMRU = new ByteArrayOutputStream();
        PrintStream beladyPrintStreamMRU = new PrintStream(outputStreamMRU);

        // I'm a big dumb idiot and this code makes me want to throw up...
        // I'm sorry to whoever has to see this.
        for (int i = 0; i < 3; i++){
            numAnomalies.add(0);
            maxDelta.add(0);
        }

        // Variables to hold totals
        int fifoPF = 0;
        int lruPF = 0;
        int mruPF = 0;
        long startTime = System.currentTimeMillis();
        // Do something for fifo anomalies

        // Loop through simulation logic numSimulations times
        for(int i = 0; i < numSimulations; i++){
            ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            // Declare page fault array
            int[] pageFaultsF = new int[totalFrames];
            int[] pageFaultsL = new int[totalFrames];
            int[] pageFaultsM = new int[totalFrames];

            // create randomized sequence of 1000 numbers
            int[] s = new int[sequenceLen];
            for(int j = 0; j < sequenceLen; j++){
                s[j] = random.nextInt(1, maxPageRef);
            }

            // Run 100 simulations with different maximum frame sizes, starting at 1
            for (int f = 1; f < totalFrames + 1; f++){
                Runnable fifo = new TaskFIFO(s, f, maxPageRef, pageFaultsF);
                Runnable lru = new TaskLRU(s, f, maxPageRef, pageFaultsL);
                Runnable mru = new TaskMRU(s, f, maxPageRef, pageFaultsM);
                pool.execute(fifo);
                pool.execute(lru);
                pool.execute(mru);
            }
            // Wait for batch of 100 to finish
            try {
                pool.shutdown();
                pool.awaitTermination(10, TimeUnit.SECONDS);
            } catch (Exception e){System.out.println(e);}

            // Take result and add it to totals
            int prevFaultsFIFO = 0;
            int prevFaultsLRU = 0;
            int prevFaultsMRU = 0;

            for (int j = 0; j < pageFaultsF.length; j++){
                // find the smallest integer
                int smallestInt = pageFaultsF[j];
                if (pageFaultsL[j] < smallestInt){
                    smallestInt = pageFaultsL[j];
                }
                if (pageFaultsM[j] < smallestInt){
                    smallestInt = pageFaultsM[j];
                }

                // Add a point to each total that matches the smallest integer
                if (pageFaultsF[j] <= smallestInt){fifoPF++;}
                if (pageFaultsL[j] <= smallestInt){lruPF++;}
                if (pageFaultsM[j] <= smallestInt){mruPF++;}
            }

            // Calculate anomaly stuff
            calculateBelady(i, pageFaultsF, beladyPrintStreamFIFO, 0, 0);
            calculateBelady(i, pageFaultsL, beladyPrintStreamLRU, 1, 1);
            calculateBelady(i, pageFaultsM, beladyPrintStreamMRU, 2, 2);
        }

        System.out.println("Simulation took " + (System.currentTimeMillis() - startTime) + " ms");
        System.out.println("FIFO min PF: " + fifoPF);
        System.out.println("LRU min PF: " + lruPF);
        System.out.println("MRU min PF: " + mruPF);
        // Write belady report
        System.out.println("\nBelady's Anomaly Report for FIFO");
        beladyPrintStreamFIFO.flush();
        System.out.print(outputStreamFIFO.toString());
        System.out.println("Anomaly detected " + numAnomalies.get(0) + " times in " + numSimulations +
                " simulations with a max delta of " + maxDelta.get(0));
        System.out.println("\nBelady's Anomaly Report for LRU");
        beladyPrintStreamLRU.flush();
        System.out.print(outputStreamLRU.toString());
        System.out.println("Anomaly detected " + numAnomalies.get(1) + " times in " + numSimulations +
                " simulations with a max delta of " + maxDelta.get(1));
        System.out.println("\nBelady's Anomaly Report for MRU");
        beladyPrintStreamMRU.flush();
        System.out.print(outputStreamMRU.toString());
        System.out.println("Anomaly detected " + numAnomalies.get(2) + " times in " + numSimulations +
                " simulations with a max delta of " + numAnomalies.get(2));
    }

    public static void calculateBelady(int simNumber, int[] pageFaults, PrintStream printStream, int delta, int anomalies){
        for (int i = 1; i < pageFaults.length; i++){
            if (pageFaults[i] > pageFaults[i-1]){
                numAnomalies.set(anomalies, numAnomalies.get(anomalies) + 1);
                // Set maximum delta if increased
                if ((pageFaults[i] - pageFaults[i-1]) > maxDelta.get(delta)){
                    maxDelta.set(delta, (pageFaults[i] - pageFaults[i-1]));
                }
                printStream.println("\tAnomaly detected in sim #" + (simNumber+1) + " - " +
                        pageFaults[i-1] + " PF's @ " + (i-1) + " frames vs. " +
                        pageFaults[i] + " PF's @ " + i + " frames (d" +
                        (pageFaults[i] - pageFaults[i-1]) + ")");
            }
        }
    }
}