/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import lia.util.net.copy.FDT;

/**
 * This class is the entry point into fdt code. Here It is decided here whether to start the server fdt of client fdt.
 * 
 * @author Shobhit
 */
public class FdtMain {
    private static FdtGUI guiInstance = null;
    private static LogPanel logPanelInstance = null;
    private static boolean isServerMode;

    public static final String appOS=System.getProperty("os.name");

    FdtMain()
    {
        if(!isServerMode)
        {

            FdtMain.logPanelInstance = LogPanel.getLogPanelInstance();
            FdtMain.guiInstance = FdtGUI.getFdtGUIInstance();
            FdtMain.guiInstance.pack();
            if(FdtMain.guiInstance.isIsUpload())
            {
                TableColumn tcol = FdtMain.guiInstance.statusTable.getColumnModel().getColumn(1);
                FdtMain.guiInstance.statusTable.removeColumn(tcol);
            }
            else
            {
                TableColumn tcol = FdtMain.guiInstance.statusTable.getColumnModel().getColumn(0);
                FdtMain.guiInstance.statusTable.removeColumn(tcol);
            }

            FdtMain.guiInstance.setVisible(true);
            FdtMain.guiInstance.showFileChooser();
        }

        
    }

    public static boolean isIsServerMode() {
        return isServerMode;
    }

    public static void main (String args[]){
        if(args.length ==3)
        {
            FdtMain.guiInstance = FdtGUI.getFdtGUIInstance();

            if(!(args[0].equals("") || args[0] == null))
            guiInstance.setServerName(args[0]);

            if(!(args[1].equals("") || args[1] == null))
            {
                if(args[1].equals("upload"))
                guiInstance.setIsUpload(true);
                else if(args[1].equals("download"))
                    guiInstance.setIsUpload(false);
            }

            if(!(args[2].equals("") || args[2] == null))
            {
                guiInstance.setDestinationDir(args[2]);
                guiInstance.setIsDirectory(true);
            }

            isServerMode = false;

            SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                	if(FdtMain.appOS.startsWith("Windows")){
                            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                	}
                	else{
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                	}

                } catch (Exception exception) {
                    exception.printStackTrace();;
                }
                new FdtMain();
            }


    });
  
        }
        else
        {
            isServerMode = true;
            try {
                FDT.main(args);
            } catch (Exception ex) {
                
            }
        }
    }
}
