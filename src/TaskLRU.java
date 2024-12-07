import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
                    idxToReplace = history.indexOf(Collections.min(history));
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












//
//    @Override
//    public void run() {
//        // Run the simulation
//        ArrayList<Integer> frames = new ArrayList<Integer>();
//        ArrayList<Integer> history = new ArrayList<Integer>();
//        for(int i = 0; i < maxMemoryFrames; i++){
//            frames.add(0);
//            history.add(0);
//        }
//
//        int faults = 0;
//        for (int i = 0; i < sequence.length; i++) {
//            int idxToReplace = 0;
//            // Check if the new page ref already exists in the table
//            if (!frames.contains(sequence[i])) {
//                // If it doesn't, put it in
//                int zeroIdx = frames.indexOf(0);
//                if (zeroIdx != -1){
//                    idxToReplace = zeroIdx;
//                } else {
//                    idxToReplace = history.indexOf(Collections.max(history));
//                }
//
//                // Replace the value with the value corresponding to the max value in history
//                frames.set(idxToReplace, sequence[i]);
//                history.set(idxToReplace, 0);
//                faults++;
//
//                // Update history
//                for(int j = 0; j < maxMemoryFrames; j++){
//                    history.set(j, history.get(j) + 1);
//                }
//            } else {
//                history.set(frames.indexOf(sequence[i]), 0);
//            }
//        }
//        // Put the results back into pageFaults[]
//        pageFaults[maxMemoryFrames - 1] = faults;
//    }
}
