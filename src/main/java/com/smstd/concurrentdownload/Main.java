package com.smstd.concurrentdownload;

import com.smstd.concurrentdownload.api.ReportingApiClient;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sergiy
 */
public class Main {

    static String savePath = "downloads/reports";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int BOUND = 500;
        
        BlockingQueue<ReportingApiClient.Report> queue = new LinkedBlockingQueue<>(BOUND);
        

        /**
         * @N_PRODUCERS number of repository
         */
        int N_PRODUCERS = BOUND;
        for(int i = 1; i <= N_PRODUCERS; i++){
            new Thread(new FileDownloader(queue, i)).start();
        }
        
        
        
        List<Thread> consumersList = new ArrayList<>();
        
        int N_CONSUMERS = 5;
        
        /**
         * @N_CONSUMERS number of consumers
         */
        for(int i = 1; i <= N_CONSUMERS; i++){
          Runnable saver = new FileSaver(queue, savePath);
          Thread t = new Thread(saver);
          consumersList.add(t);
          t.start();
        }
        
        
        
          
          Thread watch_dog = new Thread(() -> {
                try {
                    int elapsedTime = 0;
                    int sleepMilli = 500;
                    
                    while(true){
                        Thread.currentThread().sleep(sleepMilli);
                        
                        elapsedTime += sleepMilli;
                        
                        
                        
                        long count = 0;
                        
                        Path path = Paths.get(savePath);
                        if(Files.exists(path)) {
                            try {
                                count = Files.list(path).count();
                            } catch (IOException ex) {
                                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        
                        
                        System.out.println("Count: " + String.valueOf(count));
                        
                        boolean filesCountRule = count >= BOUND;
                        boolean elapsedTimeRule = elapsedTime > 6000;
                        
                        if(filesCountRule || elapsedTimeRule){
                            // terminate all threads
                            for(Thread t
                                    : consumersList) {
                                if(!t.isInterrupted())
                                    t.interrupt();
                            }
                            Thread.currentThread().interrupt();
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
          watch_dog.start();     
          
          
//          Thread deamon = new Thread(() -> {
//              try{
//                  while(true)
//                        Thread.currentThread().sleep(100);
//              }catch(InterruptedException e){
//                    Thread.currentThread().interrupt();
//              }
//          });
//          
//          deamon.setDaemon(true);
//          deamon.start();
          
    }
    
    void terminateThreads() {
        
    }
    
    
 /*
    () -> {
        try {
            while(true){
                ReportingApiClient.Report r2 = queue.take();
                save(r2.getName(), r2.getContent());
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    */   
}
