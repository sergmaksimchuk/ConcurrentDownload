package com.smstd.concurrentdownload;

import com.smstd.concurrentdownload.utils.Utils;
import com.smstd.concurrentdownload.api.ReportingApiClient;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * BRIEF:
 * An implementation of "producer-consumer" pattern that allows to download 
 * and save to local storage concurrently large number of files.
 * 
 * PATTERN:
 * The main point is that we have many producers(threads) for downloading
 * and many consumers(threads) for saving files.
 * 
 * BLOCKING QUEUE:
 * For temporary holding files between producer and consumer is used blocking 
 * queue. Blocking queue is kind of queue that absolutely thread-safe 
 * implementation of queue.
 * 
 * @author sergiy
 */
public class Applicant {

    private final int MAX_EXECUTING_LIFETIME_MILLISEC = 10000; // 10 sec

    public void fetchReports(int bound, String destinationPath) {
        
        // hold downloaded reports in thread-sage blocking queue
        BlockingQueue<ReportingApiClient.Report> blockingQueue = new LinkedBlockingQueue<>(bound);

        // start @N_PRODUCERS to download 
        int N_PRODUCERS = bound;
        for (int i = 1; i <= N_PRODUCERS; i++) {
            int reportNumber = i;
            new Thread(new FileDownloader(blockingQueue, reportNumber)).start();
        }

        // hold consumers to release later
        Queue<Thread> consumersList = new LinkedList<>();

        // start @N_CONSUMERS to write files 
        int N_CONSUMERS = 5;
        for (int i = 1; i <= N_CONSUMERS; i++) {
            Thread t = new Thread(new FileSaver(blockingQueue, destinationPath));
            t.start();
           
            // add link to consumer thread 
            consumersList.add(t);
        }

        System.out.println("fetching reports...");

        // figure out close program rules
        Rule filesCountRule = (a) -> a >= bound;
        Rule elapsedTimeRule = (a) -> a > MAX_EXECUTING_LIFETIME_MILLISEC;
       
        // wait moment to exit program
        try {
            int elapsedTime = 0;
            int filesCount = 0;
            int sleepMilli = 500;

            // while all consumers finished works
            while (!consumersList.isEmpty()) {
               
                // sleep
                Thread.currentThread().sleep(sleepMilli);

                // increase elapsed time
                elapsedTime += sleepMilli;

                // get files count
                filesCount = Utils.getFilesCount(destinationPath);

                System.out.println("saved files: " + String.valueOf(filesCount));

                // check rules
                if (filesCountRule.check(filesCount) || elapsedTimeRule.check(elapsedTime)) {

                    System.out.println("terminate...");
                    
                    // terminate all threads
                    Utils.interuptThreads(consumersList);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    interface Rule {
        boolean check(int a);
    }
}
