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
 * Class used when starting fdtClient for uploading.
 *
 * @author Shobhit
 */
public class Upload extends Transfer{
    private UserPreferences userPref;
    private static UpdateTable updateTable;
    
    public Upload()
    {
        updateTable = UpdateTable.getUpdateTableInstance();
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
        File[] file;

        this.localFileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.localFileChooser.setMultiSelectionEnabled(true);
        this.localFileChooser.setApproveButtonText("Choose");

        
        int returnVal = this.localFileChooser.showOpenDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
             file = this.localFileChooser.getSelectedFiles();

             userPref.setLastDir(this.localFileChooser.getCurrentDirectory().toString());

             userPref.storePref();

             file=updateTable.checkExistingFiles(file);

             guiInstance.files.addAll(Arrays.asList(file));

             for(int i=0;i<file.length;i++)
                 updateTable.addNewFile(file[i].getAbsolutePath(), "Server");

             guiInstance.selectFilesButton.setText("Select More Files");

                guiInstance.startButton.setEnabled(true);

        }
        else if(returnVal == JFileChooser.CANCEL_OPTION)
        {
            if(guiInstance.files.isEmpty())
            guiInstance.files.clear();

        }

    }


}
