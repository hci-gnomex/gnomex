// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import java.awt.event.ActionEvent;

public class OKAction extends GenericAction
{
    private static final long serialVersionUID = 1L;
    private static final OKAction ACTION;
    
    public static OKAction getAction() {
        return OKAction.ACTION;
    }
    
    private OKAction() {
        super("OK", null, null);
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        super.actionPerformed(e);
    }
    
    static {
        ACTION = new OKAction();
        GenericActionHolder.getInstance().addGenericAction(OKAction.ACTION);
    }
}
