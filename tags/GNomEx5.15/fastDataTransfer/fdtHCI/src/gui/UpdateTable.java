/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Class used to update table cells. Used by GUIFileStatus class to update data.
 *
 * @author Shobhit
 */
public class UpdateTable{

    private static FdtGUI guiInstance = null;

    private static UpdateTable tableInstance = null;

    private static int num_files=0;

    private static Hashtable fileNames;

    private UpdateTable()
    {
        fileNames = new Hashtable();
        guiInstance = FdtGUI.getFdtGUIInstance();
    }

    public static synchronized UpdateTable getUpdateTableInstance()
    {
      if (tableInstance == null)

          tableInstance = new UpdateTable();
        return tableInstance;
    }

    public void addNewFile(String from, String to)
    {
        String rowData[]= {from, to, "0", "Not Available", "In Queue"};

        String fileName;
        if(guiInstance.isIsUpload())
        {
            File f = new File(from);
            fileName = f.getName();
            rowData[0] = fileName;  
            if(f.isDirectory()) {
              rowData[2] = "(Dir)";
              rowData[3] = "";
              rowData[4] = "(Status for individual files will appear below)";
            }

        }
        else
        {
            fileName = new File(to).getName();
            rowData[1] = fileName;
        }

        guiInstance.model.addRow(rowData);

        fileNames.put(from, num_files);

        num_files++;
    }
    
    
    // Check all status rows. Enable Exit button if finished.
    public void checkAllRowsForDone() {
      int rowCount, doneCount;
      if(guiInstance.isIsUpload()) {
        doneCount = 0;
        rowCount = guiInstance.model.getRowCount();
        for(int i=0;i<rowCount;i++) {
          if(this.getCellValueAt(i, 1).toString().equals("100") || this.getCellValueAt(i, 1).toString().equals("(Dir)")) {
            doneCount++;
          }
        }
        if(rowCount == doneCount) {
          guiInstance.setWaitingForRemote();         
          for(int i=0;i<rowCount;i++) {
            // If done then set contents of ETA for all rows to zero
            setCellValueAt("0", i, 2);
          }          
        }   
      }

    } 

    public int getFileRow(String fileName)
    {
      Object o = fileNames.get(fileName);
      if(o != null) {
        return (Integer) o;
      } 
      // If file not found then add it to list
      addNewFile(fileName, "Server");
      return (Integer)fileNames.get(fileName);
    }
    public Object getCellValueAt(int row, int column)
    {
        return guiInstance.statusTable.getValueAt(row, column);
    }

    public synchronized void setCellValueAt(String message, int row, int column)
    {
        guiInstance.statusTable.setValueAt(message, row, column);
    }

    public synchronized void setCellValueAt(long value, int row, int column)
    {
        guiInstance.statusTable.setValueAt(value, row, column);
    }

    public int getNum_files() {
        return num_files;
    }

    public void setNum_files(int num_files) {
        UpdateTable.num_files = num_files;
    }

    //Prevents user to select an already selected file again.
    File[] checkExistingFiles(File[] file) {
        ArrayList<File> temp = new ArrayList<File>();
        int j=0;
        for(int i=0;i<file.length;i++)
        {
            if(!fileNames.containsKey(file[i].getAbsolutePath()))
            {
                temp.add(file[i]);
            }
        }

        file = temp.toArray(new File[temp.size()]);
        return file;
    }

}
