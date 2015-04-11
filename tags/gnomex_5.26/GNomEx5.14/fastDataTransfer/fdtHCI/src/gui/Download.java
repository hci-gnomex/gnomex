/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import gui.utils.UserPreferences;
import java.io.File;
import java.util.Arrays;
import javax.swing.JFileChooser;

/**
 * This class is used when using the fdtGUI in download mode. 
 *
 * @author Shobhit
 */
public class Download extends Transfer{

    private UserPreferences userPref;

    public Download()
    {
        userPref = new UserPreferences();
        userPref.loadPref();
        if(!userPref.getLastDir().isEmpty() && userPref.getLastDir() !=null)
            localFileChooser = new JFileChooser(userPref.getLastDir());
        else
            localFileChooser = new JFileChooser(System.getProperty("user.home"));

    }


    @Override
    public void showFileChooser()
    {
        File file;
        localFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        localFileChooser.setMultiSelectionEnabled(false);
        localFileChooser.setApproveButtonText("Choose");


        guiInstance.startButton.setVisible(false);
      
        int returnVal = localFileChooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            file= localFileChooser.getSelectedFile();


            // On creating a new folder, the file chooser goes inside the new folder by default and then clicking save would save the file
            // with the name of the last directory you double clicked. So if there is a folder called temp and you create a new folder inside it
            //named NewFolder, the fileChooser would go inside this newFolder and now if you click save, it will save as temp/NewFolder/temp
            //coz temp is the last name you selected. But it turns out that this is neither a Directory nor a file as it doesnt exist right now
            //This check can be used to move one step higher in directory structure and save in temp/NewFolder.

            if(!file.isFile() && !file.isDirectory())
                file = file.getParentFile();

            guiInstance.files.addAll(Arrays.asList(file));

            userPref.setLastDir(file.toString());

            userPref.storePref();
            
        }
        else if(returnVal == JFileChooser.CANCEL_OPTION)
        {
            guiInstance.files.clear();
            guiInstance.selectFilesButton.setEnabled(true);

        }
    }

}
