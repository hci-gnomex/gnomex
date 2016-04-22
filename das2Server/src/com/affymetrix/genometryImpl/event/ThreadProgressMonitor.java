// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.event;

import javax.swing.SwingUtilities;
import java.util.List;
import javax.swing.Icon;
import java.util.ArrayList;
import javax.swing.UIManager;
import java.beans.PropertyChangeEvent;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public final class ThreadProgressMonitor
{
    private JOptionPane opt_pane;
    private JDialog dialog;
    private final Thread thread;
    private boolean is_closed;
    PropertyChangeListener pcl;
    
    public ThreadProgressMonitor(final Component c, final String title, final Object message, final Thread t, final boolean can_cancel, final boolean can_dismiss) {
        this.is_closed = false;
        this.pcl = new PropertyChangeListener() {
            @Override
            public void propertyChange(final PropertyChangeEvent evt) {
                final String prop = evt.getPropertyName();
                final Object value = ThreadProgressMonitor.this.opt_pane.getValue();
                if (evt.getSource() == ThreadProgressMonitor.this.opt_pane && prop.equals("value") && value != null) {
                    if (value.equals(UIManager.getString("OptionPane.cancelButtonText"))) {
                        ThreadProgressMonitor.this.cancelPressed();
                    }
                    else if (value.equals(UIManager.getString("OptionPane.okButtonText"))) {
                        ThreadProgressMonitor.this.okPressed();
                    }
                }
            }
        };
        String cancel_text = UIManager.getString("OptionPane.cancelButtonText");
        if (cancel_text == null) {
            cancel_text = "Cancel";
        }
        String ok_text = UIManager.getString("OptionPane.okButtonText");
        if (ok_text == null) {
            ok_text = "OK";
        }
        final List<String> button_list = new ArrayList<String>(2);
        if (can_dismiss) {
            button_list.add(ok_text);
        }
        if (can_cancel) {
            button_list.add(cancel_text);
        }
        final String[] buttons = button_list.toArray(new String[button_list.size()]);
        this.thread = t;
        this.opt_pane = new JOptionPane(message, 1, -1, null, buttons);
        (this.dialog = this.opt_pane.createDialog(c, title)).setDefaultCloseOperation(0);
        this.opt_pane.addPropertyChangeListener(this.pcl);
    }
    
    private void setMessage(final Object o) {
        if (this.is_closed) {
            return;
        }
        this.opt_pane.setMessage(o);
        this.dialog.pack();
    }
    
    public void setMessageEventually(final Object o) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ThreadProgressMonitor.this.setMessage(o);
            }
        });
    }
    
    private void showDialog() {
        if (this.is_closed) {
            return;
        }
        this.dialog.pack();
        this.dialog.setVisible(true);
    }
    
    public void showDialogEventually() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ThreadProgressMonitor.this.showDialog();
            }
        });
    }
    
    public void closeDialog() {
        this.is_closed = true;
        this.dialog.setVisible(false);
        this.opt_pane.removePropertyChangeListener(this.pcl);
        this.pcl = null;
        this.opt_pane = null;
        this.dialog = null;
    }
    
    public void closeDialogEventually() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ThreadProgressMonitor.this.closeDialog();
            }
        });
    }
    
    private void cancelPressed() {
        this.dialog.setEnabled(false);
        this.closeDialog();
        if (this.thread != null) {
            this.thread.interrupt();
        }
    }
    
    private void okPressed() {
        this.closeDialog();
    }
}
