/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smstd.concurrentdownload;

import com.smstd.concurrentdownload.api.ReportingApiClient;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sergiy
 */
public class FileDownloaderTest {
    
    public FileDownloaderTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of run method, of class FileDownloader.
     */
    @Test
    public void downloadTest() {
        System.out.println("run");
        
        BlockingQueue<ReportingApiClient.Report> queue = new LinkedBlockingQueue<>();
        new FileDownloader(queue, 123).run();
        
        assertEquals(queue.size(), 1);
    }
    
}
