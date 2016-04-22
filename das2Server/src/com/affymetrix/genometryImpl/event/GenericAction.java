// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import java.util.Iterator;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import com.affymetrix.common.CommonUtils;
import java.util.HashSet;
import java.util.Set;
import javax.swing.AbstractAction;

public abstract class GenericAction extends AbstractAction
{
    private static final long serialVersionUID = 1L;
    private static final char POPUP_DIALOG = '\u2026';
    private final String text;
    private final String tooltip;
    private final String iconPath;
    private final String largeIconPath;
    private final int mnemonic;
    private final Object extraInfo;
    private final boolean popup;
    private Set<GenericActionDoneCallback> doneCallbacks;
    protected int ordinal;
    
    public GenericAction(final String text, final int mnemonic) {
        this(text, null, null, null, mnemonic);
    }
    
    public GenericAction(final String text, final String iconPath, final String largeIconPath) {
        this(text, null, iconPath, largeIconPath, 0);
    }
    
    public GenericAction(final String text, final String tooltip, final String iconPath, final String largeIconPath, final int mnemonic) {
        this(text, tooltip, iconPath, largeIconPath, mnemonic, null, false);
    }
    
    public GenericAction(final String text, final String tooltip, final String iconPath, final String largeIconPath, final int mnemonic, final Object extraInfo, final boolean popup) {
        this.ordinal = 0;
        this.text = text;
        this.tooltip = tooltip;
        this.iconPath = iconPath;
        this.largeIconPath = largeIconPath;
        this.mnemonic = mnemonic;
        this.extraInfo = extraInfo;
        this.popup = popup;
        this.doneCallbacks = new HashSet<GenericActionDoneCallback>();
        this.putValue("Name", this.getDisplay());
        if (iconPath != null) {
            final ImageIcon icon = CommonUtils.getInstance().getIcon(iconPath);
            if (icon == null) {
                System.out.println("icon " + iconPath + " returned null");
            }
            this.putValue("SmallIcon", icon);
        }
        if (largeIconPath != null) {
            final ImageIcon icon = CommonUtils.getInstance().getIcon(largeIconPath);
            if (icon == null) {
                System.out.println("icon " + largeIconPath + " returned null");
            }
            this.putValue("SwingLargeIconKey", icon);
        }
        if (mnemonic != 0) {
            this.putValue("MnemonicKey", mnemonic);
        }
        if (tooltip != null) {
            this.putValue("ShortDescription", tooltip);
        }
        if (tooltip == null && text != null) {
            this.putValue("ShortDescription", text);
        }
    }
    
    public final String getText() {
        return this.text;
    }
    
    public String getDisplay() {
        if (this.text == null) {
            return null;
        }
        return this.text + (this.popup ? "\u2026" : "");
    }
    
    public final String getTooltip() {
        return this.tooltip;
    }
    
    public final String getIconPath() {
        return this.iconPath;
    }
    
    public final int getMnemonic() {
        return this.mnemonic;
    }
    
    public final Object getExtraInfo() {
        return this.extraInfo;
    }
    
    public String getId() {
        return this.getClass().getName();
    }
    
    public final String getLargeIconPath() {
        return this.largeIconPath;
    }
    
    @Override
    public void actionPerformed(final ActionEvent e) {
        GenericActionHolder.getInstance().notifyActionPerformed(this);
    }
    
    public boolean usePrefixInMenu() {
        return false;
    }
    
    public boolean isToggle() {
        return false;
    }
    
    public final boolean isPopup() {
        return this.popup;
    }
    
    public void addDoneCallback(final GenericActionDoneCallback doneCallback) {
        this.doneCallbacks.add(doneCallback);
    }
    
    public void removeDoneCallback(final GenericActionDoneCallback doneCallback) {
        this.doneCallbacks.remove(doneCallback);
    }
    
    protected void actionDone() {
        for (final GenericActionDoneCallback doneCallback : this.doneCallbacks) {
            doneCallback.actionDone(this);
        }
    }
    
    public final int getOrdinal() {
        return this.ordinal;
    }
}
