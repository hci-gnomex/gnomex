// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import com.affymetrix.genometryImpl.util.GeneralUtils;
import java.awt.event.ActionEvent;

public class ReportBugAction extends GenericAction
{
    private static final long serialVersionUID = 1L;
    private static final ReportBugAction ACTION;
    
    public static ReportBugAction getAction() {
        return ReportBugAction.ACTION;
    }
    
    private ReportBugAction() {
        super("Report a bug", null, "16x16/apps/accessories-text-editor.png", "22x22/apps/accessories-text-editor.png", 82, null, true);
        this.ordinal = 130;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        super.actionPerformed(e);
        GeneralUtils.browse("http://sourceforge.net/tracker/?group_id=129420&atid=714744");
    }
    
    static {
        ACTION = new ReportBugAction();
        GenericActionHolder.getInstance().addGenericAction(ReportBugAction.ACTION);
    }
}
