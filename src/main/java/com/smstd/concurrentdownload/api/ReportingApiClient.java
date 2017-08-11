package com.smstd.concurrentdownload.api;

/**
 * This interface represents general API for report generation
 */
public interface ReportingApiClient {
    /**
     * Generates report by name provided.
     */
    Report getReport(String name);

    class Report {
        private String name;
        private String content;

        public Report(final String name, final String content) {
            this.name = name;
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public String getContent() {
            return content;
        }
    }
}
