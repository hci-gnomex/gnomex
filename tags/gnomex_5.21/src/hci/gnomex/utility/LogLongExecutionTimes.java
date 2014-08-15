package hci.gnomex.utility;

import hci.gnomex.model.PropertyDictionary;

import java.util.ArrayList;
import java.util.Date;

public class LogLongExecutionTimes {
  private org.apache.log4j.Logger log;
  private long millisecondCutOff;
  private boolean doLog;
  private String descriptionPrefix;
  private ArrayList<LogItem> logItems = new ArrayList<LogItem>();
  
  public LogLongExecutionTimes(org.apache.log4j.Logger log, long millisecondCutOff, boolean doLog, String descriptionPrefix) {
    init(log, millisecondCutOff, doLog, descriptionPrefix);
  }
  
  public LogLongExecutionTimes(org.apache.log4j.Logger log, PropertyDictionaryHelper dh, String descriptionPrefix) {
    String enabled = dh.getProperty(PropertyDictionary.PERFORMANCE_LOG_ENABLED);
    long cutOff = 300000; // default to 5 minutes
    String cutOffString = dh.getProperty(PropertyDictionary.PERFORMANCE_LOG_CUTOFF_MILLISECONDS);
    if (cutOffString != null) {
      try {
        cutOff = Long.parseLong(cutOffString);
      } catch(Exception e) {
      }
    }
    init(log, cutOff, enabled != null && enabled.toUpperCase().equals("Y"), descriptionPrefix);
  }
  
  private void init(org.apache.log4j.Logger log, long millisecondCutOff, boolean doLog, String descriptionPrefix) {
    this.log = log;
    this.millisecondCutOff = millisecondCutOff;
    this.doLog = doLog;
    this.descriptionPrefix = descriptionPrefix;
  }
  
  public LogItem startLogItem(String description) {
    LogItem newItem = null;
    if (doLog) {
      newItem = new LogItem(description);
    }
    return newItem;
  }
  
  public void endLogItem(LogItem item) {
    if (doLog && item != null) {
      item.endTime = new Date();
      logItems.add(item);
    }
  }
  
  public void LogTimes() {
    boolean doit = doLog;
    if (doLog) {
      Date firstStartTime = null;
      Date lastEndTime = null;
      for (LogItem i:logItems) {
        if (firstStartTime == null && i.getStartTime() != null) {
          firstStartTime = i.getStartTime();
        }
        if (i.getEndTime() != null) {
          lastEndTime = i.getEndTime();
        }
      }
      if (firstStartTime == null || lastEndTime == null || lastEndTime.getTime() - firstStartTime.getTime() < this.millisecondCutOff) {
        doit = false;
      }
    }
    
    if (doit) {
      for (LogItem i:logItems) {
        StringBuffer buf = new StringBuffer();
        buf.append("Perf:");
        if (descriptionPrefix != null && descriptionPrefix.length() > 0) {
          buf.append(descriptionPrefix).append(":");
        }
        if (i.getDescription() != null && i.getDescription().length() > 0) {
          buf.append(i.getDescription()).append(":");
        }
        buf.append(" -- Elapsed:")
          .append(Long.toString(i.getElapsedTime()))
          .append(" Start:");
        if (i.getStartTime() == null) {
          buf.append("N/A");
        } else {
          buf.append(i.getStartTime().toString());
        }
        buf.append("  End:");
        if (i.getEndTime() == null) {
          buf.append("N/A");
        } else {
          buf.append(i.getEndTime().toString());
        }
        log.warn(buf.toString());
      }
    }
  }
  
  public class LogItem {
    private Date startTime;
    private Date endTime;
    private String description;
    
    private LogItem(String description) {
      startTime = new Date();
      this.description = description;
    }
    
    public long getElapsedTime() {
      long elapsedTime = 0;
      if(startTime != null && endTime != null) {
        elapsedTime = endTime.getTime() - startTime.getTime();
      }
      return elapsedTime;
    }
    
    public String getDescription() {
      return description;
    }
    
    public Date getStartTime() { 
      return startTime;
    }
    
    public Date getEndTime() {
      return endTime;
    }
  }
}
