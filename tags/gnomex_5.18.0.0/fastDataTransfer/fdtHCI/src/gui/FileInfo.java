/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui;

/**
 *This is a helper class which keeps information about files being transferred.
 * 
 * @author Shobhit
 */
public class FileInfo {
    private String fileName;
    private String SessionId;
    private String from;
    private String to;
    private double fileSize;
    private String sizeUnit;
    private long startTime;
    private long endTime;
    private long averageTransferRate;
    private boolean completed;
    private double formattedSize;

    public double getFormattedSize() {
        return formattedSize;
    }

    public void setFormattedSize(double formattedSize) {
        this.formattedSize = formattedSize;
    }
    
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getSessionId() {
        return SessionId;
    }

    public long getAverageTransferRate() {
        return averageTransferRate;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getFileName() {
        return fileName;
    }

    public double getFileSize() {
        return fileSize;
    }

    public String getFrom() {
        return from;
    }

    public long getStartTime() {
        return startTime;
    }

    public String getTo() {
        return to;
    }

    public String getSizeUnit() {
        return sizeUnit;
    }

    public void setSizeUnit(String sizeUnit) {
        this.sizeUnit = sizeUnit;
    }


    public FileInfo(String fileName, String SessionId) {
        this.fileName = fileName;
        this.SessionId = SessionId;
    }

    public FileInfo() {
        this.startTime = -1;
        this.endTime = -1;
        this.averageTransferRate=0;
        this.completed=false;
    }

    public void setSessionId(String SessionId) {
        this.SessionId = SessionId;
    }

    public void setAverageTransferRate(long averageTransferRate) {
        this.averageTransferRate = averageTransferRate;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setTo(String to) {
        this.to = to;
    }



}
