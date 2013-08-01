/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Level;

/**
 * This class is used only for logging in client mode. Consolelogger is used for server mode.
 *
 * @author Shobhit
 */
public class GUILogger extends Log{

    private static LogPanel logPanelInstance ;
    private static GUILogger guiLogger;
    private static Level level;
    private StringWriter sw ;

    public void setLogLevel(String level)
    {
        if(level.equals("INFO"))
            GUILogger.level = Level.INFO;

        else if(level.equals("FINE"))
            GUILogger.level = Level.FINE;

        else if(level.equals("FINER"))
            GUILogger.level = Level.FINER;

        else if(level.equals("FINEST"))
            GUILogger.level = Level.FINEST;

        else if(level.equals("WARNING"))
            GUILogger.level = Level.WARNING;

        else if(level.equals("SEVERE"))
            GUILogger.level = Level.SEVERE;
    }

    public boolean isLoggable(Level level)
    {
        if(GUILogger.level == level)
            return true;
        else
            return false;
    }


    private GUILogger()
    {
        GUILogger.logPanelInstance = LogPanel.getLogPanelInstance();
        sw = new StringWriter();
    }

    public void log(String Message)
    {
        GUILogger.logPanelInstance.logMessage(Message + "\n");
    }

    public void log(Level level,String Message)
    {
        GUILogger.logPanelInstance.logMessage(level.toString()+":"+Message + "\n");
    }

    public void log(Level level, String Message, Throwable thrwbl)
    {
        GUILogger.logPanelInstance.logMessage(level.toString()+":"+Message + "\n");
        if(thrwbl != null)
        {
            thrwbl.printStackTrace(new PrintWriter(sw));
            GUILogger.logPanelInstance.logMessage(sw.toString() +"\n" );
        }
    }

    public void logError(String Message, Throwable thrwbl)
    {
        GUILogger.logPanelInstance.logMessage("ERROR:"+Message + "\n");
        if(thrwbl !=null)
        {
            thrwbl.printStackTrace(new PrintWriter(sw));
            GUILogger.logPanelInstance.logMessage(sw.toString() +"\n" );
        }
    }

    public void logError(String Message)
    {
        GUILogger.logPanelInstance.logMessage("ERROR:"+Message + "\n");
    }

    public void logError(Throwable thrwbl)
    {
        if(thrwbl != null)
        {
            thrwbl.printStackTrace(new PrintWriter(sw));
            GUILogger.logPanelInstance.logMessage(sw.toString() +"\n" );
        }
    }

    public static synchronized GUILogger getGUILoggerInstance()
    {
        if(guiLogger == null)
               guiLogger = new GUILogger();
        return guiLogger;
    }


    public void severe(String string) {
        GUILogger.logPanelInstance.logMessage(Level.SEVERE.toString()+": "+string+"\n");
    }

    public void warning(String string) {
        GUILogger.logPanelInstance.logMessage(Level.WARNING.toString()+": "+string+"\n");
    }

    public void info(String string) {
        GUILogger.logPanelInstance.logMessage(Level.INFO.toString()+": "+string+"\n");
    }

    public void config(String string) {
        
    }

    public void fine(String string) {
        GUILogger.logPanelInstance.logMessage(Level.FINE.toString()+": "+string + "\n");
    }

    public void finer(String string) {
        GUILogger.logPanelInstance.logMessage(Level.FINER.toString()+": "+string + "\n");
    }

    public void finest(String string) {
        GUILogger.logPanelInstance.logMessage(Level.FINEST.toString()+": "+string + "\n");
    }

    public void log(Level INFO, String string, int num) {
        GUILogger.logPanelInstance.logMessage(Level.INFO.toString()+": "+ string +" " + num + "\n");
    }

    public void log(Level FINE, String string, Object[] object) {
        GUILogger.logPanelInstance.logMessage(Level.FINE.toString()+": "+ string + " " + object.toString() + "\n");
     
    }

    public void log(Level INFO, String string, String string0) {
        GUILogger.logPanelInstance.logMessage(Level.INFO.toString()+": "+ string +" " + string0 + "\n");
    }

    public void log(Level level, String string, Object object) {
        GUILogger.logPanelInstance.logMessage(Level.INFO.toString()+": "+ string + " " + object.toString() + "\n");
    }

    void logError(Level SEVERE, String string, Exception ex) {
        ex.printStackTrace(new PrintWriter(sw));
            GUILogger.logPanelInstance.logMessage(Level.SEVERE.toString()+ ": "+ sw.toString() +"\n" );
       }
}
