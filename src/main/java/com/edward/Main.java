package com.edward;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Utilities {
    static int regionLimiter=10;
    static int mayorLimiter=10;
    static int pageLimiter=10;
    public static void main(String[] args) {
        Logger logger = Logger.getLogger("SCRAPER_LOGGER");
        com.edward.Utilities.setupLogger(logger);
        log(Level.INFO, "Main object created.");
        Scraper scraper = new Scraper(regionLimiter, mayorLimiter,pageLimiter);
        scraper.scrape();
    }
}
