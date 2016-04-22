// 
// Decompiled by Procyon v0.5.30
// 

package com.affymetrix.genometryImpl.util;

import java.net.URISyntaxException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.File;
import java.awt.datatransfer.Transferable;
import java.io.IOException;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.net.URL;
import java.util.List;
import java.awt.datatransfer.DataFlavor;
import javax.swing.TransferHandler;

public abstract class FileDropHandler extends TransferHandler
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public boolean canImport(final TransferSupport support) {
        return support.isDataFlavorSupported(DataFlavor.javaFileListFlavor) || support.isDataFlavorSupported(DataFlavor.stringFlavor);
    }
    
    @Override
    public boolean importData(final TransferSupport support) {
        if (!this.canImport(support)) {
            return false;
        }
        final Transferable t = support.getTransferable();
        try {
            if (support.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                final List<File> files = (List<File>)t.getTransferData(DataFlavor.javaFileListFlavor);
                this.openFileAction(files);
                return true;
            }
            DataFlavor uriListFlavor = null;
            String data = null;
            try {
                uriListFlavor = new DataFlavor("text/uri-list;class=java.lang.String");
                if (uriListFlavor != null && support.isDataFlavorSupported(uriListFlavor)) {
                    data = (String)t.getTransferData(uriListFlavor);
                }
            }
            catch (ClassNotFoundException ex3) {}
            if (data != null) {
                final List<URL> urls = textURLList(data);
                if (urls != null) {
                    for (final URL url : urls) {
                        this.openURLAction(url.toString());
                    }
                    return true;
                }
            }
            final String url2 = (String)t.getTransferData(DataFlavor.stringFlavor);
            this.openURLAction(url2);
        }
        catch (UnsupportedFlavorException ex) {
            return false;
        }
        catch (IOException ex2) {
            return false;
        }
        return true;
    }
    
    private static List<URL> textURLList(final String data) {
        final List<URL> list = new ArrayList<URL>();
        final StringTokenizer st = new StringTokenizer(data, "\r\n");
        while (st.hasMoreTokens()) {
            final String s = st.nextToken();
            if (s.charAt(0) == '#') {
                continue;
            }
            try {
                final URI uri = new URI(s);
                list.add(uri.toURL());
            }
            catch (MalformedURLException ex) {}
            catch (URISyntaxException e) {}
            catch (IllegalArgumentException ex2) {}
        }
        return list;
    }
    
    public abstract void openFileAction(final List<File> p0);
    
    public abstract void openURLAction(final String p0);
}
