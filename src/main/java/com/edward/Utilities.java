package com.edward;

import java.util.Date;
import java.util.logging.*;

public abstract class Utilities {
    final static Logger logger = Logger.getLogger("SCRAPER_LOGGER");
    public static void setupLogger(Logger logger){
        FileHandler fh;
        try {
            fh = new FileHandler("scraper.log");
            fh.setFormatter(new MyFormatter());
            logger.addHandler(fh);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not setup logger file handler.");
            e.printStackTrace();
        }
    }
    public static void log(Level level,String message){
        logger.log(level, message);
    }
}
class MyFormatter extends Formatter {

    @Override
    public String format(LogRecord record) {
        return new Date(record.getMillis())+"::"
                +record.getMessage()+"\n";
    }
}


