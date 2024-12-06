public class TaskLRU implements Runnable {

    private int[] sequence; // Randomly generated sequence of page references
    private int maxMemoryFrames; // Number of memory frames available
    private int maxPageReference; // Max page reference possible
    public int[] pageFaults; // Stores page faults for given maxMemoryFrames


    public TaskLRU(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        // Run the simulation
        // Put the results back into pageFaults[]

    }
}
