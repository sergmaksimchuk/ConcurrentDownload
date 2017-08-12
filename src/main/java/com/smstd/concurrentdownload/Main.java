package com.smstd.concurrentdownload;

/**
 *
 * @author sergiy
 */
public class Main {

    /**
     * base folder path
     */
    private static final String TARGET_DIRECTORY_PATH = "downloads/reports";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int reportsCount = 500;
                
        // let's run code
        new Applicant().fetchReports(reportsCount, TARGET_DIRECTORY_PATH);
        
    }
}
