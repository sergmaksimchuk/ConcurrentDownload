package com.smstd.concurrentdownload;

import com.smstd.concurrentdownload.api.ReportingApiClient;
import com.smstd.concurrentdownload.api.SlowReportingApiClient;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author sergiy
 */
public class FileDownloader implements Runnable {

    // api 
    private final ReportingApiClient apiClient;
    
    // queue for concurent saving
    private final BlockingQueue<ReportingApiClient.Report> queue;
    
    // client side generated name
    private final String repoName;
        
    public FileDownloader(BlockingQueue<ReportingApiClient.Report> queue, int number) {
        this.queue = queue;
        this.repoName = generateName(number);        
        this.apiClient = createApiClient();
    }
    
    private String generateName(int reportNumber) {
        return "report_" + String.valueOf(reportNumber);
    }
    
    private ReportingApiClient createApiClient() {
        return new SlowReportingApiClient();
    }

    @Override
    public void run() {
        try {
            fakeDownload(repoName);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void fakeDownload(String r) throws InterruptedException {  
        queue.add(apiClient.getReport(r));
    } 
    
}
