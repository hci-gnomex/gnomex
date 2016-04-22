// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JViewport;
import javax.swing.JTable;
import java.awt.Frame;

public final class DisplayUtils
{
    public static void bringFrameToFront(final Frame frame) {
        if ((frame.getExtendedState() & 0x1) == 0x1) {
            frame.setExtendedState(frame.getExtendedState() & 0xFFFFFFFE);
        }
        if (!frame.isShowing()) {
            frame.setVisible(true);
        }
        frame.toFront();
    }
    
    public static void scrollToVisible(final JTable table, final int rowIndex, final int vColIndex) {
        if (!(table.getParent() instanceof JViewport)) {
            return;
        }
        final JViewport viewport = (JViewport)table.getParent();
        final Rectangle rect = table.getCellRect(rowIndex, vColIndex, true);
        final Point pt = viewport.getViewPosition();
        rect.setLocation(rect.x - pt.x, rect.y - pt.y);
        viewport.scrollRectToVisible(rect);
    }
}
