public class TaskFIFO implements Runnable {

    private int[] sequence; // Randomly generated sequence of page references
    private int maxMemoryFrames; // Number of memory frames available
    private int maxPageReference; // Max page reference possible
    private int[] pageFaults; // Stores page faults for given maxMemoryFrames


    public TaskFIFO(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        // Run the simulation
        int currentIdx = 0;
        int faults = 0;
        int[] frames = new int[maxMemoryFrames];
        for (int i = 0; i < sequence.length; i++) {
            // Check if the new page ref already exists in the table
            boolean foundRef = false;
            for (int j = 0; j < maxMemoryFrames; j++) {
                if (frames[j] == sequence[i]) {
                    foundRef = true;
                }
            }

            // If the ref doesn't exist, put it in and replace one
            if (!foundRef) {
                frames[currentIdx] = sequence[i];
                faults++;
                currentIdx++;
                if (currentIdx >= maxMemoryFrames) {
                    currentIdx = 0;
                }
            }
        }
        // Put the results back into pageFaults[]
        pageFaults[maxMemoryFrames - 1] = faults;
    }
}
