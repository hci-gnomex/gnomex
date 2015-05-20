/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *This class is used when we start fdt in server mode. It is not used at all in GUI mode.
 * 
 * @author Shobhit
 */
public class ConsoleLogger extends Log{
    private static ConsoleLogger consoleLogger;
    private static Level level;
    private static final Logger logger = Logger.getLogger("ConsoleLogger");

    public void setLogLevel(String level)
    {
        if(level.equals("INFO"))
            ConsoleLogger.level = Level.INFO;

        else if(level.equals("FINE"))
            ConsoleLogger.level = Level.FINE;

        else if(level.equals("FINER"))
            ConsoleLogger.level = Level.FINER;

        else if(level.equals("FINEST"))
            ConsoleLogger.level = Level.FINEST;

        else if(level.equals("WARNING"))
            ConsoleLogger.level = Level.WARNING;

        else if(level.equals("SEVERE"))
            ConsoleLogger.level = Level.SEVERE;
    }

    public boolean isLoggable(Level level)
    {
        if(ConsoleLogger.level == level)
            return true;
        else
            return false;
    }


    private ConsoleLogger()
    {
        

    }

    public void log(String Message)
    {
        System.out.println(Message);
    }

    public void log(Level level,String Message)
    {
        logger.log(level, Message);
    }

    public void log(Level level, String Message, Throwable thrwbl)
    {
        logger.log(level, Message);
        thrwbl.printStackTrace();
    }

    public void logError(String Message, Throwable thrwbl)
    {
        
        System.out.println(Message);
        thrwbl.printStackTrace();
        
    }

    public void logError(String Message)
    {
        System.err.println(Message);
    }

    public void logError(Throwable thrwbl)
    {
        thrwbl.printStackTrace();
    }

    public static synchronized ConsoleLogger getConsoleLoggerInstance()
    {
        if(consoleLogger == null)
               consoleLogger = new ConsoleLogger();
        return consoleLogger;
    }


    public void severe(String string) {
        logger.log(Level.SEVERE, string);
    }

    public void warning(String string) {
        logger.log(Level.WARNING, string);
    }

    public void info(String string) {
        logger.log(Level.INFO, string);
    }

    public void config(String string) {
        
    }

    public void fine(String string) {
        logger.log(Level.FINE, string);
    }

    public void finer(String string) {
        logger.log(Level.FINER, string);
    }

    public void finest(String string) {
        logger.log(Level.FINEST, string);
    }

    public void log(Level FINE, String string, Object[] object) {
        logger.log(Level.FINE, string, object);
     
    }

    public void log(Level level, String string, Object object) {
        logger.log(level, string, object);
    }

    void logError(Level SEVERE, String string, Exception ex) {
        
            ex.printStackTrace();
        
    }
}
