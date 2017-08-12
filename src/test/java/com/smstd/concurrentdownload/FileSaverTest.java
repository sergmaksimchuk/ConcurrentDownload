/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smstd.concurrentdownload;

import com.smstd.concurrentdownload.api.ReportingApiClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class FileSaverTest {
    
    public FileSaverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test of run method, of class FileSaver.
     */
    @Test
    public void writeFileTest() throws InterruptedException {
        System.out.println("run");
        
        BlockingQueue<ReportingApiClient.Report> queue = new LinkedBlockingQueue<>();
        String testFolderPath = "downloadTest/files";
        
        // start thread
        Thread t = new Thread(new FileSaver(queue, testFolderPath));
        t.start();

        // simulate new repotr in queue
        String name = "sample";
        queue.put(new ReportingApiClient.Report(name, "content[" + name + "]"));
     
        // lets time to execute
        Thread.currentThread().sleep(1000);
        t.interrupt();
        
        // check that new file exist
        Path p = Paths.get(testFolderPath);
        if(Files.exists(p)){
        Path p2 = Paths.get(testFolderPath + "/" + name + ".txt");
            assertTrue(Files.exists(p2));
        }
    }
    
}
