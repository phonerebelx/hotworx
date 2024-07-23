package com.hotworx.Wrapper;
import android.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class LoggerWrapper {
    private final Logger logger;

    private LoggerWrapper(Logger logger) {
        this.logger = logger;
    }

    public static LoggerWrapper getLogger(Class<?> clazz) {
        return new LoggerWrapper(LoggerFactory.getLogger(clazz));
    }


    public void trace(String message) {
        logger.trace(message);
    }

    public void anotherTrace(String message) {
        Log.d("MYTAG",message);
    }
}
