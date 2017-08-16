package com.smstd.concurrentdownload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.smstd.concurrentdownload.api.ReportingApiClient;
import java.io.File;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Thread save files saver that could be created from any thread
 * and safely write report from blocking queue.
 * 
 * Should be terminated from another thread
 * 
 * TODO: add check if file open/exist/corrupted  
 *
 * @author sergiy
 */
public class FileSaver implements Runnable {
        
    /**
     * reference to blocking queue
     */
    private final BlockingQueue<ReportingApiClient.Report> queue;
        
    /**
     * path to local storage
     */
    private final String path;
    
    /**
     * Create instance of runnable with custom implementation that holds:
     * 
     * @param queue reference to blocking queue
     * @param path path to local storage
     */
    public FileSaver(BlockingQueue<ReportingApiClient.Report> queue, String path) {
        this.queue = queue;
        this.path = path;
    }

    /**
     * will work infinitely and have to be terminated from outside
     */
    @Override
    public void run() {
        try {
            while(true){        
                ReportingApiClient.Report r = queue.take(); 
                writeReport(r, path);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * will write report unto local storage
     * 
     * @param report
     * @param localStoragePath 
     */
    private void writeReport(ReportingApiClient.Report report, String localStoragePath) {
        try {
            Path path = Paths.get(localStoragePath);
            
            // create directories to file if not exist
            if(!Files.exists(path))
                Files.createDirectories(path);
            
            // get full path
            String fullPathString = localStoragePath + File.separator + report.getName() + ".txt";
                    
            // convert string path to Path
            Path fullPath = Paths.get(fullPathString);
            
            // convert string content to bytes
            byte[] content = report.getContent().getBytes();            
            
            // write report
            Files.write(fullPath, content);
            
        } catch (IOException ex) {
            Logger.getLogger(FileSaver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
