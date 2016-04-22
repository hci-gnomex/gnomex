// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.awt.event.WindowListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.OutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;
import java.awt.Rectangle;
import javax.swing.JScrollPane;
import java.awt.Container;
import java.awt.Window;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.BorderLayout;
import java.awt.Frame;
import javax.swing.JFrame;

public final class ConsoleView
{
    private static final String encoding;
    private static JFrame frame;
    
    public static void init(final String APP_NAME) {
        getFrame(APP_NAME);
    }
    
    public static void showConsole(final String APP_NAME) {
        if (ConsoleView.frame == null) {
            init(APP_NAME);
        }
        ConsoleView.frame.doLayout();
        ConsoleView.frame.repaint();
        DisplayUtils.bringFrameToFront(ConsoleView.frame);
    }
    
    private static JFrame getFrame(final String APP_NAME) {
        final String TITLE = APP_NAME + " Console";
        if (ConsoleView.frame == null) {
            ConsoleView.frame = createFrame(TITLE);
            final Container cpane = ConsoleView.frame.getContentPane();
            cpane.setLayout(new BorderLayout());
            final JScrollPane outPane = createOutPane();
            cpane.add(outPane, "Center");
            ConsoleView.frame.pack();
            final Rectangle pos = PreferenceUtils.retrieveWindowLocation(TITLE, ConsoleView.frame.getBounds());
            if (pos != null) {
                PreferenceUtils.setWindowSize(ConsoleView.frame, pos);
            }
        }
        return ConsoleView.frame;
    }
    
    private static JScrollPane createOutPane() {
        final JTextArea outArea = new JTextArea(20, 50);
        outArea.setEditable(false);
        final JScrollPane outPane = new JScrollPane(outArea, 22, 32);
        try {
            System.setOut(new PrintStream(new JTextAreaOutputStream(outArea, System.out), false, ConsoleView.encoding));
            System.setErr(new PrintStream(new JTextAreaOutputStream(outArea, System.err), false, ConsoleView.encoding));
        }
        catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ConsoleView.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (SecurityException se) {
            final String str = "The application may not have permission to re-direct output to this view on your system.  \nYou should be able to view output in the Java console, WebStart console, or wherever you normally would view program output.  \n\n";
            outArea.append(str);
        }
        return outPane;
    }
    
    private static JFrame createFrame(final String TITLE) {
        final JFrame frame = new JFrame(TITLE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent evt) {
                PreferenceUtils.saveWindowLocation(frame, TITLE);
            }
        });
        return frame;
    }
    
    static {
        final String enc = System.getProperty("file.encoding");
        encoding = ((enc == null || enc.isEmpty()) ? "UTF-8" : enc);
    }
    
    private static final class JTextAreaOutputStream extends OutputStream
    {
        private static final Charset charset;
        JTextArea ta;
        PrintStream original;
        
        public JTextAreaOutputStream(final JTextArea t, final PrintStream echo) {
            this.ta = t;
            this.original = echo;
        }
        
        @Override
        public void write(final int b) throws IOException {
            this.write(new byte[] { (byte)b }, 0, 1);
        }
        
        @Override
        public void write(final byte[] b) throws IOException {
            this.write(b, 0, b.length);
        }
        
        @Override
        public void write(final byte[] b, final int off, final int len) throws IOException {
            this.ta.append(new String(b, off, len, JTextAreaOutputStream.charset));
            if (this.original != null) {
                this.original.write(b, off, len);
            }
        }
        
        static {
            charset = Charset.forName(ConsoleView.encoding);
        }
    }
}
