package com.smstd.concurrentdownload;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import com.smstd.concurrentdownload.api.ReportingApiClient;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergiy
 */
public class FileSaver implements Runnable {
    
    // reference to blocing queue
    private final BlockingQueue<ReportingApiClient.Report> queue;
    
    private final String path;
    
    public FileSaver(BlockingQueue<ReportingApiClient.Report> queue, String path) {
        this.queue = queue;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            while(true){
                saveNextFile(queue);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void saveNextFile(BlockingQueue<ReportingApiClient.Report> queue) throws InterruptedException {
        ReportingApiClient.Report r = queue.take(); 
        try{
            save(r, path);
        } catch(IOException e){
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e); 
        }
    }    
    private void save(ReportingApiClient.Report r, String savePath) throws IOException{
        Path path = Paths.get(savePath);
        
        if(!Files.exists(path))
            Files.createDirectories(path);

        byte[] content = r.getContent().getBytes();
                
        Path filePath = Paths.get(savePath + "/" + r.getName() + ".txt");

        Files.write(filePath, content);
//        System.out.println("saved: " + r.getContent());  
    }
}
