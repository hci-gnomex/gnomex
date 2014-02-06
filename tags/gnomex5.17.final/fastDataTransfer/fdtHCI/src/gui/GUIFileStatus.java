/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.concurrent.TimeUnit;
import lia.util.net.common.Utils;

/**
 *This class has all logic to update file status, ETA, %completed on the fdtGUI. It doesnt update it directly but calls updateTable 
 * to update the table.
 *
 * @author Shobhit
 */
public class GUIFileStatus {

    private static UpdateTable updateTable;

    private static GUIFileStatus guiFileStatus;

    private static Hashtable sessionIds;
    
    private static String ETA = null;

    private String transferRate;

    private static ArrayList<FileInfo> fileList;
    
    private GUIFileStatus()
    {
        updateTable = UpdateTable.getUpdateTableInstance();
        sessionIds = new Hashtable();
        fileList = new ArrayList<FileInfo>();
    }

    public void initFileInfo(String fileName, String sessionId, String from, String to, long fileSize)
    {
        FileInfo file = new FileInfo();
        file.setSessionId(sessionId);
        file.setFileSize(fileSize);
        file.setFileName(fileName);
        fileList.add(file);
    }

    //Sets the start time when the file starts transferring.
    public void setStartTime(String session, long startTime)
    {
            for(FileInfo f:fileList)
            {
                if(f.getSessionId().equals(session) && f.getStartTime()==-1)
                {
                  f.setStartTime(startTime);
                }
            }
    }

    //Sets the stop time when the file is finished transferring.
    public void setEndTime(String session, long stopTime)
    {
        for(FileInfo f:fileList)
            {
                if(f.getSessionId().equals(session) && f.getEndTime()==-1)
                {
                  f.setEndTime(stopTime);
                }
            }
    }

    //Average transfer rate during the transfer is calculated here.
    public void calculateAvgTransferRate(String session)
    {
        DecimalFormat df = new DecimalFormat("###.##");

        calculateSizeUnit();

        for(FileInfo f:fileList)
        {
            if(f.getSessionId().equals(session))
            {
                f.setCompleted(true);
                String sizeString= df.format(f.getFormattedSize())+f.getSizeUnit();

                int row = (Integer) sessionIds.get(session);

                if(f.getStartTime()!=-1 && f.getEndTime()!=-1)
                {
                    long duration = (TimeUnit.NANOSECONDS.toMillis(f.getEndTime() - f.getStartTime())) / 1000;
                    String time = Utils.getETA(duration);

                    if(duration ==0)
                        duration=1;

                    String avgTransferRate = calculateSpeedUnit(f, duration);

                    updateTable.setCellValueAt("Transferred " + sizeString + " @ " + avgTransferRate + " in " + time, row, 3);
                }
                else if((f.getEndTime() -f.getStartTime()) == 0)
                {
                    updateTable.setCellValueAt("Transferred " + sizeString + " @ " + sizeString+"/s"+ " in " + "0s", row, 3);
                }
                else
                {
                    updateTable.setCellValueAt("Not Transferred. File already present.", row, 3);
                }

            }
        }
    }

    //Appropriate size unit to be displayed is calculated here. We do not want to display 100000 bytes but rather 100KB.
    public void calculateSizeUnit()
    {
        String unitArray[]={"B","KB","MB","GB","PB"};
        //Assumption that we will not be needing more than 999 PB soon.

        
        for(FileInfo f:fileList)
        {
              double size = f.getFileSize();

              String unit = "B";
              
              if(f.getSizeUnit() != null && !f.getSizeUnit().equals("B"))
                  continue;

              while(size >1000)
              {
                    size=size/1024;
                    for(int i=0;i<unitArray.length - 1;i++)
                    {
                        if(unitArray[i].equals(unit))
                        {
                            unit = unitArray[i+1];
                            break;
                        }
                    }

              }
              f.setFormattedSize(size);
              f.setSizeUnit(unit);
         }
    }

    //Appropriate speed unit to be displayed is calculated here. We do not want to display 100000 bytes/s but rather 100KB/s.
    public String calculateSpeedUnit(FileInfo f, long duration)
    {

        DecimalFormat df = new DecimalFormat("###.##");
        String unitArray[]={"B","KB","MB","GB","PB"};
        //Assumption that we will not be needing more than 999 PB soon.

        double rate = f.getFileSize()/duration;

        String unit = "B";

        while(rate >1000)
        {
            rate = rate/1024;
            for(int i=0;i<unitArray.length - 1;i++)
            {
                if(unitArray[i].equals(unit))
                {
                    unit = unitArray[i+1];
                    break;
                }
            }

         }
        return (df.format(rate) + unit +"/s");

    }

    public static synchronized GUIFileStatus getGUIFileStatusInstance()
    {
        if(guiFileStatus == null)
               guiFileStatus = new GUIFileStatus();
        return guiFileStatus;
    }

    public void updatePercentCompleted(String session, long done, long fileSize)
    {
        
        int row = (Integer)sessionIds.get(session);
        if(fileSize > 0) {
            float pcentCompleted = (( done*100)/fileSize);
            updateTable.setCellValueAt((long)pcentCompleted , row, 1);
            updateFileStatus(row,(long)pcentCompleted,session);
        }
        else if(done == fileSize) {
            updateTable.setCellValueAt(100 , row, 1);
            updateFileStatus(row,100,session);
        }
        updateTable.checkAllRowsForDone(); 
    }

    public void  updateFileStatus(int row, long pcentCompleted,String session)
    {
        if(pcentCompleted == 100)
        {
            updateTable.setCellValueAt("Completed" , row, 3);
            updateTable.setCellValueAt("0", row, 2);
            calculateAvgTransferRate(session);
        }
        else if(pcentCompleted <100)
        {
            updateTable.setCellValueAt("In Progress", row, 3);
        }
        else
            updateTable.setCellValueAt("In Queue", row, 3);
    }

    public void setETA(String eta)
    {
        ETA = eta;
        updateETA();
    }

    public void setTransferRate(long rate)
    {
        final DecimalFormat SIZE_DECIMAL_FORMAT = ((DecimalFormat) DecimalFormat.getNumberInstance());
        SIZE_DECIMAL_FORMAT.applyPattern("##0.00");
        double rateKB = rate/1024;
        double rateMB;

        if(rateKB>10)
        {
            rateMB = rateKB/1024;
            transferRate = SIZE_DECIMAL_FORMAT.format(rateMB)+"MB/s";
        }
        else
        {
            transferRate = SIZE_DECIMAL_FORMAT.format(rateKB)+"KB/s";
        }
    }

    public void updateETA()
    {
        for(int i=0;i<updateTable.getNum_files();i++)
        {
            if(updateTable.getCellValueAt(i, 1).toString().equals("100") || updateTable.getCellValueAt(i, 1).toString().equals("0"))
                continue;
            else
            {
                String update = ETA + " @ " + transferRate ;
                updateTable.setCellValueAt(update, i, 2);
            }

        }
    }

    public void initTable(String from, String to, String session, long fileSize)
    {
        
        int row = updateTable.getNum_files();
        updateTable.addNewFile(from, to);
        sessionIds.put(session, row);

    }

    //Used only while uploading. Only difference compared to the other update function is because while uploading we populate the table
    //as soon as files are selected.
    public void initUploadTable(String from, String to, String session, long fileSize)
    {

        int row = updateTable.getNum_files();
        for(FileInfo f:fileList)
        {
            if(f.getFileName().equals(from))
            {
                sessionIds.put(session,updateTable.getFileRow(from));
                return;
            }
            else
                continue;
        }

    }

    //Function to check whether file already exists and has not been transferred again.
    public void existingFileUpdates()
    {
        for(FileInfo f:fileList)
        {
            int row = (Integer)sessionIds.get(f.getSessionId());
            if(!updateTable.getCellValueAt(row, 1).toString().equals("100"))
            {
                updateTable.setCellValueAt("Not Transferred. File already Present", row, 3);
                f.setCompleted(true);
            }
        }
    }

}
