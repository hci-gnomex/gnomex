// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.awt.event.ActionEvent;
import java.awt.Dimension;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.util.Iterator;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import com.affymetrix.genometryImpl.event.ReportBugAction;
import com.affymetrix.genometryImpl.event.OKAction;
import java.util.ArrayList;
import com.affymetrix.genometryImpl.event.GenericAction;
import java.util.List;
import java.util.logging.Level;

public abstract class ErrorHandler implements DisplaysError
{
    private static DisplaysError displayHandler;
    
    public static void setDisplayHandler(final DisplaysError dH) {
        if (dH == null) {
            ErrorHandler.displayHandler = new ErrorHandler() {};
        }
        else {
            ErrorHandler.displayHandler = dH;
        }
    }
    
    public static void errorPanel(final String message) {
        errorPanel("ERROR", message, (Throwable)null, Level.SEVERE);
    }
    
    public static void errorPanel(final String message, final Throwable e, final Level level) {
        errorPanel("ERROR", message, e, level);
    }
    
    public static void errorPanel(final String title, final String message, final Level level) {
        errorPanel(title, message, (Throwable)null, level);
    }
    
    public static void errorPanel(final String title, final String message, final List<Throwable> errs, final Level level) {
        errorPanel(title, message, errs, null, level);
    }
    
    public static void errorPanelWithReportBug(final String title, final String message, final Level level) {
        final List<GenericAction> actions = new ArrayList<GenericAction>();
        actions.add(OKAction.getAction());
        actions.add(ReportBugAction.getAction());
        errorPanel(title, message, new ArrayList<Throwable>(), actions, level);
    }
    
    @Override
    public void showError(final String title, final String message, final List<GenericAction> actions, final Level level) {
        final Component scroll_pane = makeScrollPane(message);
        String[] options = null;
        if (actions != null) {
            options = new String[actions.size()];
            for (int i = 0; i < actions.size(); ++i) {
                options[i] = actions.get(i).getText();
            }
        }
        final JOptionPane pane = new JOptionPane(scroll_pane, 0, -1, null, options);
        final JDialog dialog = pane.createDialog(null, title);
        dialog.setResizable(true);
        if (SwingUtilities.isEventDispatchThread()) {
            processDialog(pane, dialog, actions);
        }
        else {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    processDialog(pane, dialog, actions);
                }
            });
        }
    }
    
    private static void errorPanel(final String title, final String message, final Throwable e, final Level level) {
        final List<Throwable> errs = new ArrayList<Throwable>();
        if (e != null) {
            errs.add(e);
        }
        errorPanel(title, message, errs, null, level);
    }
    
    private static void errorPanel(final String title, String message, final List<Throwable> errs, final List<GenericAction> actions, final Level level) {
        System.out.flush();
        System.err.flush();
        System.err.println();
        System.err.println("-------------------------------------------------------");
        if (!errs.isEmpty()) {
            for (final Throwable e : errs) {
                final String error_message = e.toString();
                message = message + "\n" + error_message;
                for (Throwable cause = e.getCause(); cause != null; cause = cause.getCause()) {
                    message = message + "\n\nCaused by:\n" + cause.toString();
                }
            }
        }
        System.err.println(title + ": " + message);
        if (!errs.isEmpty()) {
            for (final Throwable e : errs) {
                if (e instanceof FileNotFoundException) {
                    continue;
                }
                message += "\nSee console output for more details about this error.";
                e.printStackTrace(System.err);
            }
        }
        System.err.println("-------------------------------------------------------");
        System.err.println();
        System.err.flush();
        Toolkit.getDefaultToolkit().beep();
        ErrorHandler.displayHandler.showError(title, message, actions, level);
    }
    
    private static JScrollPane makeScrollPane(final String message) {
        final JTextPane text = new JTextPane();
        text.setContentType("text/plain");
        text.setText(message);
        text.setEditable(false);
        text.setCaretPosition(0);
        final JScrollPane scroller = new JScrollPane(text);
        scroller.setPreferredSize(new Dimension(400, 100));
        return scroller;
    }
    
    private static void processDialog(final JOptionPane pane, final JDialog dialog, final List<GenericAction> actions) {
        dialog.setVisible(true);
        final Object selectedValue = pane.getValue();
        if (selectedValue != null && actions != null) {
            for (final GenericAction action : actions) {
                if (action != null && selectedValue.equals(action.getText())) {
                    action.actionPerformed(null);
                }
            }
        }
    }
    
    static {
        setDisplayHandler(null);
    }
}
