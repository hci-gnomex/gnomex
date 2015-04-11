/*
 * $Id: DiskWriterManagerMonitoringTask.java,v 1.1 2012-10-29 22:30:15 HCI\rcundick Exp $
 */
package lia.util.net.copy.monitoring;

import lia.util.net.copy.Accountable;
import lia.util.net.copy.disk.DiskWriterManager;
import lia.util.net.copy.monitoring.base.AbstractAccountableMonitoringTask;

/**
 * 
 * Monitors disk activity 
 * 
 * @author ramiro
 * 
 */
public class DiskWriterManagerMonitoringTask extends AbstractAccountableMonitoringTask {

    
    private final DiskWriterManager diskWriterManager;
    
    
    public DiskWriterManagerMonitoringTask(DiskWriterManager diskWriterManager) {
        super(new Accountable[] {diskWriterManager});
        this.diskWriterManager = diskWriterManager;
    }
    
    public void rateComputed() {
        if(diskWriterManager.sessionsSize() == 0) {
            resetAllCounters();
        }
    }

}
