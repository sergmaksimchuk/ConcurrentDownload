package com.smstd.concurrentdownload;

import com.smstd.concurrentdownload.api.ReportingApiClient;
import com.smstd.concurrentdownload.api.SlowReportingApiClient;
import java.util.concurrent.BlockingQueue;

/**
 * Thread save file downloader that could be created from any thread
 * and safely add value to blocking queue.
 * 
 * Will terminated as soon as file downloaded
 * 
 * TODO: add ability to download many files by one instance of downloader
 * 
 * @author sergiy
 */
public class FileDownloader implements Runnable {

    /**
     * an object that holds instance of client api
     */
    private final ReportingApiClient apiClient;
    
    /**
     * reference to blocking queue
     */
    private final BlockingQueue<ReportingApiClient.Report> queue;
    
    /**
     * generated report name
     */
    private final String repoName;
        
    /**
     * Create instance of runnable with custom implementation that holds:
     * 
     * @param queue reference to blocking queue
     * @param number of report that have to be downloaded
     */
    public FileDownloader(BlockingQueue<ReportingApiClient.Report> queue, int number) {
        this.queue = queue;
        this.repoName = generateName(number);        
        this.apiClient = createApiClient();
    }
    
    /**
     * will generate report name based on number passed as parameter
     * 
     * @param reportNumber decimal number of report
     * @return generated string name
     */
    private String generateName(int reportNumber) {
        return "report_" + String.valueOf(reportNumber);
    }
    
    /**
     * will create an instance of SlowReportingApiClient
     * 
     * @return instance of ReportingApiClient interface
     */
    private ReportingApiClient createApiClient() {
        return new SlowReportingApiClient();
    }

    /**
     * run() do nothing after fakeDownload executed, 
     * that is thread will be interrupted automatically
     */
    @Override
    public void run() {
        try {
            fakeDownload(repoName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Download file and add to blocking queue
     * 
     * @param repoName string name
     * @throws InterruptedException when thread is interrupted
     */
    private void fakeDownload(String reportName) throws InterruptedException {  
        queue.add(apiClient.getReport(reportName));
    } 
    
}
