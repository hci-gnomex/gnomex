/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.utils;

import gui.GUILogger;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;

/**
 *This class creates and reads a fdt prefs file which is used to read the last saved directory location used by the user.
 *
 * @author Shobhit
 */
public class UserPreferences {

    String fileName;

    Properties props;

    String home;

    String lastDir;

    File dir;

    private static GUILogger guiLogger;

    public String getLastDir() {
        return lastDir;
    }

    public void setLastDir(String lastDir) {
        this.lastDir = lastDir;
    }

    public UserPreferences()
    {
        fileName = ".fdt_prefs.txt";

        props = new Properties();

        home = System.getProperty("user.home");

        dir = new File(home);

        lastDir = "";

        UserPreferences.guiLogger = GUILogger.getGUILoggerInstance();
    }

    public void storePref()
    {
        File file = new File(dir,fileName);
        props.setProperty("Directory", lastDir);
        
        try {
            props.store(new FileOutputStream(file.toString()),null);

        } catch (IOException ex) {
            guiLogger.logError("Could not write preferences file." , ex);
        }
    }

    public void loadPref()
    {
            File file = new File(dir,fileName);
            if(file.exists())
            {
                guiLogger.log(Level.INFO, "Prefs file Exists. Using it as default fileChooser directory.");
                try {
                    props.load(new FileInputStream(file.toString()));
                    lastDir = props.getProperty("Directory");
                } catch (Exception ex) {
                    guiLogger.logError("Could not load preferences file. Will use default home directory");
                    lastDir = home;
                }
            }
            else
                lastDir = home;

           
    }


}
