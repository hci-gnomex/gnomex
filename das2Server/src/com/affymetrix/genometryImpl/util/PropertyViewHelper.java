// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.awt.Point;
import java.awt.event.MouseEvent;
import javax.swing.JTable;
import java.awt.Cursor;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;

public class PropertyViewHelper implements MouseListener, MouseMotionListener
{
    private final Cursor handCursor;
    private final Cursor defaultCursor;
    private final JTable table;
    
    public PropertyViewHelper(final JTable table) {
        this.handCursor = new Cursor(12);
        this.defaultCursor = null;
        (this.table = table).addMouseListener(this);
        table.addMouseMotionListener(this);
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
        final Point p = e.getPoint();
        final int row = this.table.rowAtPoint(p);
        final int column = this.table.columnAtPoint(p);
        if (this.isURLField(row, column)) {
            GeneralUtils.browse((String)this.table.getValueAt(row, column));
        }
    }
    
    @Override
    public void mouseMoved(final MouseEvent e) {
        final Point p = e.getPoint();
        final int row = this.table.rowAtPoint(p);
        final int column = this.table.columnAtPoint(p);
        if (this.isURLField(row, column)) {
            this.table.setCursor(this.handCursor);
        }
        else if (this.table.getCursor() != this.defaultCursor) {
            this.table.setCursor(this.defaultCursor);
        }
    }
    
    @Override
    public void mouseDragged(final MouseEvent e) {
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
    }
    
    @Override
    public void mouseExited(final MouseEvent e) {
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
    }
    
    private boolean isURLField(final int row, final int column) {
        if (row > this.table.getRowCount() || column > this.table.getColumnCount() || row < 0 || column < 0) {
            return false;
        }
        final String value = (String)this.table.getValueAt(row, column);
        return value.length() > 0 && value.startsWith("<html>");
    }
}
