package com.smstd.concurrentdownload.utils;

import com.smstd.concurrentdownload.Main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergiy
 */
public class Utils {
    
    /**
     * calculate count of files that already exist in dictionary
     * 
     * @param directoryPath destination path for local storage
     * @return count of files presented in directory by path
     */
    public static int getFilesCount(String directoryPath) {
        int count = 0;
        Path path = Paths.get(directoryPath);
        if(Files.exists(path)) {
            try {
                count = (int) Files.list(path).count();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return count;
    }
    
    /**
     * terminate all threads
     * 
     * @param threads queue of threads that have to be interrupted
     */
    public static void interuptThreads(Queue<Thread> threads){
        while(!threads.isEmpty()) {
            // Retrieves and removes the head (first element) of this list.
            Thread t = threads.poll();
            
            // terminate thread
            if(!t.isInterrupted())
                t.interrupt();
        }
    }
    
}
