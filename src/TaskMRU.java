import java.util.ArrayList;
import java.util.Collections;

public class TaskMRU implements Runnable {

    private int[] sequence; // Randomly generated sequence of page references
    private int maxMemoryFrames; // Number of memory frames available
    private int maxPageReference; // Max page reference possible
    public int[] pageFaults; // Stores page faults for given maxMemoryFrames


    public TaskMRU(int[] sequence, int maxMemoryFrames, int maxPageReference, int[] pageFaults) {
        this.sequence = sequence;
        this.maxMemoryFrames = maxMemoryFrames;
        this.maxPageReference = maxPageReference;
        this.pageFaults = pageFaults;
    }

    @Override
    public void run() {
        // Run the simulation
        ArrayList<Integer> frames = new ArrayList<Integer>();
        ArrayList<Integer> history = new ArrayList<Integer>();
        for(int i = 0; i < maxMemoryFrames; i++){
            frames.add(0);
            history.add(0);
        }

        int faults = 0;
        int rank = 1;
        for (int i = 0; i < sequence.length; i++) {
            int idxToReplace = 0;
            // Check if the new page ref already exists in the table
            if (!frames.contains(sequence[i])) {
                // If it doesn't, put it in
                int zeroIdx = frames.indexOf(0);
                if (zeroIdx != -1){
                    idxToReplace = zeroIdx;
                } else {
                    idxToReplace = history.indexOf(Collections.max(history));
                }

                // Replace the value with the value corresponding to the max value in history
                frames.set(idxToReplace, sequence[i]);
                history.set(idxToReplace, rank);
                rank++;
                faults++;

            } else {
                history.set(frames.indexOf(sequence[i]), rank);
                rank++;
            }
        }
        // Put the results back into pageFaults[]
        pageFaults[maxMemoryFrames - 1] = faults;
    }

/*
    @Override
    public void run() {


        // Run the simulation
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
                // figure out which page ref has been least recently used
                int idxToReplace = 0;
                int previousScore = 0;
                // Iterate through frames and compare each value to the previous values in the sequence
                for (int j = 0; j < maxMemoryFrames; j++) {
                    // If there are still null values, fill them
                    if (frames[j] == 0) {
                        idxToReplace = j;
                        break;
                    }

                    int score = 0;
                    // Compare against each value; score is the current index minus the last time the value was used
                    for (int k = i - 1; k >= 0; k--) {
                        if (sequence[k] == sequence[i] || score > previousScore) {
                            break;
                        } else {
                            score++;
                        }
                    }
                    // If value was used more recently than previous best, update idxToReplace
                    if (score < previousScore) {
                        idxToReplace = j;
                        previousScore = score;
                    }

                }
                // Replace the value with the best score
                frames[idxToReplace] = sequence[i];
                faults++;
            }
        }
        // Put the results back into pageFaults[]
        pageFaults[maxMemoryFrames - 1] = faults;

    }*/
}
