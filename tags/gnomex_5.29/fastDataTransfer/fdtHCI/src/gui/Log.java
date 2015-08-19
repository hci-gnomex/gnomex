/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.util.logging.Level;

/**
 *Just a dummy class to decide whether to use console logger or GUI logger depending on whether its a server mode and client mode.
 *
 * @author Shobhit
 */
public abstract class Log{
    private static Log log;

    protected Log()
    {
        
    }

    public static synchronized Log getLoggerInstance()
    {
        if(FdtMain.isIsServerMode())
            return ConsoleLogger.getConsoleLoggerInstance();
        else
            return GUILogger.getGUILoggerInstance();
    }

    public abstract void log(String message);

    public abstract void logError(Throwable t);

    public abstract void logError(String message);

    public abstract void setLogLevel(String logLevel);

    public abstract boolean isLoggable(Level FINE);

    public abstract void log(Level FINE, String string);

    public abstract void log(Level level, String string, Throwable t);

    public abstract void log(Level level, String string, Object object) ;

    public abstract void info(String string) ;

    public abstract void warning(String string);

    public abstract void logError(String string, Throwable t);
}
